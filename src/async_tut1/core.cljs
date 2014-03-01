(ns async-tut1.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [clojure.string :as string]
            [cljs.core.async :refer [alts!! alts! put! chan <! timeout]])
  (:import [goog.net Jsonp]
                      [goog Uri])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(def wiki-search-url
    "http://en.wikipedia.org/w/api.php?action=opensearch&format=json&search=")

(defn user-query[]
  (.-value (dom/getElement "query")))

(defn listen [element type]
  (let [out (chan)]
  (events/listen element type 
          (fn[e] (put! out e)))
    out))

(defn jsonp[uri]
  (let [out (chan)
        req (Jsonp. (Uri. uri))]
    (.send req nil #(put! out %))
    out))

(defn render-query [results]
  (str
    "<ul>"
    (apply str
      (for [result results]
        (str "<li>" result "</li>")))
    "</ul>"))

(defn query-url [q]
     (str wiki-search-url q))

(defn display-array-in-element [element arr]
  (set! (.-innerHTML element) (render-query arr)))

(defn display-results[results]
  (let [result-view (dom/getElement "results")]
    (display-array-in-element result-view results)))

(let [clicks (listen (dom/getElement "search") "click")]
  (go (while true
        (<! clicks)
        (loop [queries (list (user-query))]
          (when (< (count queries) 10)
            (let [results (second (<! (jsonp (query-url (first queries)))))
                  next-query (second results)]
              (display-results results)
              (when-not (nil? next-query)
                (<! (timeout 2000))
                (recur (conj queries next-query)))))
          (println "query is done: " (string/join "->" (reverse queries)))))))


(defn do-several-queries []
  (go (doseq [word ["tennis" "soccer" "bowling" "ping pong" "talmud"]]
      (println "searching for: " word)
      (let [t (timeout 500)
            [v ch] (alts! [t (jsonp (query-url word))])]
        (if (= t ch)
          (println "timeout: " word)
          (display-results (second v)))))))

(defn create-channels[n]
  ;; Since go blocks are lightweight processes not bound to threads, we
  ;; can have LOTS of them! Here we create 1000 go blocks that say hi on
  ;; 1000 channels. We use alts!! to read them as they're ready.

  (let [cs (repeatedly n chan)]
    (println "creating " n " channels")
    (go (time (doseq [c cs] (go (>! c "hi")))))
    (go (time (dotimes [i n]
      (let [[v c] (alts! cs)]
        (assert (= "hi" v))))))))

(let [clicks (listen (dom/getElement "channels") "click")]
  (go (while true
        (<! clicks)
        (create-channels (.-value (dom/getElement "num-of-channels"))))))

(let [clicks (listen (dom/getElement "queries") "click")]
  (go (while true
        (<! clicks)
        (do-several-queries))))



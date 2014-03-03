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
          (fn[e] (put! out (.-id element))))
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


(defn play-sound [f vol]
  (set! (.-innerHTML (dom/getElement "sound")) (str "playing freq " f " at volume " vol)))

(defn next-volume [answer vol step]
  (if (= "yes" answer) (- vol step) (+ vol step)))

(defn create-user-input-channel[]
(let [user-input (chan)]
    (let [yes (listen (dom/getElement "yes") "click")
          no (listen (dom/getElement "no") "click")]
      (go (while true
            (let [[v c] (alts! [yes no])]
              (>! user-input (if (= yes c) "yes" "no"))))))))
   

(defn run-audio-test [frequencies volume-start volume-step volume-max volume-min]
  (let [user-input (chan) result-chan (chan)]
    (let [yes (listen (dom/getElement "yes") "click")
          no (listen (dom/getElement "no") "click")]
      (go (while true
            (let [[v c] (alts! [yes no])]
              (>! user-input (if (= yes c) "yes" "no"))))))
    (go
      (>! result-chan (loop [frequencies frequencies results {}]
                        (if (empty? frequencies)
                          results
                          (let [f (first frequencies)
                                result (loop [vol volume-start res {}]
                                         (if (<= volume-min vol volume-max)
                                           (do
                                             (play-sound f vol)
                                             (let [answer (<! user-input)]
                                               (recur (next-volume answer vol volume-step)
                                                      (assoc res vol answer))))
                                           res))]
                            (recur (rest frequencies) (assoc results f result)))))))
    result-chan))

(let [audio-test (listen (dom/getElement "audio-test") "click")]
  (go (while true
        (<! audio-test)
        (println "Audio Test is done: " (<! (run-audio-test (range 3) 75 5 100 50))))))


(def colors ["#FF0000"
             "#00FF00"
             "#0000FF"
             "#00FFFF"
             "#FFFF00"
             "#FF00FF"])

(defn make-cell [canvas x y]
  (let [ctx (-> js/document
              (.getElementById canvas)
              (.getContext "2d"))]
    (go (while true
          (set! (.-fillStyle ctx) (rand-nth colors))
          (.fillRect ctx x y 10 10)
          (<! (timeout (rand-int 1000)))))))

(defn make-scene [canvas rows cols]
  (dotimes [x cols]
    (dotimes [y rows]
      (make-cell canvas (* 10 x) (* 10 y)))))

(let [draw (listen (dom/getElement "draw") "click")]
  (go (while true
        (<! draw)
        (make-scene "canvas" 100 100))))


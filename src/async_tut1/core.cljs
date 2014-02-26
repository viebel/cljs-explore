(ns async-tut1.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [cljs.core.async :refer [put! chan <! timeout]])
  (:import [goog.net Jsonp]
                      [goog Uri])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(def wiki-search-url
    "http://en.wikipedia.org/w/api.php?action=opensearch&format=json&search=")
(println (dom/getElement "query"))

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
        (display-results (second (<! (jsonp (query-url (user-query))))))
        (<! (timeout 1000))
        (display-results ["replacing data" "cool"])
        (<! (timeout 1000))
        (display-results (second (<! (jsonp (query-url "tennis"))))))))

(go (doseq [word ["tennis" "soccer" "bowling" "dsaasdas" "ping pong" "talmud"]]
      (<! (timeout 1000))
      (display-results (second (<! (jsonp (query-url word)))))))

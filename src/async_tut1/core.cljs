(ns async-tut1.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [cljs.core.async :refer [put! chan <!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(def wiki-search-url
    "http://en.wikipedia.org/w/api.php?action=opensearch&format=json&search=")
(println (dom/getElement "query"))

(defn listen [element type]
  (let [out (chan)]
  (events/listen element type 
          (fn[e] (put! out e)))
    out))

(let [clicks (listen (dom/getElement "search") "click")]
  (go (while true
        (println (<! clicks)))))

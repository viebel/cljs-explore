(ns user
  (:require [clojure.string :as string]
            [goog.userAgent.product :as product]
            [cljs.reader :as reader]))

(js/alert product/VERSION)
(js/alert "aaba")
(defn console [s] (.log js/console s))

(defn ^:export foo [] :foo)

(defn foo [])
(foo)

(print ({:a 2} :a))
(print (:a {}))


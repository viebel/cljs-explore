(ns user
  (:require-macros [cljs_explore.macros :as m])
  (:require [clojure.string :as string]
            [goog.userAgent.product :as product]
            [cljs.reader :as reader]))

(js/alert product/VERSION)
(js/alert "aaba")
(defn console [s] (.log js/console s))

(m/deftry moo [a] (print "aaaaa"))

(defn ^:export foo [] :foo)


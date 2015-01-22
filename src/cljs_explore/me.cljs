(ns user
  (:use-macros [purnam.core :only [? ! obj !>]])
  (:require [clojure.string :as string]
            [cemerick.pprng :as rng]
            [cljs.reader :as reader]))


(set! js/aa #js {})
(def a js/aa)
(! a.b.c 5555)
(+ 2 (? a.b.c))
(+ 2  (-> js/aa
        (aget "b")
        (aget "c")))

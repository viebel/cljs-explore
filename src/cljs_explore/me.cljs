(ns user
  (:require [clojure.string :as string]
            [cemerick.pprng :as rng]
            [cljs.reader :as reader]))


(def rng (rng/rng))
(js/alert 
  (rng/int rng))

(ns prj.me
  (:use-macros [purnam.core :only [? ! obj !>]])
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :as string]
            [cljsjs.moment :as moment]
            [cljs.reader :as reader]))



(extend-type object
  ILookup
  (-lookup
    ([this k]
     (-lookup this k nil))
    ([this k not-found]
     (let [k (name k)]
       (if (.hasOwnProperty this k)
         (aget this k)
         not-found)))))


(enable-console-print!)
(print (:aaa #js {:aaa 2}))

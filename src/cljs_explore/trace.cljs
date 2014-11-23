(ns cljs-explore.trace
  (:require-macros
   (cljs-explore.trace :refer [defntraced])))
 
(defntraced foo [x y]
  (+ x y))

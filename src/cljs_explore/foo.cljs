(ns foo
  (:require
   (cljs-explore.trace :include-macros true
              :refer-macros [defntraced])))
 
(enable-console-print!)
(defntraced foo [x y]
  (+ x y))

(foo 2 3)

(ns cljs-explore.trace
  (:require [clairvoyant.core]))
 
(defmacro defntraced [& defn-body]
  `(clairvoyant.core/trace-forms
    (defn ~@defn-body)))


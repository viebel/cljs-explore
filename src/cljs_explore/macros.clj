(ns cljs_explore.macros
  (:require cljs.core
            [cljs.tagged-literals :refer [read-js]]
            cljs.analyzer))

(defmacro deftry [name args & body]
  `(defn ~name ~args
     (try ~@body
          (catch :default e#
            (js/HoneyBadger.notify e#
                                   ~(read-js
                                      {:context
                                       (read-js
                                         {:function
                                          (str (:name (cljs.analyzer/resolve-var &env name)))})}))))))

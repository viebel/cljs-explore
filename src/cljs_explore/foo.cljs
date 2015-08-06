(ns prj.foo)




(def n 100000)

(map inc [1 2])
(range 2)

(zipmap (range 2) (range 2))
(def a (zipmap (range n) (range n)))
(time (clj->js a))

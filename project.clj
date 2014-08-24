(defproject cljs-explore "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [shodan "0.3.0"]
                 [cljs-http "0.1.14"]
                 [com.cemerick/piggieback "0.1.3"]
                 [net.polyc0l0r/konserve "0.1.0"]
                 [com.andrewmcveigh/cljs-time "0.1.4"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [org.clojure/clojurescript "0.0-2280"]]

  :plugins [[lein-cljsbuild "1.0.3"]]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}


  :hooks [leiningen.cljsbuild]

  :source-paths ["src"]

  :cljsbuild {
              :builds {:dev {
                             :source-paths ["src"]
                             :compiler {
                                        :output-to "main.js"
                                        :output-dir "out"
                                        ;:preamble ["license.js"]
                                        :optimizations :simple
                                        }}
                       :production {
                                    :source-paths ["src"]
                                    :compiler {
                                               :output-to "out_opt/main.min.js"
                                               :output-dir "out_opt"
                                               :optimizations :advanced}}
                       :test {
                              :source-paths ["src"]
                              :compiler {
                                         :output-to "out/main.test.js"
                                         :optimizations :simple }}}
              })

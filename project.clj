(defproject cljs-explore "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0-rc1"]
                 [shodan "0.3.0"]
                 [cljsjs/moment "2.9.0-0"]
                 [cljs-http "0.1.14"]
                 [im.chit/purnam "0.5.1"]
                 [com.cemerick/piggieback "0.1.3"]
                 [net.polyc0l0r/konserve "0.1.0"]
                 [com.andrewmcveigh/cljs-time "0.1.4"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.clojure/clojurescript "0.0-3308"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild]

  :source-paths ["src"]

  :cljsbuild {
              :builds {
                       :prod {
                              :source-paths ["src"]
                              :compiler {
                                         :output-to "main.min.s"
                                         :output-dir "advanced"
                                         :static-fns true 
                                         :optimizations :advanced
                                         :preamble ["license.js"]}}
                       :dev {
                             :source-paths ["src"]
                             :compiler {
                                        :output-to "main.js"
                                        :output-dir "out"
                                        :static-fns true 
                                        :preamble ["license.js"]
                                        :optimizations :simple}} }})

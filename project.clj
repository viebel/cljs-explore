(defproject cljs-explore "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [shodan "0.3.0"]
                 [spellhouse/clairvoyant "0.0-48-gf5e59d3"]
                 [cljs-http "0.1.14"]
                 [com.cemerick/piggieback "0.1.3"]
                 [net.polyc0l0r/konserve "0.1.0"]
                 [com.andrewmcveigh/cljs-time "0.1.4"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.clojure/clojurescript "0.0-2280"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild]

  :source-paths ["src"]

  :cljsbuild {
              :builds {
                       :dev {
                             :source-paths ["src"]
                             :compiler {
                                        :output-to "main.js"
                                        :output-dir "out"
                                        :static-fns true 
                                        :preamble ["license.js"]
                                        :optimizations :simple}} }})

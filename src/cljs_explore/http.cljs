(ns http2
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<! chan]]))


(js/alert "1")

(go (let [response (<! (http/get "https://api.github.com/users" {:with-credentials? false}))]
      (prn response)
      (prn (:status response))
      (prn (map :login (:body response)))))

;; POSTing automatically sets Content-Type header and serializes
(http/post "http://example.com" {:edn-params {:foo :bar}})

;; JSON is auto-converted via `cljs.core/clj->js`
(http/post "http://example.com" {:json-params {:foo :bar}})

;; Form parameters in a POST request (simple)
(http/post "http://example.com" {:form-params {:key1 "value1" :key2 "value2"}})

;; Form parameters in a POST request (array of values)
(http/post "http://example.com" {:form-params {:key1 [1 2 3] :key2 "value2"}})

;; HTTP Basic Authentication
(http/get
  "http://example.com"
  {:basic-auth {:username "hello" :password "world"}})

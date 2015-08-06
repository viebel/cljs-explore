(ns prj.http2
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<! chan]]))


(js/alert "1")

(go (let [response (<! (http/get "https://api.github.com/users" {:with-credentials? false}))]
      (prn response)
      (prn (:status response))
      (prn (map :login (:body response)))))



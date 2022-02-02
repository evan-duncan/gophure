(ns gophure.server-test
  (:require [gophure.server :as server]
            [gophure.client :as client]
            [clojure.test :as t]
            [manifold.stream :as s]))

(defn echo [s _] (s/connect s s))

(defn handle-request [req]
  (let [port   65432
        server (server/listen echo port)
        client @(client/create "localhost" port)
        _msg   @(s/put! client req)
        res    @(s/take! client)]
    (.close server)
    res))

(t/deftest handles-gopher-request
  (t/testing "parses request properly"
    (t/is (= "/Reference" (handle-request "/Reference")))))

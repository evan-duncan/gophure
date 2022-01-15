(ns gophure.server
  (:require [clojure.edn :as edn]
            [aleph.tcp :as tcp]
            [gloss.core :as gloss]
            [gloss.io :as io]
            [manifold.stream :as s]
            [manifold.deferred :as d]))

(def ^:*private* protocol
  (gloss/compile-frame
   (gloss/delimited-frame
    [(str \return \newline) (str \tab)]
    (gloss/string :utf-8))
   pr-str
   edn/read-string))

(defn- wrap-duplex-stream [protocol in]
  (let [out s/stream]
    (s/connect (s/map #(io/encode protocol %) out) in)
    (s/splice out (io/decode-stream in protocol))))

(defn start [handler port]
  (tcp/start-server
   (fn [sink info] (handler (wrap-duplex-stream protocol sink) info))
   {:port port}))

;; Test server implementation

(defn fast-echo-handler [f]
  (fn [s _info] (s/connect (s/map f s) s)))

(defn client
  [host port]
  (d/chain (tcp/client {:host host :port port})
           #(wrap-duplex-stream protocol %)))

(def s
  (start
   (fast-echo-handler inc)
   10000))

(def c @(client "localhost" 10000))

(ns gophure.tcp
  (:require [aleph.tcp :as lf]
            [clojure.edn :as edn]
            [gloss.core :as gloss]
            [gloss.io :as io]
            [manifold.stream :as s]
            [manifold.deferred :as d]))

(def gopher-codec
  (gloss/string :utf-8 :delimiters ["\r\n"]))

(defn- wrap-duplex-stream
  "
  Takes a raw TCP duplex stream which represents bidrectional communication
  via a single stream. Messages from the remote endpoint can be consumed
  via `take!`, and messages can be sent to the remote endpoint view `put!`.
  It returns a duplex stream which will take and emit arbitrary Clojure data,
  via the protocol.
  "
  [protocol s]
  (let [out (s/stream)]
    (s/connect (s/map #(io/encode protocol %) out) s)
    (s/splice out (io/decode-stream s protocol))))

(defn client [host port]
  (d/chain (lf/client {:host host :port port})
           #(wrap-duplex-stream gopher-codec %)))

(defn listen
  "Start a TCP server handling Gopher protocol requests"
  [handler port]
  (lf/start-server
   (fn [s info] (handler (wrap-duplex-stream gopher-codec s) info))
   {:port port}))

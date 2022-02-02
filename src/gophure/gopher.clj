(ns gophure.gopher
  (:require [gloss.core :as gloss]
            [gloss.io :as io]
            [manifold.stream :as s]))

(def ^:const crlf "\r\n")

(def codec
  (gloss/string :utf-8 :delimiters [crlf]))

(defn wrap-duplex-stream
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

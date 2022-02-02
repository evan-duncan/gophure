(ns gophure.client
  (:require [manifold.deferred :as d]
            [aleph.tcp :as tcp]
            [gophure.gopher :as gopher]))

(defn create [host port]
  (d/chain (tcp/client {:host host :port port})
           #(gopher/wrap-duplex-stream gopher/codec %)))

(ns gophure.server
  (:require [aleph.tcp :as lf]
            [gophure.gopher :as gopher]))

(defn listen
  "Start a TCP server handling Gopher protocol requests"
  [handler port]
  (lf/start-server
   (fn [s info] (handler (gopher/wrap-duplex-stream gopher/codec s) info))
   {:port port}))

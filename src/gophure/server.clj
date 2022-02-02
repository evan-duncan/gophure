(ns gophure.server
  (:require [aleph.tcp :as tcp]
            [gophure.gopher :as gopher]))

(defn listen
  "Start a TCP server handling Gopher protocol requests"
  [handler port]
  (tcp/start-server
   (fn [s info] (handler (gopher/wrap-duplex-stream s) info))
   {:port port}))

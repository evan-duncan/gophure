(defproject gophure "0.1.0-SNAPSHOT"
  :description "A gopher server"
  :url "http://example.com/FIXME"
  :license {:name "AGPL-#.0-or-later"
            :url "https://www.gnu.org/licenses/agpl-3.0.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [aleph "0.4.6"]
                 [gloss "0.2.6"]
                 [manifold "0.2.3"]]
  :main ^:skip-aot gophure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

(defproject vagabond "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                  [org.clojure/clojurescript "0.0-2202"]
                  [org.clojure/core.async "0.1.278.0-76b25b-alpha"]
                  [om "0.5.3"]
                  [liberator "0.11.0"]
                  [compojure "1.1.6"]
                  [ring/ring-core "1.2.1"]
                  [ring/ring-json "0.2.0"]
                  [ring/ring-jetty-adapter "1.1.0"]
                  [org.xerial/sqlite-jdbc "3.7.2"]
                  [korma "0.3.0-RC6"]
                  [digest "1.4.3"]]

  :plugins [[lein-ring "0.8.10"]
            [lein-cljsbuild "1.0.3"]]

  :source-paths ["src/server"]

  :cljsbuild {
    :builds [{:id "js/vagabond"
              :source-paths ["src/client"]
              :compiler {
                :output-to "resources/public/js/main.js"
                :output-dir "resources/public/js"
                :optimizations :none
                :source-map true}}]}

  :ring {
    :handler vagabond.core/handler
    :auto-refresh? true
    :auto-reload? true
  }
  :main vagabond.core)  

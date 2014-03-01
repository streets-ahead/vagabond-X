(defproject vagabond "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [liberator "0.11.0"]
                 [compojure "1.1.6"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [cheshire "5.3.1"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [korma "0.3.0-RC6"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler vagabond.core/app}
  :main vagabond.core)  

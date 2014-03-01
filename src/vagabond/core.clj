(ns vagabond.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.adapter.jetty :refer [run-jetty]]      
            [compojure.core :refer [defroutes ANY GET]]
            [cheshire.core :refer (generate-string)]
            [vagabond.blog_service :refer (all-posts get-post posts-by-author)]))

(defn create-handler [render-html]
  (fn [ctx] 
    (let [data (:req-data ctx)]
      (case (get-in ctx [:representation :media-type])
        "application/json" (generate-string data)
        "text/html" (render-html data)))))

(defn create-exists [get-data]
  (fn [ctx] 
    (let [data (get-data)]
      (if (empty? data)
        false
        {:req-data data}))))

(defn post-handler [ctx] (println "calling post"))

(defn simple-template [data] (str "<h1>" (:myObj data) "</h1>"))

(def route-defaults {
  :available-media-types ["text/html" "application/json"]
  :handle-ok (create-handler simple-template)})

(defresource posts route-defaults
         :allowed-methods [:post :get]
         :exists? (create-exists (all-posts))
         :post! post-handler)

(defresource single-post [slug] route-defaults 
         :allowed-methods [:put :get]
         :exists? (create-exists (get-post slug)))

(defresource by-author [author] route-defaults
         :allowed-methods [:get]
         :exists? (create-exists (posts-by-author author)))

(defroutes app
  (ANY "/posts" [] posts)
  (ANY "/posts/:slug" [slug] (single-post slug))
  (ANY "/posts/author/:author" [author] (by-author author)))
  
(def handler (-> app (wrap-params)))  
  
(defn -main [] (run-jetty #'handler {:port 3000}))

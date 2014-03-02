(ns vagabond.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.adapter.jetty :refer [run-jetty]]      
            [compojure.core :refer [defroutes ANY GET POST]]
            [vagabond.blog_service :refer (all-posts get-post posts-by-author)]))

(defn create-exists [get-data]
  (fn [ctx] 
    (let [data (get-data)]
      [(not (empty? data)) {:req-data data}])))

(defn post-handler [ctx] (println "calling post"))

(def route-defaults {
  :available-media-types ["text/html" "application/json"]
  :handle-ok :req-data})

(defresource posts-resource route-defaults
         :allowed-methods [:get]
         :exists? (create-exists (all-posts))
         :post! post-handler)

(defresource create-post-resource route-defaults
         :allowed-methods [:post]
         :exists? (create-exists (all-posts))
         :post! post-handler)

(defresource single-post [slug] route-defaults 
         :allowed-methods [:put :get :delete]
         :exists? (create-exists (get-post slug)))

(defresource by-author [author] route-defaults
         :allowed-methods [:get]
         :exists? (create-exists (posts-by-author author)))

(defroutes app
  (GET "/posts" [] posts-resource)
  (POST "/posts/:slug" [slug] (create-post-resource slug))
  (ANY "/posts/:slug" [slug] (single-post slug))
  (ANY "/posts/author/:author" [author] (by-author author)))
  
(def handler (-> app (wrap-params)))  
  
(defn -main [] (run-jetty #'handler {:port 3000}))

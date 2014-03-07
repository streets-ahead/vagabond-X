(ns vagabond.core
  (:require [liberator.core :refer [resource defresource]]
    [liberator.representation :refer [ring-response]]
    [ring.middleware.params :refer [wrap-params]]
    [ring.adapter.jetty :refer [run-jetty]]      
    [compojure.core :refer [defroutes ANY GET POST]]
    [vagabond.blog_service :as blog_service]
    [ring.middleware.json :refer [wrap-json-body]]))

(defn create-exists [get-data]
  (fn [ctx] 
    (let [data (get-data)]
      [(not (empty? data)) {:req-data data}])))

(def route-defaults {
  :available-media-types ["text/html" "application/json"]
  :handle-ok :req-data})

(defresource posts-resource route-defaults
 :allowed-methods [:get]
 :exists? (create-exists #(blog_service/all-posts)))

(defresource create-post-resource route-defaults
  :allowed-methods [:post]
  :authorized? (fn [ctx] {:user {:id 1}})
  :post! (fn [ctx] (when-not (blog_service/create-post (get-in ctx [:request :body])) {:conflict true}))
  :handle-created 
    (fn [{conflict :conflict data :data}]
      (if conflict
        (ring-response {:status 409 :body "duplicate post!"})
        data)))
  
(defresource single-post [slug] route-defaults 
  :allowed-methods [:post :get :delete]
  :exists? (create-exists #(blog_service/get-post slug))
  :post-to-missing? false)

(defresource by-author [author] route-defaults
 :allowed-methods [:get]
 :exists? (create-exists #(blog_service/posts-by-author author)))

(defroutes app
  (POST "/posts" [] create-post-resource)
  (ANY "/posts" [] posts-resource)
  (ANY "/posts/:slug" [slug] (single-post slug))
  (ANY "/posts/author/:author" [author] (by-author author)))

(def handler (wrap-json-body app {:keywords? true}))  

(defn -main [] (run-jetty #'handler {:port 3000}))

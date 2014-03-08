(ns vagabond.core
  (:require [liberator.core :refer [resource defresource]]
    [liberator.representation :refer [ring-response]]
    [ring.middleware.params :refer [wrap-params]]
    [ring.adapter.jetty :refer [run-jetty]]      
    [compojure.core :refer [defroutes ANY GET POST]]
    [vagabond.blog_service :as blog_service]
    [ring.middleware.json :refer [wrap-json-body]]))

(defn- create-exists [get-data]
  (fn [ctx] 
    (let [data (get-data)]
      [(not (empty? data)) {::req-data data}])))

(defn- handle-conflict [{conflict ::conflict}] 
  (when conflict
    (ring-response {:status 409 :body "duplicate title!"})))

(defn- check-conflict [{conflict ::conflict}] (not conflict))

(defn- create-modifier [action]
  (fn [ctx] 
    (let [body (get-in ctx [:request :body])
          post (assoc body :author (get-in ctx [::user :id]))] 
      (when-not (action post) {::conflict true}))))
  
(def route-defaults {
  :available-media-types ["text/html" "application/json"]
  :handle-ok ::req-data
  :respond-with-entity? check-conflict
  :handle-no-content handle-conflict})

(defresource posts-resource route-defaults
 :allowed-methods [:get]
 :exists? (create-exists #(blog_service/all-posts)))

; hand post to / to create new posts
(defresource create-post-resource route-defaults
  :allowed-methods [:post]
  :authorized? (fn [ctx] {::user {:id 1}})
  :new? check-conflict
  :post! (create-modifier #(blog_service/create-post %)))
  
; handler for all things /[slug]
(defresource single-post [slug] route-defaults 
  :allowed-methods [:put :get :delete]
  :exists? (create-exists #(blog_service/get-post slug))
  :can-put-to-missing? false
  :new? false
  :put! (create-modifier #(blog_service/update-post slug %)))

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




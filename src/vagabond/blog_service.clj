(ns vagabond.blog_service
  (:require [korma.db :refer (defdb)])
  (:use korma.core))

(defdb db {:classname   "org.sqlite.JDBC"
      :subprotocol "sqlite"
      :subname     "resources/sts-blog.sqlite"})

(declare posts users)
(defentity users (has-many posts {:fk :author}))
(defentity posts (belongs-to users {:fk :author}))

(def base (-> (select* posts) 
              (fields :title :body :publish_date :slug) 
              (with users (fields [:name :author_name] [:username :author]))))

(defn query-posts 
  ([] (-> base (select)))
  ([opts] (-> base (where opts) (select))))

(defn all-posts [] #(query-posts))

(defn get-post [slug] #(first (query-posts {:slug slug})))

(defn posts-by-author [author] (do
                                 (println author)
                                 #(query-posts {:users.username author})))

(ns vagabond.blog_service
  (:require [korma.db :refer (defdb)]
            [clojure.string])
  (:use korma.core))

(defdb db {:classname   "org.sqlite.JDBC"
      :subprotocol "sqlite"
      :subname     "resources/sts-blog.sqlite"})

(def leading-trailing #"(^-)|(-$)")
(def non-alpha #"[^\p{L}\p{Nd}]")
(def multiple-dash #"(-+)")

(defn- create-slug [{title :title}]
  (-> title clojure.string/trim 
    clojure.string/lower-case
    (clojure.string/replace non-alpha "-")
    (clojure.string/replace leading-trailing "")
    (clojure.string/replace multiple-dash "-")))

(declare posts users)
(defentity users (has-many posts {:fk :author}))
(defentity posts (belongs-to users {:fk :author}))

(def base (-> (select* posts) 
              (fields :title :body :publish_date :slug) 
              (with users (fields [:name :author_name] [:username :author]))))

(defn query-posts 
  ([] (-> base (select)))
  ([opts] (-> base (where opts) (select))))

(defn all-posts [] (query-posts))

(defn get-post [slug] (first (query-posts {:slug slug})))

(defn posts-by-author [author] (query-posts {:users.username author}))

(defn create-post [obj] 
    (let [slug (create-slug obj)
        post (assoc obj :slug slug)]
    (when-not (get-post slug)
      (insert posts (values post)))))
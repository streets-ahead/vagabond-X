(ns vagabond.blog_service
  (:require [korma.db :refer (defdb)]
            [clojure.string]
            [digest])
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

(def base-posts (-> (select* posts) 
  (fields :title :body :publish_date :slug) 
  (with users (fields [:name :author_name] [:username :author]))))

(defn query-posts 
  ([] (-> base-posts (select)))
  ([opts] (-> base-posts (where opts) (select))))

(defn all-posts [] (query-posts))

(defn get-post [slug] (first (query-posts {:slug slug})))

(defn posts-by-author [author] (query-posts {:users.username author}))

(defn- conflicting? [new old]
  (if (not= new old)
    (not (nil? (get-post new)))
    false))

(defn- clean-post [post]
  (select-keys post [:title :body :slug :author]))

(defn update-post [old-slug obj] 
  (let [slug (create-slug obj)
          post (clean-post (assoc obj :slug slug))]
    (when-not (conflicting? slug old-slug)
      (update posts (set-fields post) (where {:slug old-slug})))))

(defn create-post [obj] 
  (let [slug (create-slug obj)
        post (clean-post (assoc obj :slug slug))]
    (when-not (get-post slug)
      (insert posts (values post)))))

(def base-user (-> (select* users) (fields :username :id :name)))

(defn- get-salt [username]
  (:salt (first (select users (fields :salt) (where {:username username})))))

(defn auth-user [username password]
  (let [salt (get-salt username)
        hashpass (digest/sha-256 (str password salt))]
    (first (-> base-user (where {:username username :password hashpass}) (select)))))














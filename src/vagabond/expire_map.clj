(ns vagabond.expire-map)

(deftype ExpireMap [contents]
  clojure.lang.IPersistentMap
  (assoc [_ k v]
    (Person. (.assoc contents k v)))
  (assocEx [_ k v]
    (Person. (.assocEx contents k v)))
  (without [_ k]
    (Person. (.without contents k)))
 
  java.lang.Iterable
  (iterator [this]
    (.iterator (augment-contents contents)))
 
  clojure.lang.Associative
  (containsKey [_ k]
    (.containsKey (augment-contents contents) k))
  (entryAt [_ k]
    (.entryAt (augment-contents contents) k))
 
  clojure.lang.IPersistentCollection
  (count [_]
    (.count (augment-contents contents)))
  (cons [_ o]
    (Person. (.cons contents o)))
  (empty [_]
    (.empty (augment-contents contents)))
  (equiv [_ o]
    (and (isa? (class o) Person)
         (.equiv (augment-contents contents) (.(augment-contents contents) o))))
 
  clojure.lang.Seqable
  (seq [_]
    (.seq (augment-contents contents)))
 
  clojure.lang.ILookup
  (valAt [_ k]
    (.valAt (augment-contents contents) k))
  (valAt [_ k not-found]
    (.valAt (augment-contents contents) k not-found)))
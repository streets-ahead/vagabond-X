(ns vagabond.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def root (. js/document (querySelector "#root")))

(def app-state
  (atom
    {:text "hi"}))

(om/root
  (fn [app owner] (
  	om/component (dom/h2 nil (:text app))))
	app-state
	{:target root})
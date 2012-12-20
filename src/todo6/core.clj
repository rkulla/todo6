(ns todo6.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))


(defn- parse-todo-file [filename]
  (def todo 
    (let [todo-seq (string/split-lines (slurp filename))] 
      (into (sorted-map) (zipmap (range 1 (inc (count todo-seq))) todo-seq))))
  (pprint/pprint todo))


(defn -main [& args]
  (def home (System/getenv "HOME"))
  (parse-todo-file (str home "/todo6.txt")))

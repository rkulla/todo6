(ns todo6.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))


(defn- parse-todo-file [filename]
  (def todo 
    (let [todo-seq (string/split-lines (slurp filename))] 
      (into (sorted-map) (zipmap [1 2 3 4 5 6] todo-seq))))
  (pprint/pprint todo))


(defn -main [& args]
  (def home (System/getenv "HOME"))
  (parse-todo-file (str home "/todo6.txt")))

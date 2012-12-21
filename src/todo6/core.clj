(ns todo6.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))

(def todo-file (str (System/getenv "HOME") "/todo6.txt"))

(def max-tasks 6)

(defn- parse-todo-file []
  (def todo 
    (let [tasks (string/split-lines (slurp todo-file))] 
      (into (sorted-map) (zipmap (range 1 (inc (count tasks))) tasks))))
  (when (> (count todo) max-tasks) 
    (println "Too many tasks." "Trim down" todo-file "to" max-tasks "\n")
    (System/exit 0))
  (pprint/pprint todo))


(defn -main [& args]
  (parse-todo-file))

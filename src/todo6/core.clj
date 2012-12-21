(ns todo6.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))

(def todo-file (str (System/getenv "HOME") "/todo6.txt"))
(def max-tasks 6)
(def abort-msg-format 
  (format "Too many tasks. Trim down %s to %d lines\n" todo-file max-tasks))

(defn- validate-todo-contents [tasks]
  (when (> (count tasks) max-tasks) 
    (println abort-msg-format)
    (System/exit 0)))

(defn- parse-todo-file []
  (def tasks 
    (let [lines (string/split-lines
      (try
        (slurp todo-file)
        (catch Exception e
          (printf "Error: ") (.getMessage e))))]
            (into (sorted-map) (zipmap (range 1 (inc (count lines))) lines))))
  (validate-todo-contents tasks)
  (pprint/pprint tasks))

(defn -main [& args]
  (parse-todo-file))

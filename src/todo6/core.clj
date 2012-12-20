(ns todo6.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))


(defn- parse-todo-file [filename]
  (def todo (string/split-lines (slurp filename)))
  (pprint/pprint todo))


(defn -main [& args]
  (def home (System/getenv "HOME"))
  (parse-todo-file (str home "/todo6.txt")))

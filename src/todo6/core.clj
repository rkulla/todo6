(ns todo6.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))

(def todo-file (str (System/getenv "HOME") "/todo6.txt"))
(def max-tasks 6)
(def abort-message
  (format "Too many tasks. Trim down %s to %d lines\n" todo-file max-tasks))
(def help-message "Commands: ?/help, ls/list, quit/exit")
(def prompt "> ")

(defn- exit []
  (System/exit 0))

(defn- validate-todo-contents [tasks]
  "Validates the contents of the todo file"
  (when (> (count tasks) max-tasks) 
    (println abort-message)
    (exit)))

(defn- prompt-user-input []
  "Show a prompt and user input"
  (print prompt)
  (flush)
  (read-line))

(defn- is-in [s & args]
  "Checks if a string is in the argument list"
  (if (some (partial = s) args)
    true false))

(defn- get-commands [tasks]
  "Repeatedly get user commands"
  (println help-message) 
  (loop [input  
    (prompt-user-input)]
      (cond
        (is-in input "ls" "list") (pprint/pprint tasks)
        (is-in input "exit" "quit") (exit)
        (is-in input "?" "help") (println help-message)
        :else (println "No such command." help-message))
        (recur (prompt-user-input))))

(defn- parse-todo-file []
  "Parses out the tasks from the todo file"
  (def tasks 
    (let [lines (string/split-lines
      (try
        (slurp todo-file)
        (catch Exception e
          (print "Error: ") (.getMessage e))))]
            (into (sorted-map) (zipmap (range 1 (inc (count lines))) lines))))
  (validate-todo-contents tasks)
  (get-commands tasks))

(defn -main [& args]
  (parse-todo-file))

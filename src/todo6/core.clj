(ns todo6.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))

;; Configuration
(def todo-file (str (System/getenv "HOME") "/todo6.txt"))
(def max-tasks 6)
(def abort-message
  (format "Too many tasks. Trim down %s to %d lines\n" todo-file max-tasks))
(def help-message "Commands: ?/help, ls/list, quit/exit, 1-6, done 1-6")
(def prompt "> ")

(defn- exit []
  "Shutdowns the application"
  (System/exit 0))

(defn- validate-todo-contents [tasks]
  "Validates the contents of the todo file"
  (when (> (count @tasks) max-tasks) 
    (println abort-message)
    (exit)))

(defn- prompt-user-input []
  "Show a prompt and user input"
  (print prompt)
  (flush)
  (read-line))

(defn- is-in [s & args]
  "Returns boolean if a string is in the argument list"
  (if (some (partial = s) args)
    true false))

(defn- get-first-int [s]
  "Returns integer value of first number in string"
  (Integer. (first (re-seq #"\d" s))))

(defn- get-commands [tasks]
  "Repeatedly get user commands"
  (println help-message) 
  (loop [input  
    (prompt-user-input)]
      (cond
        (is-in input "ls" "list") (pprint/pprint @tasks)
        (apply is-in input (map str (range 1 (inc max-tasks))))
          (println (get @tasks (Integer. input)))
        (is-in input "exit" "quit") (exit)
        (is-in input "?" "help") (println help-message)
        (.startsWith input "done") 
          (swap! tasks assoc-in [(get-first-int input) :status] "done")
        :else (println "No such command." help-message))
        (recur (prompt-user-input))))

(defn- parse-todo-file []
  "Parses out the tasks from the todo file"
  (def tasks 
    (atom
        (let [lines (string/split-lines
        (try
            (slurp todo-file)
            (catch Exception e
            (print "Error: ") (.getMessage e))))]
                (into (sorted-map) (zipmap (range 1 (inc (count lines)))
                (map-indexed 
                    (fn [idx itm] idx {:name itm, :status "pending"}) lines))))))
  (validate-todo-contents tasks)
  (get-commands tasks))

(defn -main [& args]
  (parse-todo-file))

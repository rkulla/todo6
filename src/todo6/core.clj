(ns todo6.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]))

;; Configuration
(def todo-file (str (System/getenv "HOME") "/todo6.txt"))
(def max-tasks 6)
(def abort-message
  (format "Too many tasks. Trim down %s to %d lines\n" todo-file max-tasks))
(def help-message "?/help, ls/list/todo, quit/exit, 1-6, done 1-6, undone 1-6")
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

(defn- change-task-val [tasks task-num prop v]
  "Lets you change the value in our nested todo data structure"
  (swap! tasks assoc-in [task-num prop] v))

(defn- show-task [tasks i]
  "Print out a single task in a nice format"
  (printf "%d (%s) %s\n" i (get-in @tasks [i :status]) 
                           (get-in @tasks [i :name])))

(defn- show-all-tasks [tasks]
  "Print out all the tasks in a nice format"
  (doseq [[i] @tasks] (show-task tasks i)))

(defn- get-commands [tasks]
  "Repeatedly get user commands"
  (println help-message) 
  (loop [input  
    (prompt-user-input)]
      (cond
        (is-in input "ls" "list" "todo") (show-all-tasks tasks)
        (apply is-in input (map str (range 1 (inc max-tasks))))
          (show-task tasks (Integer. input))
        (is-in input "exit" "quit") (exit)
        (is-in input "?" "help") (println help-message)
        (and (.startsWith input "done") (> (count input) 4))
          (change-task-val tasks (get-first-int input) :status "done")
        (and (.startsWith input "undone") (> (count input) 6))
          (change-task-val tasks (get-first-int input) :status "todo")
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
                    (fn [idx itm] idx {:name itm, :status "todo"}) lines))))))
  (validate-todo-contents tasks)
  (get-commands tasks))

(defn -main [& args]
  (parse-todo-file))

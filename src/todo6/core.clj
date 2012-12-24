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

(defn- get-file-contents [filename]
  "Reads in the contents of a file and prints any errors"
  (try
    (slurp filename)
    (catch Exception e
      (print "Error: ") (.getMessage e))))

(defn- format-lines [lines]
  "Puts the todo tasks in a certain format"
  (map-indexed 
   (fn [idx itm] idx {:name itm, :status "todo"}) lines))

(def tasks 
  (atom
   (let [lines (string/split-lines (get-file-contents todo-file))]
     (into (sorted-map) (zipmap (range 1 (inc (count lines)))
                                (format-lines lines))))))

(def get-task-range []
  "Return a seq of task numbers from 1 to max-tasks"
  (range 1 (inc max-tasks))

(defn- exit []
  "Shutdowns the application"
  (System/exit 0))

(defn- validate-todo-contents []
  "Validates the contents of the todo file"
  (when (> (count @tasks) max-tasks) 
    (println abort-message)
    (exit)))

(defn- prompt-user-input []
  "Show a prompt and user input"
  (print prompt)
  (flush)
  (read-line))

(defn- is-in [val & args]
  "Returns boolean if a value is contained in the argument list"
  (if (some (partial = val) args)
    true 
    false))

(defn- get-first-int [s]
  "Returns integer value of first number in string"
  (Integer. (first (re-seq #"\d" s))))

(defn- change-task-val [task-num prop v]
  "Lets you change the value in our nested todo data structure"
  (if (is-in task-num (get-task-range))
    (swap! tasks assoc-in [task-num prop] v)))

(defn- show-task [i]
  "Print out a single task in a nice format"
  (printf "%d (%s) %s\n" i (get-in @tasks [i :status]) 
          (get-in @tasks [i :name])))

(defn- show-all-tasks []
  "Print out all the tasks in a nice format"
  (doseq [[i] @tasks] (show-task i)))

(defn- get-commands []
  "Repeatedly get user commands"
  (loop [input  
         (prompt-user-input)]
    (cond
     (is-in input "ls" "list" "todo") (show-all-tasks)
     (apply is-in input (map str (get-task-range)))
     (show-task (Integer. input))
     (is-in input "exit" "quit") (exit)
     (is-in input "?" "help") (println help-message)
     (and (.startsWith input "done") (> (count input) 5))
     (change-task-val (get-first-int input) :status "done")
     (and (.startsWith input "undone") (> (count input) 7))
     (change-task-val (get-first-int input) :status "todo")
     :else (println "No such command." help-message))
    (recur (prompt-user-input))))

(defn -main [& args]
  (validate-todo-contents)
  (println help-message) 
  (newline)
  (show-all-tasks)
  (get-commands))

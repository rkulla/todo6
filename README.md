# todo6

Todo6 is a app that I made because I wanted a *main* to-do
list app that forces me to only add 6 things at a time (per day). I also
made it to help me learn Clojure.

It's not necessarily the only list I'll make in a given day. For example,
my main todo list in todo6 might contain "shopping" but actual shopping 
list (bananas, eggs, OJ) would be in a separate list on my smart phone.

## Usage

Create a file in your home directory called todo6.txt and put one task
per line. (create no more than 6 lines!)

Then, assuming you got this todo6 as a jar file, run the todo6 app with:

    $ java -jar todo6.jar

If you just have the source code and have leiningen:

    $ lein trampoline run

You'll then see a '>' prompt where you can type commands:  To show all
the pending tasks, type:

    > ls

You'll notice the tasks are assigned numbers. You can use the numbers to
referene the tasks. For example, to see task 2:

    > 2

To mark task 2 as completed:

    > done 2

You can resume a task by marking it undone:

    > undone 2

## License

Copyright (C) 2012 Ryan Kulla

Distributed under the Eclipse Public License, the same as Clojure.

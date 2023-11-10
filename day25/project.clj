(defproject day25 "0.1.0-SNAPSHOT"
  :description "Four-Dimensional Adventure"
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :profiles {:dev {:dependencies [[speclj "3.4.3"]]}}
  :plugins [[speclj "3.4.3"]]
  :test-paths ["spec"]
  :repl-options {:init-ns day25.core})

(defproject day03 "0.1.0-SNAPSHOT"
  :description "No Matter How You Slice It"
  :main day03.core
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])

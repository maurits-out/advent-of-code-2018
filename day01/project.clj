(defproject day01 "0.1.0-SNAPSHOT"
  :description "Day 1"
  :main day01.core
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])

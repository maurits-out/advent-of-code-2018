(defproject day15 "0.1.0-SNAPSHOT"
  :description "Beverage Bandits"
  :main day15.core
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [data.deque "0.1.0"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])

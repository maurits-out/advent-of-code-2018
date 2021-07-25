(defproject day06 "0.1.0-SNAPSHOT"
  :description "Chronal Coordinates"
  :main day06.core
  :dependencies [[org.clojure/clojure "1.10.3"] [criterium "0.4.6"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])

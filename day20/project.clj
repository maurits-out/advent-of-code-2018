(defproject day20 "0.1.0-SNAPSHOT"
  :description "A Regular Map"
  :main day20.core
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]
                                  [data.deque "0.1.0"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])

(ns day25.core-test
  (:require [speclj.core :refer :all]
            [day25.core :refer :all]
            [clojure.java.io :as io]))

(def input-file (io/resource "input.txt"))

(describe "Day 25"

  (it "Should solve puzzle"
    (let [input (slurp input-file)]
      (should= 314 (solve input)))))

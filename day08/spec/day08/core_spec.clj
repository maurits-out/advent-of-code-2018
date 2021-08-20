(ns day08.core-spec
  (:require [speclj.core :refer :all]
            [day08.core :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(def input-file (io/resource "input.txt"))
(def input (string/trim-newline (slurp input-file)))

(describe "Day 8"

  (context "Parsing the input"
    (it "can parse the input to return a sequence of numbers."
      (should= [1] (parse-input "1"))
      (should= [1 2] (parse-input "1 2"))))

  (context "Calculating the sum of the metadata entries"
    (it "should calculate the sum of the metadata entries."
      (should= ['() 138] (sum-of-node [2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2]))))

  (context "Solutions"
    (it "should solve part 1."
      (should= 46829 (part1 input)))
    (it "should solve part 2."
      (should= 37450 (part2 input)))))

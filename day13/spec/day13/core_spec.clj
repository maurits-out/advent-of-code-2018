(ns day13.core-spec
  (:require [speclj.core :refer :all]
            [day13.core :refer :all]
            [clojure.java.io :as io]))

(def input (slurp (io/resource "input.txt")))

(describe "Day 13"
          (context "Solutions"
                   (it "should solve part 1."
                       (should= "41,22" (part1 input)))
                   (it "should solve part 2."
                       (should= "84,90" (part2 input)))
                   ))
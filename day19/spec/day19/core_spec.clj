(ns day19.core-spec
  (:require [speclj.core :refer :all]
            [day19.core :refer :all]
            [clojure.java.io :as io]))

(def example "#ip 0\nseti 5 0 1\nseti 6 0 2\naddi 0 1 0\naddr 1 2 3\nsetr 1 0 0\nseti 8 0 4\nseti 9 0 5\n")
(def input-file (io/resource "input.txt"))

(describe "Day 19"

          (context "Parsing"
                   (it "should parse the input."
                       (should= {:ip-reg  0,
                                 :program [["seti" 5 0 1] ["seti" 6 0 2] ["addi" 0 1 0]
                                           ["addr" 1 2 3] ["setr" 1 0 0] ["seti" 8 0 4]
                                           ["seti" 9 0 5]]} (parse-input example))))

          (context "Evaluate"
                   (it "should evaluate an instruction."
                       (should= [0 5 0 0 0 0] (evaluate [0 0 0 0 0 0] ["seti" 5 0 1]))))

          (context "Example"
                   (it "should execute the example."
                       (should= 6 (execute-part-1 (parse-input example)))))

          (context "Part 1"
                   (it "should execute the puzzle input."
                       (should= 2106 (execute-part-1 (parse-input (slurp input-file))))))

          (context "Part 2"
                   (it "should execute the puzzle input."
                       (should= 23021280 (execute-part-2 (parse-input (slurp input-file)))))))

(ns day19.core-spec
  (:require [speclj.core :refer :all]
            [day19.core :refer :all]))

(def example "#ip 0\nseti 5 0 1\nseti 6 0 2\naddi 0 1 0\naddr 1 2 3\nsetr 1 0 0\nseti 8 0 4\nseti 9 0 5\n")

(describe "Day 19"
          (context "Parsing"
                   (it "should parse the input."
                       (should= {:ip-reg 0,
                                 :program ["seti 5 0 1" "seti 6 0 2" "addi 0 1 0"
                                           "addr 1 2 3" "setr 1 0 0" "seti 8 0 4"
                                           "seti 9 0 5"]} (parse-input example)))))

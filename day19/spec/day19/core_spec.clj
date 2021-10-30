(ns day19.core-spec
  (:require [speclj.core :refer :all]
            [day19.core :refer :all]))

(def example "#ip 0\nseti 5 0 1\nseti 6 0 2\naddi 0 1 0\naddr 1 2 3\nsetr 1 0 0\nseti 8 0 4\nseti 9 0 5\n")

(describe "Day 19"

          (context "Parsing"
                   (it "should parse the input."
                       (should= {:ip-reg 0,
                                 :program [["seti" 5 0 1] ["seti" 6 0 2] ["addi" 0 1 0]
                                           ["addr" 1 2 3] ["setr" 1 0 0] ["seti" 8 0 4]
                                           ["seti" 9 0 5]]} (parse-input example))))

          (context "State"
                   (it "should create the initial state."
                       (should= {:ip 0, :ip-reg 3, :reg [0 0 0 0 0 0]} (initial-state 3))))

          (context "Evaluate"
                   (it "should evaluate an instruction."
                       (should= [0 5 0 0 0 0] (evaluate [0 0 0 0 0 0] ["seti" 5 0 1])))
                   (it "can write the ip to the registers."
                       (should= [0 0 0 2 0 0] (write-ip-to-reg 2 3 [0 0 0 0 0 0])))
                   (it "can extract the ip from the registers."
                       (should= 2 (get-ip-from-reg 3 [0 0 0 2 0 0])))))

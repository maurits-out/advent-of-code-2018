(ns day02.core-spec
  (:require [speclj.core :refer :all]
            [day02.core :refer :all]
            [clojure.java.io :as io]))

(def input-file (io/resource "input.txt"))

(describe "Day 1"

  (context "Parsing"
    (it "should be able to parse the input."
      (should= ["abcdef", "bababc", "abbcde"] (parse-input "abcdef\nbababc\nabbcde\n"))))

  (context "Counting letters"
    (it "should be able to determine if an ID contains a specific count of any letter."
      (should-not ((has-any-letter-count-fn 2) "abcdef"))
      (should-not ((has-any-letter-count-fn 3) "abcdef"))
      (should ((has-any-letter-count-fn 2) "bababc"))
      (should ((has-any-letter-count-fn 3) "bababc"))))

  (context "Counting IDs"
    (it "should be able to count IDs that have exactly two of any letter."
      (should= 4 (count-ids ["abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab"] 2)))
    (it "should be able to count IDs that have exacvtly three of any letter."
      (should= 3 (count-ids ["abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab"] 3))))

  (context "Checksum"
    (it "should be able to calculate the checksum given the counts."
      (should= 12 (calculate-checksum 3 4))
      (should= 56 (calculate-checksum 7 8))))

  (context "Examples"
    (it "should solve part 1."
      (should= 12 (solve1 "abcdef\nbababc\nabbcde\nabcccd\naabcdd\nabcdee\nababab\n"))))

  (context "Solutions"
    (let [input (slurp input-file)]
      (it "should solve part 1."
        (should= 5456 (solve1 input))))))

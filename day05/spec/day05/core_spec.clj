(ns day05.core-spec
  (:require [speclj.core :refer :all]
            [day05.core :refer :all]
            [clojure.java.io :as io]))

(def input-file (io/resource "input.txt"))

(describe "Day 5"

  (context "Check if two units can react"
    (it "cannot react two units of different types."
      (should-not (react? \a \b))
      (should-not (react? \A \B)))
    (it "cannot not react two units of the same type and same polarity."
      (should-not (react? \A \A)))
    (it "cannot react two units of the same type and opposite polarity."
      (should (react? \a \A))
      (should (react? \A \a))))

  (context "Fully react polymer"
    (it "should be able to fully react a polymer"
      (should= 0 (react ""))
      (should= 1 (react "a"))
      (should= 0 (react "aA"))
      (should= 1 (react "aAC"))
      (should= 1 (react "abB"))
      (should= 1 (react "aACBb"))
      (should= 2 (react "aACBbD"))
      (should= 0 (react "abBA"))))

  (context "Examples"
    (it "should solve part 1"
      (should= 10 (react "dabAcCaCBAcCcaDA"))))

  (context "Solutions"
    (it "should solve part 1"
      (should= 11118 (react (slurp input-file))))))
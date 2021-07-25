(ns day06.core-spec
  (:require [speclj.core :refer :all]
            [day06.core :refer :all]
            [clojure.java.io :as io]
            [criterium.core :as crit]))

(def input-file (io/resource "input.txt"))

(describe "Day 6"

  (context "Parsing"
    (it "should parse a line of coordinates"
      (should= [1 1] (parse-line "1, 1"))
      (should= [3 4] (parse-line "3, 4")))
    (it "should parse lines of coordinates"
      (should= [[1 1] [3 4]] (parse-input "1, 1\n3, 4\n"))))

  (context "Manhattan distance"
    (it "should calculate the manhattan distance between two coordinates"
      (should= 10 (manhattan-distance [8 3] [1 6]))
      (should= 3 (manhattan-distance [3 4] [5 5]))))

  (context "Finding closest coordinate"
    (it "should find the index of a coordinate closest to a location"
      (should= 0 (find-closest-coordinate-index [[1 1] [1 6] [8 3] [3 4] [5 5] [8 9]] [0 0]))
      (should= 1 (find-closest-coordinate-index [[1 1] [1 6] [8 3] [3 4] [5 5] [8 9]] [0 6]))
      (should-be-nil (find-closest-coordinate-index [[1 1] [1 6] [8 3] [3 4] [5 5] [8 9]] [5 0]))))

  (context "Partial dimensions"
    (it "should find the corner coordinates of the partial view"
      (should= [[0 0] [9 10]] (partial-view-coordinates [[1 1] [1 6] [8 3] [3 4] [5 5] [8 9]]))))

  (context "Examples"
    (it "should solve part 1"
      (should= 17 (part1 "1, 1\n1, 6\n8, 3\n3, 4\n5, 5\n8, 9\n")))
    (it "should solve part 2"
      (part2 "1, 1\n1, 6\n8, 3\n3, 4\n5, 5\n8, 9\n" 32)))

  (context "Solutions"
    (it "should solve part 1"
      (should= 5975 (part1 (slurp input-file))))
    (it "should solve part 2"
      (should= 38670 (part2 (slurp input-file) 10000)))))

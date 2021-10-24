(ns day17.core-spec
  (:require [speclj.core :refer :all]
            [day17.core :refer :all]
            [clojure.java.io :as io]))

(describe "Day 17"

          (it "can solve part 1 and part 2 given the example."
              (let [input "x=495, y=2..7\ny=7, x=495..501\nx=501, y=3..7\nx=498, y=2..4\nx=506, y=1..2\nx=498, y=10..13\nx=504, y=10..13\ny=13, x=498..504\n"]
                (should= {:part1 57, :part2 29} (solve input))))

          (it "can solve part 1 and part 2 given the input."
              (let [input (slurp (io/resource "input.txt"))]
                (should= {:part1 33052, :part2 27068} (solve input)))))

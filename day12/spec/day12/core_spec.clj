(ns day12.core-spec
  (:require [speclj.core :refer :all]
            [day12.core :refer :all]))

(describe "Day 12"
          (it "should solve part 1."
              (should= 3248 (part1)))

          (it "should solve part 2."
              (should= 4000000000000 (part2))))

(ns day11.core-spec
  (:require [speclj.core :refer :all]
            [day11.core :refer :all]))

(describe "Day 11"

          (it "should solve part 1."
              (should= "33,54" (part1)))

          (it "should solve part 2."
              (should= "232,289,8" (part2))))

(ns day11.core-spec
  (:require [speclj.core :refer :all]
            [day11.core :refer :all]))

(describe "Day 11"

          (it "should calculate the power level of a fuel cell"
              (should= 4 (power-level 3 5 8))
              (should= -5 (power-level 122 79 57))
              (should= 0 (power-level 217 196 39))
              (should= 4 (power-level 101 153 71)))

          (it "should calculate the power of a square."
              (should= 29 (square-power 33 45 18))
              (should= 30 (square-power 21 61 42)))

          (it "should solve part 1."
              (should= [33 54] (part1 5235))))

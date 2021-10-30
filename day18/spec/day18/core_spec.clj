(ns day18.core-spec
  (:require [speclj.core :refer :all]
            [day18.core :refer :all]))

(describe "Day 18"

          (it "solves part 1"
              (let [gens (generations (parse-input))]
                (should= 543312 (part1 gens))))

          (it "solves part 2"
              (let [gens (generations (parse-input))]
                (should= 199064 (part2 gens)))))

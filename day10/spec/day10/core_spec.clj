(ns day10.core-spec
  (:require [speclj.core :refer :all]
            [day10.core :refer :all]
            [clojure.java.io :as io]))

(def input-file (io/resource "input.txt"))

(describe "Day 10"

          (it "should be able to parse the input."
              (should= [{:x 9, :y 1, :dx 0, :dy 2}
                        {:x 7, :y 0, :dx -1, :dy 0}]
                       (parse-input "position=< 9,  1> velocity=< 0,  2>\nposition=< 7,  0> velocity=<-1,  0>")))
          
          (it "should be able to update points."
              (should= [{:x 9, :y -2, :dx -1, :dy 1}
                        {:x 6, :y 0, :dx -1, :dy 0}]
                       (update-points [{:x 10, :y -3, :dx -1, :dy 1}
                                       {:x 7, :y 0, :dx -1, :dy 0}])))

          (it "should be able to find the smallest rectangle."
              (should= {:min-x 7, :min-y 0, :max-x 9, :max-y 1}
                       (smallest-rectangle [{:x 9, :y 1, :dx 0, :dy 2} {:x 7, :y 0, :dx -1, :dy 0}])))

          (it "should be able calculate the area of a rectangle."
              (should= 2 (area [{:x 9, :y 1, :dx 0, :dy 2} {:x 7, :y 0, :dx -1, :dy 0}])))

          (it "can solve part 1 and part2."
              (let [{:keys [positions seconds]} (solve (slurp input-file))]
                (plot positions)
                (should= 10639 seconds))))

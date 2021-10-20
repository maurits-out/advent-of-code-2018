(ns day16.core-spec
  (:require [speclj.core :refer :all]
            [day16.core :refer :all]
            [clojure.java.io :as io]))

(describe "Day 16"
          (context "Parsing"
                   (it "can parse a sample."
                       (should= {:before [3 2 1 1] :instruction [9 2 1 2] :after [3 2 2 1]}
                                (parse-sample "Before: [3, 2, 1, 1]\n9 2 1 2\nAfter:  [3, 2, 2, 1]\n"))
                       (should= {:before [1 3 2 2] :instruction [6 2 3 2] :after [1 3 1 2]}
                                (parse-sample "Before: [1, 3, 2, 2]\n6 2 3 2\nAfter:  [1, 3, 1, 2]\n")))

                   (it "can parse multiple samples."
                       (should= [{:before [1 3 2 2] :instruction [6 2 3 2] :after [1 3 1 2]}
                                 {:before [0 2 1 1] :instruction [3 3 3 3] :after [0 2 1 0]}]
                                (parse-samples "Before: [1, 3, 2, 2]\n6 2 3 2\nAfter:  [1, 3, 1, 2]\n\nBefore: [0, 2, 1, 1]\n3 3 3 3\nAfter:  [0, 2, 1, 0]\n"))))

          (context "Part 1"
                   (it "can solve part 1."
                       (should= 570 (count-samples-part1 (slurp (io/resource "input-samples.txt"))))))

          (context "Part 2"
                   (it "can solve part 2."
                       (should= 1 (get (run-test-program-part2 (slurp (io/resource "input-test-program.txt"))) 0)))))

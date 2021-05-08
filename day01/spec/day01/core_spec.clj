(ns day01.core-spec
  (:require [speclj.core :refer :all]
            [day01.core :refer :all]
            [clojure.java.io :as io]))

(def input-file (io/resource "input.txt"))

(describe "Day 1"

  (context "Parsing"
    (it "should be able to parse the input."
      (should= [1 -2 3 1] (parse-input "+1\n-2\n+3\n+1\n"))
      (should= [0] (parse-input "0\n"))))

  (context "Summing"
    (it "should be able to compute the sum of a vector."
      (should= 0 (sum [0]))
      (should= 3 (sum [1 -2 3 1]))))

  (context "Frequencies"
    (it "should be able to generate frequencies."
      (should= [0 1 -1 2 3] (take 5 (device-frequencies [1 -2 3 1])))
      (should= [0 1 -1 2 3 4 2] (take 7 (device-frequencies [1 -2 3 1])))))

  (context "Duplicate"
    (it "should be able to find first duplicate."
      (should= 2 (find-first-duplicate [1 -1 2 3 4 2]))))

  (context "Examples"
    (it "should solve part 1."
      (should= 3 (solve1 "+1\n-2\n+3\n+1"))
      (should= 3 (solve1 "+1\n+1\n+1\n"))
      (should= 0 (solve1 "+1\n+1\n-2\n"))
      (should= -6 (solve1 "-1\n-2\n-3\n")))
    (it "should solve part 2."
      (should= 2 (solve2 "+1\n-2\n+3\n+1"))
      (should= 0 (solve2 "+1\n-1\n"))
      (should= 10 (solve2 "+3\n+3\n+4\n-2\n-4\n"))
      (should= 5 (solve2 "-6\n+3\n+8\n+5\n-6\n"))
      (should= 14 (solve2 "+7\n+7\n-2\n-7\n-4\n"))))

  (context "Solution"
    (let [input (slurp input-file)]
      (it "should solve part 1."
        (should= 400 (solve1 input)))
      (it "should solve part 2."
        (should= 232 (solve2 input))))))

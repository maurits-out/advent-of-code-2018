(ns day03.core-spec
  (:require [speclj.core :refer :all]
            [day03.core :refer :all]
            [clojure.java.io :as io]))

(def input-file (io/resource "input.txt"))

(describe "Day 2"

  (context "Parsing"

    (it "should be able to parse a claim."
      (should== ["123" 3 2 5 4] (parse-claim "#123 @ 3,2: 5x4"))
      (should== ["13" 288 876 18 16] (parse-claim "#13 @ 288,876: 18x16")))
    (it "should be able to parse the input."
      (should== [["123" 3 2 5 4] ["13" 288 876 18 16]]
        (parse-input "#123 @ 3,2: 5x4\n#13 @ 288,876: 18x16\n"))))

  (context "Converting to square inches"

    (it "should be able to convert a claim to square inches."
      (should== [[3 2] [3 3] [3 4] [3 5]
                 [4 2] [4 3] [4 4] [4 5]
                 [5 2] [5 3] [5 4] [5 5]
                 [6 2] [6 3] [6 4] [6 5]
                 [7 2] [7 3] [7 4] [7 5]]
        (convert-claim-to-square-inches ["123" 3 2 5 4])))

    (it "should be able to create a collection of all square inches of multiple claims."
      (should== [] (convert-claims-to-square-inches []))
      (should== [[1 7] [1 8] [2 7] [2 8]] (convert-claims-to-square-inches [["1" 1 7 2 2]]))
      (should== [[1 7] [1 8] [2 7] [2 8] [3 5]] (convert-claims-to-square-inches [["1" 1 7 2 2] ["2" 3 5 1 1]]))))

  (context "Counting"

    (it "should be able to count the number of square inches of fabric within two or more claims."
      (should= 0 (count-square-inches-within-at-least-2-claims [[1 7] [1 8] [2 7] [2 8] [3 5]]))
      (should= 4 (count-square-inches-within-at-least-2-claims [[3 1] [4 1] [5 1] [6 1]
                                                                [3 2] [4 2] [5 2] [6 2]
                                                                [3 3] [4 3] [5 3] [6 3]
                                                                [3 4] [4 4] [5 4] [6 4]
                                                                [1 3] [2 3] [3 3] [5 3]
                                                                [1 4] [2 4] [3 4] [5 4]
                                                                [1 5] [2 5] [3 5] [5 5]
                                                                [1 6] [2 6] [3 6] [5 6]]))))

  (context = "Solutions"

    (it "should solve part 1"
      (should= 110383 (solve1 (slurp input-file))))))

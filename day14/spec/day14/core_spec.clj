(ns day14.core-spec
  (:require [speclj.core :refer :all]
            [day14.core :refer :all]))

(describe "Chocolate Charts"
          (context "Part 1"
                   (it "should find the scores of ten recipes immediately after the number of recipes in the input"
                       (should= "5158916779" (count-recipes-scores-after 9))
                       (should= "0124515891" (count-recipes-scores-after 5))
                       (should= "9251071085" (count-recipes-scores-after 18))
                       (should= "5941429882" (count-recipes-scores-after 2018))
                       (should= "5371393113" (count-recipes-scores-after 513401))))

          (context "Part 2"
                   (it "should count the recipes to the left of the score sequence"
                       (should= 9 (count-recipes-to-the-left [5 1 5 8 9]))
                       (should= 5 (count-recipes-to-the-left [0 1 2 4 5]))
                       (should= 18 (count-recipes-to-the-left [9 2 5 1 0]))
                       (should= 2018 (count-recipes-to-the-left [5 9 4 1 4]))
                       (should= 20286858 (count-recipes-to-the-left [5 1 3 4 0 1])))))

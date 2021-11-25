(ns day21.core-spec
  (:require [speclj.core :refer :all]
            [day21.core :refer :all]
            [clojure.java.io :as io]))

(def input-file (io/resource "input.txt"))

(describe "Day 21"

  (it "should solve part1."
    (should= 12980435 (part1-new)))

  (it "should solve part2."
    (should= 12980435 (part2-new)))
  )

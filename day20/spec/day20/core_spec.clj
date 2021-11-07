(ns day20.core-spec
  (:require [speclj.core :refer :all]
            [day20.core :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(def input-file (io/resource "input.txt"))

(describe "Day 20"

  (it "Should solve puzzle"
    (let [input (slurp input-file)]
      (should= {:part1 3958 :part2 8566} (solve (string/trim input))))))

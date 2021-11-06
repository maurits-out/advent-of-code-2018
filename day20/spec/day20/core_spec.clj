(ns day20.core-spec
  (:require [speclj.core :refer :all]
            [day20.core :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(def input-file (io/resource "input.txt"))

(describe "Day 20"

  (it "Should find the largest number of doors to pass that is required to reach a room."
    (should= 1 (find-largest-numbers-of-doors "^E$"))
    (should= 2 (find-largest-numbers-of-doors "^EE$"))
    (should= 3 (find-largest-numbers-of-doors "^WNE$"))
    (should= 2 (find-largest-numbers-of-doors "^EEW$"))
    (should= 10 (find-largest-numbers-of-doors "^ENWWW(NEEE|SSE(EE|N))$"))
    (should= 18 (find-largest-numbers-of-doors "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"))
    (should= 23 (find-largest-numbers-of-doors "^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$"))
    (should= 31 (find-largest-numbers-of-doors "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$")))

  (it "Should solve part 1"
    (let [input (slurp input-file)]
      (should= 1 (find-largest-numbers-of-doors (string/trim input))))))

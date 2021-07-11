(ns day03.core
  (:require [clojure.string :as string]))

(def claim-regex #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)")

(defn parse-claim [claim]
  (let [[_ id left top width height] (re-matches claim-regex claim)]
    [id (Integer/parseInt left) (Integer/parseInt top) (Integer/parseInt width) (Integer/parseInt height)]))

(defn parse-input [input]
  (mapv parse-claim (string/split-lines input)))

(defn convert-claim-to-square-inches [[_ left top width height]]
  (for [x (range left (+ left width)) y (range top (+ top height))] [x y]))

(defn convert-claims-to-square-inches [claims]
  (apply concat (map convert-claim-to-square-inches claims)))

(defn count-square-inches-within-at-least-2-claims [square-inches]
  (->>
    (frequencies square-inches)
    (filter (fn [[_ num-square-inches]] (> num-square-inches 1)))
    (count)))

(defn solve1 [input]
  (let [claims (parse-input input)
        square-inches (convert-claims-to-square-inches claims)]
    (count-square-inches-within-at-least-2-claims square-inches)))

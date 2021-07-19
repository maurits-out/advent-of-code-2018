(ns day03.core
  (:require [clojure.string :as string]))

(def claim-regex #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)")

(defn parse-claim [claim]
  (->>
    (re-matches claim-regex claim)
    (rest)
    (mapv #(Integer/parseInt %))))

(defn claim-values->map [[id left top width height]]
  {:id id :left, left :top, top :width width, :height height})

(defn parse-input [input]
  (mapv (comp claim-values->map parse-claim) (string/split-lines input)))

(defn convert-claim-to-square-inches [{:keys [left top width height]}]
  (for [x (range 0 width) y (range 0 height)]
    [(+ left x) (+ top y)]))

(defn convert-claims-to-square-inches [claims]
  (apply concat (map convert-claim-to-square-inches claims)))

(defn count-square-inches-within-at-least-2-claims [square-inches]
  (->>
    (frequencies square-inches)
    (filter (fn [[_ count]] (> count 1)))
    (count)))

(defn solve1 [input]
  (let [claims (parse-input input)
        square-inches (convert-claims-to-square-inches claims)]
    (count-square-inches-within-at-least-2-claims square-inches)))

(defn overlapping? [{left1 :left top1 :top width1 :width height1 :height}
                    {left2 :left top2 :top width2 :width height2 :height}]
  (and
    (< left1 (+ left2 width2))
    (> (+ left1 width1) left2)
    (< top1 (+ top2 height2))
    (> (+ top1 height1) top2)))

(defn ids-of-non-overlapping-claim [claims]
  (for [claim claims
        :when (not-any? #(and (not= claim %) (overlapping? claim %))
                claims)]
    (:id claim)))

(defn solve2 [input]
  (let [claims (parse-input input)]
    (first (ids-of-non-overlapping-claim claims))))

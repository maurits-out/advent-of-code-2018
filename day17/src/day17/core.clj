(ns day17.core
  (:require [clojure.string :as string]
            [clojure.set :as set]))

(defn parse-int [s]
  (Integer/parseInt s))

(defn parse-line [l]
  (let [[_ axis s1 s2 s3] (re-matches #"([xy])=(\d+), [xy]=(\d+)..(\d+)" l)
        r (range (parse-int s2) (inc (parse-int s3)))
        n (parse-int s1)]
    (case axis
      "x" (into (hash-set) (for [y r] [n y]))
      (into (hash-set) (for [x r] [x n])))))

(defn parse-input [input]
  (apply set/union (for [l (string/split-lines input)] (parse-line l))))

(defn create-grid [clay]
  (let [max-y (apply max (map second clay))
        min-x (- (apply min (map first clay)) 2)
        max-x (+ (apply max (map first clay)) 2)
        grid (make-array Character/TYPE (inc max-y) (- max-x min-x -1))
        x-water-spring (- 500 min-x)]
    (doseq [y (range (inc max-y))
            x (range min-x (inc max-x))]
      (aset grid y (- x min-x) (if (contains? clay [x y]) \# \.)))
    (aset grid 0 x-water-spring \+)
    {:grid grid :water-spring [x-water-spring 0]}))

(defn blocked-at-side? [[x y] grid d]
  (case (aget grid y x)
    \# true
    \. false
    (recur [(+ x d) y] grid d)))

(defn blocked-by-clay-at-both-sides? [[x y] grid]
  (and (blocked-at-side? [x y] grid 1) (blocked-at-side? [x y] grid -1)))

(defn settle-side [[x y] grid d]
  (case (aget grid y x)
    \# nil
    (do
      (aset grid y x \~)
      (recur [(+ x d) y] grid d))))

(defn settle-both-sides [[x y] grid]
  (do
    (settle-side [x y] grid 1)
    (settle-side [x y] grid -1)))

(defn move [[x y] grid]
  (if (< y (dec (alength grid)))
    (do
      (if (= \. (aget grid (inc y) x))
        (do
          (aset grid (inc y) x \|)
          (move [x (inc y)] grid)))
      (if (contains? #{\# \~} (aget grid (inc y) x))
        (do
          (if (= (aget grid y (dec x)) \.)
            (do
              (aset grid y (dec x) \|)
              (move [(dec x) y] grid)))
          (if (= (aget grid y (inc x)) \.)
            (do
              (aset grid y (inc x) \|)
              (move [(inc x) y] grid)))
          (if (blocked-by-clay-at-both-sides? [x y] grid)
            (settle-both-sides [x y] grid)))))))

(defn count-tiles [grid min-y allowed]
  (apply + (for [r (drop min-y grid) c r :when (contains? allowed c)] 1)))

(defn solve [input]
  (let [clay (parse-input input)
        min-y (apply min (map second clay))
        {:keys [water-spring grid]} (create-grid clay)]
    (do
      (move water-spring grid)
      {:part1 (count-tiles grid min-y #{\~ \|}) :part2 (count-tiles grid min-y #{\~})})))

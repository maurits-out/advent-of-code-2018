(ns day18.core
  (:require [clojure.java.io :as io]
            [clojure.walk :as w]
            [clojure.string :as string]))

(defn read-input []
  (let [input-file (io/resource "input.txt")]
    (slurp input-file)))

(defn parse-input []
  (let [input (read-input)]
    (into (hash-map)
          (apply concat (map-indexed
                          (fn [row line] (map-indexed (fn [col ch] [[row col] ch]) line))
                          (string/split-lines input))))))

(defn adjacent-acres-freq [[row column] world]
  (let [r (range -1 2)
        adjacent-coordinates (for [dr r, dc r, :when (not= dr dc 0)]
                               [(+ row dr) (+ column dc)])]
    (->> adjacent-coordinates
         (map #(world %))
         (remove nil?)
         (frequencies))))

(defn freq-at-least [freq ch min]
  (>= (get freq ch 0) min))

(defn update-acre [c world]
  (let [freq (adjacent-acres-freq c world)]
    (case (get world c)
      \. (if (freq-at-least freq \| 3) \| \.)
      \| (if (freq-at-least freq \# 3) \# \|)
      \# (if (and (freq-at-least freq \# 1) (freq-at-least freq \| 1)) \# \.))))

(defn next-generation [world]
  (w/walk (fn [[c _]] [c (update-acre c world)]) identity world))

(defn generations [world]
  (iterate next-generation world))

(defn resource-value [world]
  (let [freq (frequencies (vals world))]
    (* (freq \|) (freq \#))))

(defn part1 [gens]
  (let [end-state (nth gens 10)]
    (resource-value end-state)))

;; https://stackoverflow.com/questions/19894216/clojure-find-repetition
(defn get-cycle [xs]
  (first (filter #(number? (first %))
                 (reductions
                   (fn [[m i] x] (if-let [xat (m x)] [xat i] [(assoc m x i) (inc i)]))
                   [(hash-map) 0] xs))))

(defn part2 [gens]
  (let [minutes 1000000000
        [first second] (get-cycle gens)
        diff (- second first)
        i (* diff (quot (- minutes first) diff))
        j (- minutes first i)]
    (resource-value (nth gens (+ first j)))))

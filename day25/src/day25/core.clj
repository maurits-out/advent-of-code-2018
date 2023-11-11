(ns day25.core
  (:require [clojure.string :as string]))

(def max-distance 3)

(defn to-int [str]
  (Integer/parseInt str))

(defn split-line [line]
  (string/split line #","))

(defn parse-line [line]
  (mapv to-int (split-line line)))

(defn parse-input [input]
  (let [lines (string/split-lines input)]
    (set (mapv parse-line lines))))

(defn sum [numbers]
  (reduce + numbers))

(defn manhattan-distance [point1 point2]
  (sum (map #(abs (- %1 %2)) point1 point2)))

(defn is-within-distance? [point1 point2]
  (<= (manhattan-distance point1 point2) max-distance))

(defn find-neighbors [remaining point]
  (filter #(is-within-distance? point %) remaining))

(defn dfs [remaining point]
  (let [updated-remaining (disj remaining point)
        neighbors (find-neighbors updated-remaining point)]
    (reduce dfs updated-remaining neighbors)))

(defn count-constellations [fixed-points]
  (loop [acc 0
         remaining fixed-points]
    (if (empty? remaining)
      acc
      (let [point (first remaining)
            updated-remaining (dfs remaining point)]
        (recur (inc acc) updated-remaining)))))

(defn solve [input]
  (let [fixed-points (parse-input input)]
    (count-constellations fixed-points)))

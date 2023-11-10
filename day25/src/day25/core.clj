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
    (mapv parse-line lines)))

(defn sum [numbers]
  (reduce + numbers))

(defn manhattan-distance [point1 point2]
  (sum (map #(abs (- %1 %2)) point1 point2)))

(defn is-within-distance? [point1 point2]
  (<= (manhattan-distance point1 point2) max-distance))

(defn find-neighbors [fixed-points unvisited point]
  (filter #(and (contains? unvisited %) (is-within-distance? point %)) fixed-points))

(defn dfs [fixed-points unvisited point]
  (let [updated-unvisited (disj unvisited point)
        neighbors (find-neighbors fixed-points unvisited point)]
    (reduce (partial dfs fixed-points) updated-unvisited neighbors)))

(defn count-constellations [fixed-points]
  (loop [current-count 0
         unvisited (set fixed-points)]
    (if (empty? unvisited)
      current-count
      (let [point (first unvisited)
            updated-unvisited (dfs fixed-points unvisited point)]
        (recur (inc current-count) updated-unvisited)))))

(defn solve [input]
  (let [fixed-points (parse-input input)]
    (count-constellations fixed-points)))

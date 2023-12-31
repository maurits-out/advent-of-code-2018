(ns day25.core
  (:require [clojure.string :as string]))

(defn parse-line [line]
  (map #(Integer/parseInt %) (string/split line #",")))

(defn parse-input [input]
  (let [lines (string/split-lines input)]
    (set (map parse-line lines))))

(defn manhattan-distance [point1 point2]
  (reduce + (map #(abs (- %1 %2)) point1 point2)))

(defn is-within-distance? [point1 point2]
  (<= (manhattan-distance point1 point2) 3))

(defn find-neighbors [remaining point]
  (filter (partial is-within-distance? point) remaining))

(defn dfs [remaining point]
  (let [updated-remaining (disj remaining point)
        neighbors (find-neighbors updated-remaining point)]
    (reduce dfs updated-remaining neighbors)))

(defn solve [input]
  (loop [acc 0
         remaining (parse-input input)]
    (if (empty? remaining)
      acc
      (let [point (first remaining)
            updated-remaining (dfs remaining point)]
        (recur (inc acc) updated-remaining)))))

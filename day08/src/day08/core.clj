(ns day08.core
  (:require [clojure.string :as string]))

(defn parse-input [input]
  (map #(Integer/parseInt %) (string/split input #" ")))

(declare sum-of-node)

(defn calculate-sum [child-values metadata]
  (apply + (concat child-values metadata)))

(defn sum-of-child-nodes [numbers num-child-nodes acc]
  (if (zero? num-child-nodes)
    [numbers acc]
    (let [[remaining sum] (sum-of-node numbers)]
      (recur remaining (dec num-child-nodes) (conj acc sum)))))

(defn sum-of-node [[num-child-nodes num-metadata-entries & numbers]]
  (let [[remaining child-values] (sum-of-child-nodes numbers num-child-nodes [])
        [metadata rest] (split-at num-metadata-entries remaining)]
    [rest (calculate-sum child-values metadata)]))

(declare value-of-node)

(defn calculate-value [child-values metadata]
  (reduce (fn [acc n] (+ acc (nth child-values (dec n) 0))) 0 metadata))

(defn values-of-child-nodes [numbers num-child-nodes acc]
  (if (zero? num-child-nodes)
    [numbers acc]
    (let [[remaining value] (value-of-node numbers)]
      (recur remaining (dec num-child-nodes) (conj acc value)))))

(defn value-of-node [[num-child-nodes num-metadata-entries & numbers]]
  (if (zero? num-child-nodes)
    (let [[metadata rest] (split-at num-metadata-entries numbers)]
      [rest (apply + metadata)])
    (let [[metadata-rest child-values] (values-of-child-nodes numbers num-child-nodes [])
          [metadata rest] (split-at num-metadata-entries metadata-rest)]
      [rest (calculate-value child-values metadata)])))

(defn part1 [input]
  (second (sum-of-node (parse-input input))))

(defn part2 [input]
  (second (value-of-node (parse-input input))))

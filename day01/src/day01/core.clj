(ns day01.core
  (:require [clojure.string :as string]))

(defn parse-input [input]
  (let [lines (string/split-lines input)]
    (mapv #(Integer/parseInt %) lines)))

(defn sum [numbers] (apply + numbers))

(defn device-frequencies [changes] (reductions + 0 (cycle changes)))

(defn find-first-duplicate [numbers]
  (loop [[current & remaining] numbers
         seen #{}]
    (if (contains? seen current)
      current
      (recur remaining (conj seen current)))))

(defn solve1 [input] (sum (parse-input input)))

(defn solve2 [input]
  (let [frequencies (device-frequencies (parse-input input))]
    (find-first-duplicate frequencies)))

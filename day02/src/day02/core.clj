(ns day02.core
  (:require [clojure.string :as string]))

(defn parse-input [input] (string/split-lines input))

(defn has-any-letter-count-fn [count]
  #(some #{count} (vals (frequencies %))))

(defn count-ids [ids letter-count]
  (let [has-any-letter-count (has-any-letter-count-fn letter-count)]
    (count (filter #(has-any-letter-count %) ids))))

(defn calculate-checksum [x y] (* x y))

(defn solve1 [input]
  (let [ids (parse-input input)]
    (calculate-checksum (count-ids ids 2) (count-ids ids 3))))

(defn count-different-letters [word1 word2]
  (count (filter (complement zero?) (mapv compare word1 word2))))

(defn correct-boxes [id1 id2] (= 1 (count-different-letters id1 id2)))

(defn find-correct-boxes [ids]
  (first (for [id1 ids id2 ids
               :when (neg? (compare id1 id2))
               :when (correct-boxes id1 id2)]
           [id1 id2])))

(defn common-letters [id1 id2]
  (->>
    (map vector id1 id2)
    (filter #(zero? (apply compare %)))
    (map first)
    (string/join)))

(defn solve2 [input]
  (let [ids (parse-input input)
        correct-ids (find-correct-boxes ids)]
    (apply common-letters correct-ids)))

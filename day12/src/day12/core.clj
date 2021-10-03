(ns day12.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(def pattern-rule-length 5)

(defn parse-rules [lines]
  (into (hash-map) (for [l lines]
                     (let [[_ left right] (re-matches #"(.{5}) => (.)" l)]
                       [left right]))))

(defn read-input []
  (let [input (slurp (io/resource "input.txt"))
        lines (string/split-lines input)]
    {:input-plants (subs (first lines) 15)
     :rules        (parse-rules (drop 2 lines))}))

(defn trim [plants offset]
  (let [furthest-left (string/index-of plants \#)
        furthest-right (string/last-index-of plants \#)]
    {:plants (subs plants furthest-left (inc furthest-right))
     :offset (+ offset furthest-left -2)}))

(defn update-plants [rules plants]
  (->> (str "...." plants "....")
       (partition pattern-rule-length 1)
       (map (comp #(get rules % \.) string/join))
       (string/join)))

(defn next-generation [rules {:keys [plants offset]}]
  (trim (update-plants rules plants) offset))

(defn initial-state [plants] {:plants plants :offset 0})

(defn generations [plants rules]
  (iterate (partial next-generation rules) (initial-state plants)))

(defn part1 []
  (let [{:keys [input-plants rules]} (read-input)
        {:keys [plants offset]} (nth (generations input-plants rules) 20)
        indices (keep-indexed #(if (= %2 \#) (+ %1 offset)) plants)]
    (apply + indices)))

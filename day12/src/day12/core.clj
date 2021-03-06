(ns day12.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

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
       (partition 5 1)
       (map (comp #(get rules % \.) string/join))
       (string/join)))

(defn next-generation [rules {:keys [plants offset]}]
  (trim (update-plants rules plants) offset))

(defn generations [plants rules]
  (iterate (partial next-generation rules) {:plants plants :offset 0}))

(defn sum-of-numbers [plants offset]
  (apply + (keep-indexed #(if (= %2 \#) (+ %1 offset)) plants)))

(defn part1 []
  (let [{:keys [input-plants rules]} (read-input)
        {:keys [plants offset]} (nth (generations input-plants rules) 20)]
    (sum-of-numbers plants offset)))

(defn part2 []
  (let [{:keys [input-plants rules]} (read-input)]
    (loop [[current next & _ :as all] (generations input-plants rules)
           count 0]
      (if (= (:plants current) (:plants next))
        (let [increment (- (:offset next) (:offset current))]
          (sum-of-numbers (:plants next) (+ (:offset current) (* increment (- 50000000000 count)))))
        (recur (rest all) (inc count))))))

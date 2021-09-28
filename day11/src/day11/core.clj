(ns day11.core
  (:require [clojure.string :as string]))

(def grid-size 300)
(def grid-serial-number 5235)

(defn power-level [x y]
  (let [rack-id (+ x 10)
        pl1 (* y rack-id)
        pl2 (+ pl1 grid-serial-number)
        pl3 (* pl2 rack-id)
        pl4 (rem (quot pl3 100) 10)]
    (- pl4 5)))

(defn partial-sum [partial-sums x y]
  (+ (power-level x y)
     (partial-sums [(dec x) y])
     (partial-sums [x (dec y)])
     (- (partial-sums [(dec x) (dec y)]))))

(defn generate-partial-sums-row [partial-sums y]
  (reduce
    #(assoc %1 [%2 y] (partial-sum %1 %2 y))
    (assoc partial-sums [0 y] 0)
    (range 1 (inc grid-size))))

(defn generate-partial-sums []
  (reduce
    #(generate-partial-sums-row %1 %2)
    (into (hash-map) (for [x (range 0 (inc grid-size))] [[x 0] 0]))
    (range 1 (inc grid-size))))

(defn square-power-level [[left-x top-y] size partial-sums]
  (let [x (+ left-x (dec size))
        y (+ top-y (dec size))]
    (+ (partial-sums [x y])
       (- (partial-sums [(- x size) y]))
       (- (partial-sums [x (- y size)]))
       (partial-sums [(- x size) (- y size)]))))

(defn square-coordinates [size]
  (let [r (range 1 (+ grid-size (- size) 2))]
    (for [x r, y r] [x y])))

(defn square-with-largest-power-level [size partial-sums]
  (apply max-key #(square-power-level % size partial-sums) (square-coordinates size)))

(defn part1 []
  (let [partial-sums (generate-partial-sums)]
    (string/join "," (square-with-largest-power-level 3 partial-sums))))

(defn part2 []
  (let [partial-sums (generate-partial-sums)
        [[x y] size] (->> (range 1 (inc grid-size))
                          (map (fn [size] [(square-with-largest-power-level size partial-sums) size]))
                          (apply max-key (fn [[c size]] (square-power-level c size partial-sums))))]
    (string/join "," [x y size])))

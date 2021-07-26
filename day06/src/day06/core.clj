(ns day06.core
  (:require [clojure.string :as string]))

(defn parse-line [line]
  (let [[x y] (string/split line #", ")]
    [(Integer/parseInt x) (Integer/parseInt y)]))

(defn parse-input [input]
  (for [line (string/split-lines input)]
    (parse-line line)))

(defn manhattan-distance [[^int x1 ^int y1] [^int x2 ^int y2]]
  (+ (Math/abs (- x2 x1)) (Math/abs (- y2 y1))))

(defn find-closest-coordinate-index [coordinates location]
  (let [index-distance (map-indexed (fn [idx c] [idx (manhattan-distance c location)]) coordinates)
        group-by-distance (group-by second index-distance)
        closest-group (second (apply min-key first group-by-distance))]
    (if (= (count closest-group) 1)
      (ffirst closest-group)
      nil)))

(defn partial-view-coordinates [coordinates]
  (let [x-values (mapv #(first %) coordinates)
        y-values (mapv #(second %) coordinates)]
    [[(dec (apply min x-values)) (dec (apply min y-values))] [(inc (apply max x-values)) (inc (apply max y-values))]]))

(defn calculate-index-of-closest-coordinates-in-partial-view [coordinates [min-x min-y] [max-x max-y]]
  (for [x (range min-x (inc max-x))
        y (range min-y (inc max-y))
        :let [idx (find-closest-coordinate-index coordinates [x y])]
        :when idx]
    [[x y] idx]))

(defn extract-indices-of-edge [location->index [min-x min-y] [max-x max-y]]
  (set (for [[[x y] idx] location->index
             :when (or (= x min-x) (= x max-x) (= y min-y) (= y max-y))]
         idx)))

(defn extract-indices-of-closed-areas [location->index edge-indices]
  (for [[_ idx] location->index
        :when (not (contains? edge-indices idx))]
    idx))

(defn part1 [input]
  (let [coordinates (parse-input input)
        [top-left bottom-right] (partial-view-coordinates coordinates)
        location->index (calculate-index-of-closest-coordinates-in-partial-view coordinates top-left bottom-right)
        edge-indices (extract-indices-of-edge location->index top-left bottom-right)
        indices-closed-areas (extract-indices-of-closed-areas location->index edge-indices)]
    (apply max (vals (frequencies indices-closed-areas)))))

(defn center-coordinates-of-partial-view [coordinates]
  (let [[[min-x min-y] [max-x max-y]] (partial-view-coordinates coordinates)]
    [(quot (+ min-x max-x) 2) (quot (+ min-y max-y) 2)]))

(defn generate-border [[center-x center-y] iteration]
  (let [x (- center-x iteration)
        y (- center-y iteration)
        length (+ (* iteration 2) 1)]
    (concat
      (for [dx (range 0 length)] [(+ x dx) y])
      (for [dy (range 1 length)] [(+ x (dec length)) (+ y dy)])
      (for [dx (range 0 (dec length))] [(+ x dx) (+ y (dec length))])
      (for [dy (range 1 (dec length))] [x (+ y dy)]))))

(defn calculate-total-distance [coordinates location]
  (apply + (map #(manhattan-distance location %) coordinates)))

(defn part2 [input total-distance]
  (let [coordinates (parse-input input)
        center (center-coordinates-of-partial-view coordinates)]
    (loop [iteration 0 size 0]
      (let [location-count (count (for [l (generate-border center iteration)
                                        :when (< (calculate-total-distance coordinates l) total-distance)]
                                    l))]
        (if (zero? location-count)
          size
          (recur (inc iteration) (+ size location-count)))))))

(ns day10.core
  (:require [clojure.string :as string]))

(defn parse-int [s] (Integer/parseInt (string/triml s)))

(defn parse-input [input]
  (for [l (string/split-lines input)]
    (let [[_ x y dx dy] (re-matches #"position=<(.+), (.+)> velocity=<(.+), (.+)>" l)]
      {:x (parse-int x), :y (parse-int y), :dx (parse-int dx), :dy (parse-int dy)})))

(defn update-points [ps]
  (for [{:keys [x y dx dy]} ps]
    {:x (+ x dx), :y (+ y dy), :dx dx, :dy dy}))

(defn smallest-rectangle [ps]
  {:min-x (apply min (for [{:keys [x]} ps] x))
   :min-y (apply min (for [{:keys [y]} ps] y))
   :max-x (apply max (for [{:keys [x]} ps] x))
   :max-y (apply max (for [{:keys [y]} ps] y))})

(defn area [ps]
  (let [{:keys [min-x min-y max-x max-y]} (smallest-rectangle ps)]
    (* (- max-x min-x) (- max-y min-y))))

(defn solve [input]
  (loop [ps (parse-input input) t 0]
    (let [next-ps (update-points ps)]
      (if (< (area next-ps) (area ps))
        (recur next-ps (inc t))
        {:positions ps, :seconds t}))))

(defn convert-positions [ps]
  (set (map #(dissoc % :dx :dy) ps)))

(defn row2str [points y min-x max-x]
  (apply str (for [x (range min-x (inc max-x))]
               (if (contains? points {:x x, :y y}) "#" " "))))

(defn plot-rows [ps]
  (let [points (convert-positions ps)
        {:keys [min-x min-y max-x max-y]} (smallest-rectangle points)]
    (for [y (range min-y (inc max-y))]
      (row2str points y min-x max-x))))

(defn plot [ps]
  (run! println (plot-rows ps)))

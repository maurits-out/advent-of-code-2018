(ns day13.core
  (:require [clojure.string :as string]
            [clojure.walk :as w]))

(defn parse-input [input]
  (into (hash-map)
    (apply concat (map-indexed
                    (fn [y line] (map-indexed (fn [x ch] [[x y] ch]) line))
                    (string/split-lines input)))))

;; 0 = north, 1 = east, 2 = south, 3 = west
(defn car-char-to-heading [ch]
  (get {\^ 0 \> 1 \v 2 \< 3} ch))

(defn extract-cars [tracks]
  (for [[[x y] ch] tracks :when (contains? #{\< \> \^ \v} ch)]
    {:x x :y y :heading (car-char-to-heading ch) :next-turn 0}))

(defn replace-car-char [ch]
  (case ch
    (\< \>) \-
    (\^ \v) \|
    ch))

(defn remove-cars [tracks]
  (w/walk (fn [[c ch]] [c (replace-car-char ch)]) identity tracks))

(defn move [x y heading]
  (case heading
    0 [x (dec y)]
    1 [(inc x) y]
    2 [x (inc y)]
    3 [(dec x) y]))

(defn turn-left [heading]
  (if (= heading 0) 3 (dec heading)))

(defn turn-right [heading]
  (if (= heading 3) 0 (inc heading)))

(defn update-heading [ch heading next-turn]
  (case ch
    (\- \|) heading
    \+ (case (rem next-turn 3)
         0 (turn-left heading)
         2 (turn-right heading)
         heading)
    \/ (case heading
         (0 2) (turn-right heading)
         (turn-left heading))
    \\ (case heading
         (0 2) (turn-left heading)
         (turn-right heading))))

(defn update-next-turn [ch next-turn]
  (if (= ch \+) (inc next-turn) next-turn))

(defn move-car [{:keys [x y heading next-turn]} tracks]
  (let [new-location (move x y heading)]
    {:x         (first new-location)
     :y         (second new-location)
     :heading   (update-heading (tracks new-location) heading next-turn)
     :next-turn (update-next-turn (tracks new-location) next-turn)}))

(defn collided? [car1 car2]
  (and (= (:x car1) (:x car2)) (= (:y car1) (:y car2))))

(defn next-tick [cars tracks]
  (loop [[car & cs] (sort-by (juxt :y :x) cars)
         remaining-cars []
         collided-cars []]
    (if-not car
      [remaining-cars collided-cars]
      (let [moved-car (move-car car tracks)
            new-collided (filter #(collided? % moved-car) (concat cs remaining-cars))]
        (if (empty? new-collided)
          (recur cs
            (conj remaining-cars moved-car)
            collided-cars)
          (recur (remove #(collided? % moved-car) cs)
            (remove #(collided? % moved-car) remaining-cars)
            (conj (concat new-collided collided-cars) moved-car)))))))

(defn do-ticks [input stop-fn result-fn]
  (let [tracks-with-cars (parse-input input)
        tracks (remove-cars tracks-with-cars)]
    (loop [cars (extract-cars tracks-with-cars)]
      (let [[remaining-cars collided-cars] (next-tick cars tracks)]
        (if (stop-fn remaining-cars collided-cars)
          (result-fn remaining-cars collided-cars)
          (recur remaining-cars))))))

(defn car-location-to-string [car]
  (str (:x car) "," (:y car)))

(defn part1 [input]
  (do-ticks input
    (fn [_ collided] (not-empty collided))
    (fn [_ collided] (car-location-to-string (first collided)))))

(defn part2 [input]
  (do-ticks input
    (fn [remaining _] (= (count remaining) 1))
    (fn [remaining _] (car-location-to-string (first remaining)))))

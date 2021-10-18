(ns day15.core
  (:require [clojure.string :as string]
            [clojure.walk :as w]
            [data.deque :refer [deque add-last remove-first peek-first]]))

(defn parse-input [input]
  (into (hash-map)
        (apply concat (map-indexed
                        (fn [row line] (map-indexed (fn [col ch] [[row col] ch]) line))
                        (string/split-lines input)))))

(defn extract-units [world]
  (->> (filter (fn [[_ ch]] (contains? #{\E \G} ch)) world)
       (map-indexed (fn [idx [c ch]] [idx {:id idx :location c, :type ch, :hit-points 200}]))
       (into (hash-map))))

(defn replace-unit [ch]
  (case ch
    (\E \G) \.
    ch))

(defn replace-units [world]
  (w/walk (fn [[c ch]] [c (replace-unit ch)]) identity world))

(defn turn-order [id->unit]
  (->> (sort-by (fn [[_ unit]] (:location unit)) id->unit)
       (map (fn [[id _]] id))))

(defn identify-targets [type units]
  (for [u units :when (not= (:type u) type)]
    u))

(defn abs [^long x]
  (Math/abs x))

(defn in-range? [unit1 unit2]
  (let [[r1 c1] (:location unit1)
        [r2 c2] (:location unit2)]
    (= (+ (abs (- r2 r1)) (abs (- c2 c1))) 1)))

(defn adjacent-squares [[row column]]
  [[(dec row) column] [row (dec column)] [row (inc column)] [(inc row) column]])

(defn open-square? [square world]
  (= (world square) \.))

(defn occupied? [square units]
  (some #(= (:location %) square) units))

(defn open-squares-in-range [{:keys [location]} units world]
  (for [sq (adjacent-squares location)
        :when (and (open-square? sq world) (not (occupied? sq units)))]
    sq))

(defn neighbors-to-next-step [from-square to-square neighbors]
  (loop [current-square to-square
         previous nil]
    (if (= current-square from-square)
      previous
      (recur (neighbors current-square) current-square))))

(defn shortest-path [from-square to-square units world]
  (loop [queue (deque [from-square 0])
         visited #{from-square}
         previous {}]
    (if-let [[current-square distance] (peek-first queue)]
      (do
        (if (= current-square to-square)
          {:shortestDistance distance,
           :next-square      (neighbors-to-next-step from-square to-square previous),
           :to-square        to-square}
          (let [neighbors (filter #(and (open-square? % world) (not (occupied? % units)) (not (contains? visited %)))
                                  (adjacent-squares current-square))]
            (recur (reduce #(add-last %1 %2) (remove-first queue) (for [square neighbors] [square (inc distance)]))
                   (into visited neighbors)
                   (into previous (for [square neighbors] [square current-square])))))))))

(defn next-move [unit-to-move units world]
  (let [next-square (->> (identify-targets (:type unit-to-move) units)
                         (mapcat #(open-squares-in-range % units world))
                         (map #(shortest-path (:location unit-to-move) % units world))
                         (filter some?)
                         (sort-by (juxt :shortestDistance :to-square))
                         (first)
                         (:next-square))]
    (or next-square (:location unit-to-move))))

(defn ids-of-targets-in-range [unit-id id->unit]
  (let [unit (id->unit unit-id)
        adjacent (into (hash-set) (adjacent-squares (:location unit)))]
    (->> id->unit
         (filter (fn [[_ u]] (and (contains? adjacent (:location u)) (not= (:type u) (:type unit)))))
         (map (fn [[id _]] id)))))

(defn is-in-range-of-any-target? [id id->unit]
  (not-empty (ids-of-targets-in-range id id->unit)))

(defn move-unit [id id->unit world]
  (if (is-in-range-of-any-target? id id->unit)
    id->unit
    (let [unit (id->unit id)
          move (next-move unit (vals id->unit) world)
          moved-unit (assoc unit :location move)]
      (assoc id->unit id moved-unit))))

(defn attack [id id->unit]
  (let [target-ids (ids-of-targets-in-range id id->unit)
        ordered (sort-by (juxt :hit-points :location) target-ids)]
    (if (empty? ordered)
      id->unit
      )))

(defn move-units [world start-id->unit]
  (loop [ids (turn-order start-id->unit)
         id->unit start-id->unit]
    (if (empty? ids)
      id->unit
      (recur (rest ids) (move-unit (first ids) id->unit world)))))

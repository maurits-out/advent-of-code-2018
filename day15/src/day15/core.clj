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
       (map-indexed (fn [idx [c ch]] {:id idx :location c, :type ch, :hit-points 200}))))

(defn replace-unit [ch]
  (case ch
    (\E \G) \.
    ch))

(defn replace-units [world]
  (w/walk (fn [[c ch]] [c (replace-unit ch)]) identity world))

(defn turn-order [units]
  (->> (sort-by :location units)
       (map :id)))

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

(defn targets-in-range [unit all-units]
  (let [adjacent (into (hash-set) (adjacent-squares (:location unit)))]
    (filter #(and (contains? adjacent (:location %)) (not= (:type %) (:type unit))) all-units)))

(defn find-unit-by-id [id units]
  (first (filter #(= id (:id %)) units)))

(defn alive? [id units]
  (find-unit-by-id id units))

(defn move-unit [id all-units world]
  (let [unit-to-move (find-unit-by-id id all-units)]
    (if (not-empty (targets-in-range unit-to-move all-units))
      all-units
      (let [move (next-move unit-to-move all-units world)
            moved-unit (assoc unit-to-move :location move)]
        (conj (disj all-units unit-to-move) moved-unit)))))

(defn attack-unit [unit attack-power]
  (let [updated-hit-points (- (:hit-points unit) attack-power)]
    (assoc unit :hit-points updated-hit-points)))

(defn attack [id all-units attack-powers]
  (let [attacking-unit (find-unit-by-id id all-units)
        targets (sort-by (juxt :hit-points :location) (targets-in-range attacking-unit all-units))
        attack-power (get attack-powers (:type attacking-unit) 3)]
    (if (empty? targets)
      all-units
      (let [unit-before-attack (first targets)
            unit-after-attack (attack-unit unit-before-attack attack-power)]
        (if (<= (:hit-points unit-after-attack) 0)
          (disj all-units unit-before-attack)
          (conj (disj all-units unit-before-attack) unit-after-attack))))))

(defn move-units [world start-units]
  (loop [ids (turn-order start-units)
         units (into (hash-set) start-units)]
    (if (empty? ids)
      units
      (recur (rest ids) (move-unit (first ids) units world)))))

(defn combat-ends? [units]
  (= (count (distinct (map :type units))) 1))

(defn do-round [world start-units attack-powers]
  (loop [ids (turn-order start-units)
         units (into (hash-set) start-units)]
    (cond
      (empty? ids) {:round-completed true, :updated-units units}
      (combat-ends? units) {:round-completed false, :updated-units units}
      :else (if (empty? ids)
              units
              (let [id (first ids)]
                (if (alive? id units)
                  (recur (rest ids) (attack id (move-unit id units world) attack-powers))
                  (recur (rest ids) units)))))))

(defn count-elves [units]
  (count (filter #(= (:type %) \E) units)))

(defn play [world start-units attack-powers]
  (loop [units start-units
         completed-rounds 0]
    (let [{:keys [round-completed updated-units]} (do-round world units attack-powers)]
      (if round-completed
        (recur updated-units (inc completed-rounds))
        {:outcome (* completed-rounds (apply + (map :hit-points updated-units))),
         :elves   (count-elves updated-units)}))))

(defn part1 [world start-units]
  (let [{:keys [outcome]} (play world start-units {})]
    outcome))

(defn part2 [world start-units]
  (let [original-elf-count (count-elves start-units)]
    (loop [attack-power-elves 34]
      (let [{:keys [outcome elves]} (play world start-units {\E attack-power-elves})]
        (if (= elves original-elf-count)
          outcome
          (recur (inc attack-power-elves)))))))

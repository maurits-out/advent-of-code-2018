(ns day20.core
  (:require [data.deque :refer [deque add-first add-last remove-first peek-first]]))

(def char->direction {\E [1 0], \S [0 1], \W [-1 0], \N [0 -1]})

(defn move [location step]
  (mapv + location (char->direction step)))

(defn update-distance [distances [location doors]]
  (update distances location #(if % (min % doors) doors)))

(defn move-positions [current-positions step]
  (into (hash-map) (for [[location doors] current-positions]
                     [(move location step) (inc doors)])))

(defn update-distances [distances positions]
  (reduce #(update-distance %1 %2) distances positions))

(defn follow [{:keys [distances current-positions starts ends groups] :as state} step]
  (case step
    (\E \S \W \N) (let [new-positions (move-positions current-positions step)]
                    (assoc state
                      :distances (update-distances distances new-positions)
                      :current-positions new-positions))
    \( (assoc state
         :starts current-positions
         :ends {}
         :groups (add-first groups [starts ends]))
    \| (assoc state
         :current-positions starts
         :ends (merge-with min ends current-positions))
    \) (assoc state
         :current-positions (merge-with min current-positions ends)
         :starts (first (peek-first groups))
         :ends (second (peek-first groups))
         :groups (remove-first groups))
    (\$ \^) state))

(defn initial-state []
  {:distances         {[0 0] 0}
   :current-positions {[0 0] 0}
   :starts            {[0 0] 0}
   :ends              {}
   :groups            (deque)})

(defn follow-route [route]
  (reduce #(follow %1 %2) (initial-state) route))

(defn solve [route]
  (let [distances (vals (:distances (follow-route route)))]
    {:part1 (apply max distances)
     :part2 (count (filter #(>= % 1000) distances))}))

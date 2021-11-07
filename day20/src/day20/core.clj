(ns day20.core
  (:require [data.deque :refer [deque add-first add-last remove-first peek-first]]))

(def origin [0 0])

(def char->direction {\E [1 0], \S [0 1], \W [-1 0], \N [0 -1]})

(defn move [location step]
  (mapv + location (char->direction step)))

(defn update-distance [distances [location doors]]
  (if (distances location)
    (update distances location #(min % doors))
    (assoc distances location doors)))

(defn update-distances [distances current-positions step]
  (let [updated-positions (map (fn [[location doors]] [(move location step) (inc doors)]) current-positions)]
    (reduce #(update-distance %1 %2) distances updated-positions)))

(defn follow [{:keys [distances current-positions starts ends stack] :as state} step]
  (case step
    (\E \S \W \N) (assoc state
                    :distances (update-distances distances current-positions step)
                    :current-positions (map (fn [[location doors]] [(move location step) (inc doors)]) current-positions))
    \( (assoc state
         :starts current-positions
         :ends #{}
         :stack (add-first stack [starts ends]))
    \| (assoc state
         :current-positions starts
         :ends (into ends current-positions))
    \) (assoc state
         :current-positions (into current-positions ends)
         :starts (first (peek-first stack))
         :ends (second (peek-first stack))
         :stack (remove-first stack))
    (\$ \^) state))

(defn initial-state []
  {:distances         {origin 0}
   :current-positions #{[origin 0]}
   :starts            #{[origin 0]}
   :ends              #{}
   :stack             (deque)})

(defn follow-route [route]
  (reduce #(follow %1 %2) (initial-state) route))

(defn solve [route]
  (let [distances (vals (:distances (follow-route route)))]
    {:part1 (apply max distances)
     :part2 (count (filter #(>= % 1000) distances))}))

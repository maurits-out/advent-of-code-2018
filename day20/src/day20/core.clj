(ns day20.core
  (:require [data.deque :refer [deque add-first add-last remove-first peek-first]]))

(def origin [0 0])

(def char->direction {\E [1 0], \S [0 1], \W [-1 0], \N [0 -1]})

(defn move [pos step]
  (mapv + pos (char->direction step)))

(defn add-edges [graph current-positions step]
  (reduce #(update %1 %2 (fnil conj #{}) (move %2 step)) graph current-positions))

(defn follow [{:keys [graph current-positions starts ends stack] :as state} step]
  (case step
    (\E \S \W \N) (assoc state
                    :graph (add-edges graph current-positions step)
                    :current-positions (set (mapv #(move % step) current-positions)))
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
  {:graph {} :current-positions #{origin} :starts #{origin} :ends #{} :stack (deque)})

(defn follow-route [route]
  (reduce #(follow %1 %2) (initial-state) route))

(defn count-doors [graph destination]
  (loop [queue (deque [origin 0])
         visited #{origin}]
    (let [[location distance] (peek-first queue)]
      (if (= location destination)
        distance
        (recur
          (reduce #(add-last %1 [%2 (inc distance)]) (remove-first queue) (graph location))
          (into visited (graph location)))))))

(defn find-largest-numbers-of-doors [route]
  (let [graph (:graph (follow-route route))
        reachable-rooms (distinct (apply concat (vals graph)))]
    (apply max (for [r reachable-rooms]
                 (count-doors graph r)))))

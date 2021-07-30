(ns day07.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(def num-workers 5)
(def time-offset 60)

(defn parse-input [input]
  (for [line (string/split-lines input)]
    [(nth line 36) (nth line 5)]))

(defn add-dependency-to-map [map [step depends-on]]
  (update-in map [step] (fnil conj #{}) depends-on))

(defn create-dependency-map [entries]
  (reduce add-dependency-to-map {} entries))

(defn steps [dependencies]
  (set/difference (apply set/union (vals dependencies)) (set (keys dependencies))))

(defn remove-step [dependencies step]
  (for [[k v] dependencies :when (not= k step)] [k (disj v step)]))

(defn find-new-candidates [dependencies]
  (set (for [[k v] dependencies :when (empty? v)] k)))

(defn part1 [dependencies]
  (loop [dependencies dependencies
         steps (steps dependencies)
         order []]
    (if (empty? steps)
      (string/join order)
      (let [step (first (sort steps))
            updated-dependencies (remove-step dependencies step)
            updated-candidates (set/union (disj steps step) (find-new-candidates updated-dependencies))]
        (recur updated-dependencies updated-candidates (conj order step))))))

(defn time-to-complete [step]
  (+ (Character/compare step \A) time-offset 1))

(defn initial-state [dependencies]
  (let [steps (steps dependencies)
        steps-to-allocate (take num-workers (sort steps))]
    {:waiting   (set/difference steps steps-to-allocate),
     :allocated (into (hash-map) (map (fn [step] [step (time-to-complete step)]) steps-to-allocate))
     :elapsed   0}))

(defn next-entry-to-complete [allocated]
  (apply min-key second allocated))

(defn collect-all-candidates [state new-candidates]
  (set/difference (set/union (:waiting state) new-candidates) (set (keys (:allocated state)))))

(defn select-steps-to-allocate [state all-candidates]
  (take (inc (- num-workers (count (:allocated state)))) (sort all-candidates)))

(defn create-new-allocated [state step-to-remove seconds-to-complete to-allocate]
  (merge
    (into (hash-map) (for [[s t] (:allocated state)
                           :when (not= s step-to-remove)]
                       [s (- t seconds-to-complete)]))
    (into (hash-map) (for [s to-allocate]
                       [s (time-to-complete s)]))))

(defn update-state [state new-candidates]
  (let [current-allocated-steps (:allocated state)
        [step-to-complete seconds-to-complete] (next-entry-to-complete current-allocated-steps)
        all-candidates (collect-all-candidates state new-candidates)
        new-steps-to-allocate (select-steps-to-allocate state all-candidates)
        next-allocated (create-new-allocated state step-to-complete seconds-to-complete new-steps-to-allocate)]
    {:waiting   (set/difference all-candidates new-steps-to-allocate)
     :allocated next-allocated
     :elapsed   (+ (:elapsed state) seconds-to-complete)}))

(defn part2 [dependencies]
  (loop [dependencies dependencies
         state (initial-state dependencies)]
    (if (empty? (:allocated state))
      (:elapsed state)
      (let [next-step (first (next-entry-to-complete (:allocated state)))
            updated-dependencies (remove-step dependencies next-step)
            new-candidates (find-new-candidates updated-dependencies)
            next-state (update-state state new-candidates)]
        (recur updated-dependencies next-state)))))

(defn -main []
  (let [input (slurp (io/resource "input.txt"))
        dependencies (create-dependency-map (parse-input input))]
    (println "Part 1:" (part1 dependencies))
    (println "Part 2:" (part2 dependencies))))

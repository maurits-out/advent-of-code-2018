(ns day08.core
  (:require [clojure.string :as string]))

(defn parse-input [input]
  (map #(Integer/parseInt %) (string/split input #" ")))

(declare sum-of-metadata-entries)

(defn sum-of-metadata-entries
  ([numbers acc]
   (let [[num-child-nodes num-metadata-entries & rest] numbers
         [sum remaining] (sum-of-metadata-entries rest num-child-nodes acc)
         [xs ys] (split-at num-metadata-entries remaining)]
     [(apply + (cons sum xs)) ys]))
  ([numbers num-nodes acc]
   (if (zero? num-nodes)
     [acc numbers]
     (let [[sum remaining] (sum-of-metadata-entries numbers acc)]
       (sum-of-metadata-entries remaining (dec num-nodes) sum)))))

(defn part1 [input]
  (first (sum-of-metadata-entries (parse-input input) 0)))

(ns day08.core
  (:require [clojure.string :as string]))

(defn parse-input [input]
  (map #(Integer/parseInt %) (string/split input #" ")))

(defn sum-of-metadata-entries
  ([numbers acc]
   (let [[num-child-nodes num-metadata-entries & rest] numbers
         [remaining updated-acc] (sum-of-metadata-entries rest num-child-nodes acc)
         [xs ys] (split-at num-metadata-entries remaining)]
     [ys (apply + (cons updated-acc xs))]))
  ([numbers num-nodes acc]
   (if (zero? num-nodes)
     [numbers acc]
     (let [[remaining updated-acc] (sum-of-metadata-entries numbers acc)]
       (sum-of-metadata-entries remaining (dec num-nodes) updated-acc)))))

(defn part1 [input]
  (second (sum-of-metadata-entries (parse-input input) 0)))

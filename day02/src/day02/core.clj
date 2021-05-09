(ns day02.core
  (:require [clojure.string :as string]))

(defn parse-input [input]
  (string/split-lines input))

(defn has-any-letter-count-fn [count]
  (fn [id] (some #{count} (vals (frequencies id)))))

(defn count-ids [ids letter-count]
  (let [has-any-letter-count (has-any-letter-count-fn letter-count)]
    (count (filter #(has-any-letter-count %) ids))))

(defn calculate-checksum [x y]
  (* x y))

(defn solve1 [input]
  (let [ids (parse-input input)]
    (calculate-checksum (count-ids ids 2) (count-ids ids 3))))

(ns day05.core
  (:require [clojure.string :as string]))

(defn react? [u v]
  (= (Math/abs (- (int u) (int v))) 32))

(defn less-than-two-units-remaining? [p i]
  (>= i (dec (count p))))

(defn react-units [p i]
  (str (subs p 0 i) (subs p (+ i 2))))

(defn react [polymer]
  (loop [i 0 p polymer]
    (if (less-than-two-units-remaining? p i)
      (count p)
      (if (react? (get p i) (get p (inc i)))
        (recur (max 0 (dec i)) (react-units p i))
        (recur (inc i) p)))))

(defn extract-unit-types [polymer]
  (set (seq (string/lower-case polymer))))

(defn remove-units [polymer type]
  (string/join (filter (fn [^Character u] (not= (Character/toLowerCase u) type)) (seq polymer))))

(defn length-of-shortest-polymer [polymer]
  (let [unit-types (extract-unit-types polymer)
        remaining-polymers (map #(remove-units polymer %) unit-types)
        lengths (map #(react %) remaining-polymers)]
    (apply min lengths)))

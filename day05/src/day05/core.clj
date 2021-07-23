(ns day05.core
  (:require [clojure.string :as string]))

(defn react? [u v]
  (= (Math/abs (- (int u) (int v))) 32))

(defn react [polymer]
  (loop [res '()
         rem polymer]
    (let [[u v] (take 2 rem)]
      (if (nil? u)
        (count res)
        (if (nil? v)
          (inc (count res))
          (if (react? u v)
            (if (empty? res)
              (recur res (drop 2 rem))
              (recur (rest res) (cons (first res) (drop 2 rem))))
            (recur (cons u res) (rest rem))))))))

(defn extract-unit-types [polymer]
  (distinct (string/lower-case polymer)))

(defn remove-units [polymer ^Character type]
  (let [pair #{type (Character/toUpperCase type)}]
    (filter (fn [u] (not (contains? pair u))) polymer)))

(defn length-of-shortest-polymer [polymer]
  (let [unit-types (extract-unit-types polymer)
        remaining-polymers (map #(remove-units polymer %) unit-types)
        lengths (map #(react %) remaining-polymers)]
    (apply min lengths)))

(ns day14.core
  (:require [clojure.string :as string]))

(defn initial-state []
  {:score-board [3 7] :pos-elf-1 0 :pos-elf-2 1})

(defn update-elf-position [recipe current-position score-board-size]
  (rem (+ current-position recipe 1) score-board-size))

(defn next-recipe [{:keys [score-board pos-elf-1 pos-elf-2]}]
  (let [recipe-elf-1 (nth score-board pos-elf-1)
        recipe-elf-2 (nth score-board pos-elf-2)
        recipe-sum (+ recipe-elf-1 recipe-elf-2)
        digits-of-sum (if (< recipe-sum 10) [recipe-sum] [1 (rem recipe-sum 10)])
        updated-score-board (apply conj score-board digits-of-sum)
        next-pos-elf-1 (update-elf-position recipe-elf-1 pos-elf-1 (count updated-score-board))
        next-pos-elf-2 (update-elf-position recipe-elf-2 pos-elf-2 (count updated-score-board))]
    {:score-board updated-score-board :pos-elf-1 next-pos-elf-1 :pos-elf-2 next-pos-elf-2}))

(defn generate-recipes-while [predicate]
  (loop [[current & states] (iterate next-recipe (initial-state))]
    (if (predicate (:score-board current))
      (recur states)
      (:score-board current))))

(defn count-recipes-scores-after [input]
  (->> (generate-recipes-while #(< (count %) (+ input 10)))
       (drop input)
       (take 10)
       (string/join)))

(defn match? [input score-board start]
  (let [end (+ start (count input))]
    (and (>= start 0) (= (subvec score-board start end) input) start)))

(defn index-of-match [input score-board]
  (let [start (- (count score-board) (count input))]
    (or (match? input score-board start) (match? input score-board (dec start)))))

(defn count-recipes-to-the-left [input]
  (->> (generate-recipes-while #(not (index-of-match input %)))
       (index-of-match input)))

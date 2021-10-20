(ns day16.core
  (:require [clojure.string :as string]))

(def opcodes
  {:addr (fn [reg a b] (+ (get reg a) (get reg b)))
   :addi (fn [reg a b] (+ (get reg a) b))
   :mulr (fn [reg a b] (* (get reg a) (get reg b)))
   :muli (fn [reg a b] (* (get reg a) b))
   :banr (fn [reg a b] (bit-and (get reg a) (get reg b)))
   :bani (fn [reg a b] (bit-and (get reg a) b))
   :borr (fn [reg a b] (bit-or (get reg a) (get reg b)))
   :bori (fn [reg a b] (bit-or (get reg a) b))
   :setr (fn [reg a _] (get reg a))
   :seti (fn [_ a _] a)
   :gtir (fn [reg a b] (if (> a (get reg b)) 1 0))
   :gtri (fn [reg a b] (if (> (get reg a) b) 1 0))
   :gtrr (fn [reg a b] (if (> (get reg a) (get reg b)) 1 0))
   :eqir (fn [reg a b] (if (= a (get reg b)) 1 0))
   :eqri (fn [reg a b] (if (= (get reg a) b) 1 0))
   :eqrr (fn [reg a b] (if (= (get reg a) (get reg b)) 1 0))})

(defn parse-values-between-brackets [line]
  (let [group (second (re-matches #".+ \[(.+)\]" line))]
    (mapv #(Integer/parseInt %) (string/split group #", "))))

(defn parse-sample [sample-text]
  (let [[before instruction after] (string/split-lines sample-text)]
    {:before      (parse-values-between-brackets before),
     :instruction (mapv #(Integer/parseInt %) (string/split instruction #" "))
     :after       (parse-values-between-brackets after)}))

(defn parse-samples [input]
  (for [s (string/split input #"\n\n")] (parse-sample s)))

(defn evaluate [reg a b c fn]
  (assoc reg c (apply fn [reg a b])))

(defn match? [{:keys [before instruction after]} fn]
  (let [[_ a b c] instruction]
    (= after (evaluate before a b c fn))))

(defn matching-ops [{:keys [instruction] :as sample}]
  {:opcode     (first instruction)
   :candidates (for [[k fn] opcodes :when (match? sample fn)] k)})

(defn count-matching-opcodes [sample]
  (->> (matching-ops sample)
       :candidates
       (count)))

(defn count-samples-part1 [input]
  (->> (parse-samples input)
       (filter #(>= (count-matching-opcodes %) 3))
       (count)))

(defn run-test-program-part2 [input]
  [1 1 1 1])
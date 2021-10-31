(ns day19.core
  (:require [clojure.string :as string]))

(def op-fn
  {"addr" (fn [reg a b] (+ (get reg a) (get reg b)))
   "addi" (fn [reg a b] (+ (get reg a) b))
   "mulr" (fn [reg a b] (* (get reg a) (get reg b)))
   "muli" (fn [reg a b] (* (get reg a) b))
   "banr" (fn [reg a b] (bit-and (get reg a) (get reg b)))
   "bani" (fn [reg a b] (bit-and (get reg a) b))
   "borr" (fn [reg a b] (bit-or (get reg a) (get reg b)))
   "bori" (fn [reg a b] (bit-or (get reg a) b))
   "setr" (fn [reg a _] (get reg a))
   "seti" (fn [_ a _] a)
   "gtir" (fn [reg a b] (if (> a (get reg b)) 1 0))
   "gtri" (fn [reg a b] (if (> (get reg a) b) 1 0))
   "gtrr" (fn [reg a b] (if (> (get reg a) (get reg b)) 1 0))
   "eqir" (fn [reg a b] (if (= a (get reg b)) 1 0))
   "eqri" (fn [reg a b] (if (= (get reg a) b) 1 0))
   "eqrr" (fn [reg a b] (if (= (get reg a) (get reg b)) 1 0))})

(defn parse-instruction [instruction]
  (let [[op & args] (string/split instruction #" ")]
    (into [op] (mapv #(Integer/parseInt %) args))))

(defn parse-input [input]
  (let [lines (string/split-lines input)]
    {:ip-reg  (Character/digit ^char (last (first lines)) 10),
     :program (mapv parse-instruction (next lines))}))

(defn evaluate [reg [op a b c]]
  (assoc reg c (apply (op-fn op) [reg a b])))

(defn apply-instruction [{:keys [ip reg]} ip-reg instruction]
  (let [updated-reg (evaluate (assoc reg ip-reg ip) instruction)
        updated-ip (inc (get updated-reg ip-reg))]
    {:ip updated-ip :reg updated-reg}))

(defn ip-out-of-range? [ip program]
  (or (< ip 0) (>= ip (count program))))

(defn execute-part-1 [{:keys [ip-reg program]}]
  (loop [state {:ip 0, :reg [0 0 0 0 0 0]}]
    (if (ip-out-of-range? (:ip state) program)
      (get (:reg state) 0)
      (recur (apply-instruction state ip-reg (get program (:ip state)))))))

(defn factors [n]
  (into (hash-set)
        (apply concat (for [x (range 1 (inc (Math/sqrt n))) :when (zero? (rem n x))]
                        [x (quot n x)]))))

(defn sum-of-factors [n]
  (apply + (factors n)))

(defn take-short-cut [reg]
  (let [sum (sum-of-factors (get reg 2))
        updated-reg (assoc reg
                      0 sum
                      3 15)]
    {:ip 16, :reg updated-reg}))

(defn update-state [state ip-reg instruction]
  (if (= (:ip state) 1)
    (take-short-cut (:reg state))
    (apply-instruction state ip-reg instruction)))

(defn execute-part-2 [{:keys [ip-reg program]}]
  (loop [state {:ip 0, :reg [1 0 0 0 0 0]}]
    (if (ip-out-of-range? (:ip state) program)
      (get (:reg state) 0)
      (recur (update-state state ip-reg (get program (:ip state)))))))

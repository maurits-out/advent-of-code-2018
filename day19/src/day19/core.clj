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

(defn initial-state [ip-reg]
  {:ip 0, :ip-reg ip-reg, :reg [0 0 0 0 0 0]})

(defn evaluate [reg [op a b c]]
  (assoc reg c (apply (op-fn op) [reg a b])))

(defn write-ip-to-reg [ip ip-reg reg]
  (assoc reg ip-reg ip))

(defn get-ip-from-reg [ip-reg reg]
  (get reg ip-reg))

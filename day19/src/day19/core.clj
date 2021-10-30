(ns day19.core
  (:require [clojure.string :as string]))

(defn parse-input [input]
  (let [lines (string/split-lines input)
        ip-reg (Character/digit ^char (last (first lines)) 10)
        program (vec (next lines))]
    {:ip-reg ip-reg,
     :program program}))

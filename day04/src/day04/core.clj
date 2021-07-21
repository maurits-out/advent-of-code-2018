(ns day04.core
  (:require [clojure.string :as string]))

(def regex-falls-asleep #"\[(.{10}) (\d{2}):(\d{2})\] falls asleep")
(def regex-falls-wakes-up #"\[(.{10}) (\d{2}):(\d{2})\] wakes up")
(def regex-falls-begins-shift #"\[(.{10}) (\d{2}):(\d{2})\] Guard #(\d+) begins shift")

(defn parse-int [s] (Integer/parseInt s))

(defn parse-record [line]
  (if-let [[_ day hour minutes] (re-matches regex-falls-asleep line)]
    {:day day, :hour hour, :minutes (parse-int minutes), :type :fall-asleep}
    (if-let [[_ day hour minutes] (re-matches regex-falls-wakes-up line)]
      {:day day, :hour hour, :minutes (parse-int minutes), :type :wakes-up}
      (let [[_ day hour minutes guard] (re-matches regex-falls-begins-shift line)]
        {:day day, :hour hour, :minutes (parse-int minutes), :type :start-shift, :guard (parse-int guard)}))))

(defn parse-records [input]
  (let [lines (string/split-lines input)]
    (sort-by (juxt :day :hour :minutes) (mapv parse-record lines))))

(defmulti replay (fn [_ record] (:type record)))
(defmethod replay :start-shift [state {:keys [guard]}]
  (assoc state :guard guard))
(defmethod replay :fall-asleep [state {:keys [minutes]}]
  (assoc state :asleep-since minutes))
(defmethod replay :wakes-up [{:keys [guard asleep-since minutes-asleep-by-guard]} {:keys [minutes]}]
  (let [minute-range (range asleep-since minutes)
        minutes-asleep (get minutes-asleep-by-guard guard [])]
    {:guard                   guard,
     :minutes-asleep-by-guard (assoc minutes-asleep-by-guard guard (concat minutes-asleep minute-range))}))

(defn replay-records [records]
  (reduce replay {} records))

(defn find-guard-with-most-minutes-asleep [asleep]
  (key (apply max-key (comp count val) asleep)))

(defn find-minute-most-asleep [minutes]
  (key (apply max-key val (frequencies minutes))))

(defn solve1 [input]
  (let [records (parse-records input)
        minutes-asleep-by-guard (:minutes-asleep-by-guard (replay-records records))
        guard (find-guard-with-most-minutes-asleep minutes-asleep-by-guard)
        minute (find-minute-most-asleep (get minutes-asleep-by-guard guard))]
    (* guard minute)))

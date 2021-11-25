(ns day21.core)

(defn update-r2 [r2 r5]
  (->> r5
    (bit-and r5 255)
    (+ r2)
    (bit-and 16777215)
    (* 65899)
    (bit-and 16777215)))

(defn f [start-r2]
  (loop [r2 16123384
         r5 (bit-or start-r2 65536)]
    (if (< r5 256)
      {:r2 (update-r2 r2 r5), :r5 r5}
      (recur (update-r2 r2 r5) (quot r5 256)))))

(defn part1-new []
  (:r2 (f 0)))

(defn part2-new []
  (loop [{:keys [r2 r5]} (f 0)
         seen #{}]
    (do
      (println r2 r5)
      (if (contains? seen r5)
        r2
        (recur (f r2) (conj seen r5))))))

(ns day22.core)

(defn coordinates [tx ty]
  (for [x (range 0 (inc tx))
        y (range 0 (inc ty))]
    [x y]))

(defn erosion-levels [[tx ty] depth]
  (reduce
    (fn [m [x y]] (assoc m [x y] (let [g (cond
                                           (or (= 0 x y) (and (= x tx) (= y ty))) 0
                                           (= 0 x) (* 48271 y)
                                           (= 0 y) (* 16807 x)
                                           :else (* (m [(dec x) y]) (m [x (dec y)])))]
                                   (rem (+ g depth) 20183))))
    {}
    (coordinates tx ty)))

(defn total-risk-level [target depth]
  (->>
    (erosion-levels target depth)
    vals
    (map #(rem % 3))
    (apply +)))

(defn -main []
  (println "Part 1:" (total-risk-level [12 757] 3198)))

(ns day22.core)

(defn coordinates [[tx ty]]
  (for [x (range 0 (inc tx))
        y (range 0 (inc ty))]
    [x y]))

(defn erosion-level-fn [[tx ty] depth]
  (let [erosion-level (fn [mem-erosion-level [x y]]
                        (let [erosion-level (fn [c] (mem-erosion-level mem-erosion-level c))
                              g (cond
                                  (or (= 0 x y) (and (= x tx) (= y ty))) 0
                                  (= 0 x) (* 48271 y)
                                  (= 0 y) (* 16807 x)
                                  :else (* (erosion-level [(dec x) y]) (erosion-level [x (dec y)])))]
                          (rem (+ g depth) 20183)))
        mem-erosion-level (memoize erosion-level)]
    (partial mem-erosion-level mem-erosion-level)))

(defn part1 [target depth]
  (let [erosion-level (erosion-level-fn target depth)]
    (->>
      (coordinates target)
      (map erosion-level)
      (map #(rem % 3))
      (apply +))))

(defn -main []
  (println "Part 1:" (part1 [12 757] 3198)))

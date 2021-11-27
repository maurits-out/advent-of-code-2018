(ns day22.core)

(defn coordinates [[tx ty]]
  (for [x (range 0 (inc tx))
        y (range 0 (inc ty))]
    [x y]))

;; https://stackoverflow.com/questions/3906831/how-do-i-generate-memoized-recursive-functions-in-clojure
(defn erosion-level-fn [[tx ty] depth]
  (let [erosion-level (fn [mem-erosion-level [x y]]
                        (let [erosion-level (partial mem-erosion-level mem-erosion-level)
                              g (cond
                                  (= 0 x y) 0
                                  (and (= x tx) (= y ty)) 0
                                  (= 0 x) (* 48271 y)
                                  (= 0 y) (* 16807 x)
                                  :else (* (erosion-level [(dec x) y]) (erosion-level [x (dec y)])))]
                          (rem (+ g depth) 20183)))
        mem-erosion-level (memoize erosion-level)]
    (partial mem-erosion-level mem-erosion-level)))

(defn region-type-fn [target depth]
  (comp #(rem % 3) (erosion-level-fn target depth)))

(defn part1 [target depth]
  (->>
    (coordinates target)
    (map (region-type-fn target depth))
    (apply +)))

(defn -main []
  (println "Part 1:" (part1 [12 757] 3198)))

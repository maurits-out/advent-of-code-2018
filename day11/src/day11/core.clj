(ns day11.core)

(def grid-size 300)
(def square-size 3)

(defn power-level [x y grid-serial-number]
  (let [rack-id (+ x 10)
        pl1 (* y rack-id)
        pl2 (+ pl1 grid-serial-number)
        pl3 (* pl2 rack-id)
        pl4 (rem (quot pl3 100) 10)]
    (- pl4 5)))

(defn square-power [left-x top-y grid-serial-number]
  (apply + (for [x (range left-x (+ left-x square-size))
                 y (range top-y (+ top-y square-size))]
             (power-level x y grid-serial-number))))

(defn square-coordinates []
  (let [r (range (- grid-size (dec square-size)))]
    (for [x r, y r] [x y])))

(defn square-powers [grid-serial-number]
  (into (hash-map)
        (for [[x y] (square-coordinates)]
          [[x y] (square-power x y grid-serial-number)])))

(defn part1 [grid-serial-number]
  (key (apply max-key val (square-powers grid-serial-number))))

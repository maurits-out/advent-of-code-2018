(ns day05.core)

(defn react? [u v]
  (= (Math/abs (- (int u) (int v))) 32))

(defn less-than-two-units-remaining? [p i]
  (>= i (dec (count p))))

(defn remove-two-units [p i]
  (str (subs p 0 i) (subs p (+ i 2))))

(defn react [polymer]
  (loop [i 0 p polymer]
    (if (less-than-two-units-remaining? p i)
      (count p)
      (if (react? (get p i) (get p (inc i)))
        (recur (max 0 (dec i)) (remove-two-units p i))
        (recur (inc i) p)))))

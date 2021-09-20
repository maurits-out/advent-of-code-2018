(ns day09.core)

(defn create-node [value]
  (transient {:value value}))

(defn initial-circle []
  (let [node (create-node 0)]
    (assoc! node :next node)
    (assoc! node :prev node)))

(defn add-marble [current marble]
  (let [node (create-node marble)
        first (:next current)
        second (:next first)]
    (assoc! node :next second)
    (assoc! node :prev first)
    (assoc! first :next node)
    (assoc! second :prev node)
    node))

(defn delete-marble [current]
  (let [to-delete (reduce (fn [node _] (:prev node)) current (range 7))
        before (:prev to-delete)
        after (:next to-delete)]
    (assoc! before :next after)
    (assoc! after :prev before)
    {:new-current after, :deleted-marble (:value to-delete)}))

(defn winning-score [scoreboard]
  (apply max (vals scoreboard)))

(defn game-ends? [marble highest]
  (> marble highest))

(defn next-marble [marble]
  (inc marble))

(defn update-player-score [current-score marble-to-play deleted-marble]
  (+ (or current-score 0) marble-to-play deleted-marble))

(defn play [player-count highest-marble]
  (loop [current-marble (initial-circle)
         scoreboard {}
         marble-to-play 1
         players (cycle (range 1 (inc player-count)))]
    (if (game-ends? marble-to-play highest-marble)
      (winning-score scoreboard)
      (if (zero? (rem marble-to-play 23))
        (let [{:keys [new-current deleted-marble]} (delete-marble current-marble)
              updated-scoreboard (update scoreboard (first players) #(update-player-score % marble-to-play deleted-marble))]
          (recur new-current updated-scoreboard (next-marble marble-to-play) (rest players)))
        (recur (add-marble current-marble marble-to-play) scoreboard (next-marble marble-to-play) (rest players))))))

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

(defn play [player-count highest-marble]
  (loop [current-marble (initial-circle)
         scoreboard {}
         [marble-to-play & remaining-marbles] (range 1 (inc highest-marble))
         [current-player & players] (cycle (range 1 (inc player-count)))]
    (if marble-to-play
      (if (zero? (rem marble-to-play 23))
        (let [{:keys [new-current deleted-marble]} (delete-marble current-marble)
              update-player-score-fn #(+ (or % 0) marble-to-play deleted-marble)
              updated-scoreboard (update scoreboard current-player update-player-score-fn)]
          (recur new-current updated-scoreboard remaining-marbles players))
        (recur (add-marble current-marble marble-to-play) scoreboard remaining-marbles players))
      (apply max (vals scoreboard)))))

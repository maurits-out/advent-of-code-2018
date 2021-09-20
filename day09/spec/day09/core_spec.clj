(ns day09.core-spec
  (:require [speclj.core :refer :all]
            [day09.core :refer :all]))

(defn add-marbles [& marbles]
  (reduce #(add-marble %1 %2) (initial-circle) marbles))

(describe "Day 9"

  (context "Circle"

    (it "creates initial circle"
      (let [current (initial-circle)]
        (should= 0 (:value current))
        (should= current (:next current))
        (should= current (:prev current))))

    (it "can add the first marble"
      (let [current (initial-circle)
            new-current (add-marble current 1)]
        (should= 1 (:value new-current))
        (should= current (:prev new-current))
        (should= current (:next new-current))
        (should= new-current (:next current))
        (should= new-current (:prev current))))

    (it "can add the second marble"
      (let [current (add-marbles 1 2)]
        (should= 2 (:value current))
        (should= 0 (:value (:prev current)))
        (should= 1 (:value (:next current)))))

    (it "can add the third marble"
      (let [current (add-marbles 1 2 3)]
        (should= 3 (:value current))
        (should= 1 (:value (:prev current)))
        (should= 0 (:value (:next current)))
        (should= 2 (:value (:next (:next current))))))

    (it "can delete a marble"
      (let [circle (add-marbles 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22)
            {:keys [new-current deleted-marble]} (delete-marble circle)]
        (should= 19 (:value new-current))
        (should= 18 (:value (:prev new-current)))
        (should= 19 (:value (:next (:prev new-current))))
        (should= 9 deleted-marble)))

    (it "can solve the examples"
      (should= 32 (play 9 25))
      (should= 8317 (play 10 1618))
      (should= 146373 (play 13 7999))
      (should= 2764 (play 17 1104))
      (should= 54718 (play 21 6111))
      (should= 37305 (play 30 5807)))

    (it "can solve part 1"
      (should= 374287 (play 468 71010)))

    (it "can solve part 2"
      (should= 3083412635 (play 468 (* 71010 100))))))

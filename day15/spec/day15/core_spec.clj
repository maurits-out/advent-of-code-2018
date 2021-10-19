(ns day15.core-spec
  (:require [speclj.core :refer :all]
            [day15.core :refer :all]
            [clojure.java.io :as io]))

(describe "Day 15"
          (context "Parsing the input"
                   (it "can parse the input"
                       (let [world (parse-input "#######\n#.G.E.#\n#E.G.E#\n#.G.E.#\n#######\n")]
                         (should= 35 (count world))
                         (should= \G (world [1 2]))
                         (should= \E (world [3 4]))
                         (should= \# (world [4 6]))
                         (should= \. (world [2 2]))))

                   (it "can extract the units"
                       (let [world (parse-input "#######\n#.G.E.#\n#E.G.E#\n#.G.E.#\n#######\n")]
                         (should== [{:id 0, :location [2 3], :type \G, :hit-points 200}
                                    {:id 1, :location [2 5], :type \E, :hit-points 200}
                                    {:id 2, :location [3 4], :type \E, :hit-points 200}
                                    {:id 3, :location [1 4], :type \E, :hit-points 200}
                                    {:id 4, :location [2 1], :type \E, :hit-points 200}
                                    {:id 5, :location [1 2], :type \G, :hit-points 200}
                                    {:id 6, :location [3 2], :type \G, :hit-points 200}] (extract-units world))))

                   (it "can replace units in the world"
                       (let [world (parse-input "#######\n#.G.E.#\n#E.G.E#\n#.G.E.#\n#######\n")
                             world-without-units (replace-units world)]
                         (should= 35 (count world-without-units))
                         (should= \. (world-without-units [1 2]))
                         (should= \. (world-without-units [3 4]))
                         (should= \# (world-without-units [4 6]))
                         (should= \. (world-without-units [2 2])))))

          (context "Turn order"
                   (it "can determine the order in which units take their turns"
                       (let [world (parse-input "#######\n#.G.E.#\n#E.G.E#\n#.G.E.#\n#######\n")
                             units (extract-units world)]
                         (should= [5 3 4 0 1 6 2] (turn-order units)))))

          (context "Identify targets"
                   (it "can identify the targets"
                       (let [world (parse-input "#######\n#E..G.#\n#...#.#\n#.G.#G#\n#######")
                             units (extract-units world)]
                         (should== [{:id 1, :location [1 4], :type \G, :hit-points 200}
                                    {:id 2, :location [3 5], :type \G, :hit-points 200}
                                    {:id 3, :location [3 2], :type \G, :hit-points 200}] (identify-targets \E units))
                         (should== [{:id 0, :location [1 1], :type \E, :hit-points 200}] (identify-targets \G units)))))

          (context "Range"
                   (it "can check if two units are in range of each other"
                       (should (in-range? {:location [3 2], :type \G} {:location [4 2], :type \E}))
                       (should (in-range? {:location [4 2], :type \G} {:location [3 2], :type \E}))
                       (should-not (in-range? {:location [3 2], :type \G} {:location [5 2], :type \E}))
                       (should (in-range? {:location [3 2], :type \G} {:location [3 3], :type \E}))
                       (should (in-range? {:location [3 3], :type \G} {:location [3 2], :type \E}))
                       (should-not (in-range? {:location [3 3], :type \G} {:location [3 3], :type \E}))
                       (should-not (in-range? {:location [3 3], :type \G} {:location [4 4], :type \E}))
                       (should-not (in-range? {:location [3 3], :type \G} {:location [2 2], :type \E})))

                   (it "can identify the open squares in range of unit"
                       (let [world (parse-input "#######\n#E..G.#\n#...#.#\n#.G.#G#\n#######")
                             id->unit (extract-units world)
                             world-without-units (replace-units world)]
                         (should== #{[2 2] [3 1] [3 3]} (open-squares-in-range {:location [3 2], :type \G} id->unit world-without-units))
                         (should== #{[2 5]} (open-squares-in-range {:location [3 5], :type \G} id->unit world-without-units))
                         (should== #{[2 5]} (open-squares-in-range {:location [3 5], :type \G} id->unit world-without-units)))))

          (context "Shortest path"
                   (it "can find the shortest path between two squares"
                       (let [world (parse-input "#######\n#E..G.#\n#...#.#\n#.G.#G#\n#######")
                             units (extract-units world)
                             world-without-units (replace-units world)]
                         (should= {:shortestDistance 2, :next-square [2 1], :to-square [3 1]} (shortest-path [1 1] [3 1] units world-without-units))
                         (should= {:shortestDistance 2, :next-square [1 2], :to-square [1 3]} (shortest-path [1 1] [1 3] units world-without-units))
                         (should-be-nil (shortest-path [1 1] [2 5] units world-without-units)))))

          (context "Determine next move"
                   (it "can determine the next move for a given unit"
                       (let [world (parse-input "#######\n#E..G.#\n#...#.#\n#.G.#G#\n#######")
                             units (extract-units world)
                             world-without-units (replace-units world)
                             elf (first (filter #(= (:type %) \E) units))]
                         (should= [1 2] (next-move elf units world-without-units)))))

          (context "Larger example of movement"
                   (it "can move multiple units during multiple rounds"
                       (let [world (parse-input (slurp (io/resource "movement.txt")))
                             units (extract-units world)
                             result (nth (iterate (partial move-units (replace-units world)) units) 3)]
                         (should== [{:id 3, :location [2 3], :type \G, :hit-points 200},
                                    {:id 6, :location [2 4], :type \G, :hit-points 200},
                                    {:id 7, :location [2 5], :type \G, :hit-points 200},
                                    {:id 5, :location [3 3], :type \G, :hit-points 200},
                                    {:id 8, :location [3 4], :type \E, :hit-points 200}
                                    {:id 4, :location [3 5], :type \G, :hit-points 200},
                                    {:id 0, :location [4 1], :type \G, :hit-points 200},
                                    {:id 2, :location [4 4], :type \G, :hit-points 200},
                                    {:id 1, :location [5 7], :type \G, :hit-points 200}] result)))
                   (it "can move all units from example1"
                       (let [world (parse-input (slurp (io/resource "example1.txt")))
                             units (extract-units world)
                             result (nth (iterate (partial move-units (replace-units world)) units) 1)]
                         (should== [{:id 0, :location [3 3], :type \G, :hit-points 200}
                                    {:id 1, :location [2 5], :type \G, :hit-points 200}
                                    {:id 2, :location [2 4], :type \E, :hit-points 200}
                                    {:id 3, :location [4 5], :type \E, :hit-points 200}
                                    {:id 4, :location [1 3], :type \G, :hit-points 200}
                                    {:id 5, :location [3 5], :type \G, :hit-points 200}] result))))

          (context "Play examples"
                   (it "can play example 1"
                       (let [world (parse-input (slurp (io/resource "example1.txt")))
                             units (extract-units world)]
                         (should= 27730 (play (replace-units world) units))))
                   (it "can play example 2"
                       (let [world (parse-input (slurp (io/resource "example2.txt")))
                             units (extract-units world)]
                         (should= 36334 (play (replace-units world) units))))))

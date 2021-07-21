(ns day04.core-spec
  (:require [speclj.core :refer :all]
            [day04.core :refer :all]
            [clojure.java.io :as io]))

(def example-file (io/resource "example.txt"))
(def input-file (io/resource "input.txt"))

(describe "Day 4"

  (context "Parsing input"

    (it "should be able to parse a 'falls asleep' record."
      (should== {:day "1518-11-02", :hour "00", :minutes 40, :type :fall-asleep}
        (parse-record "[1518-11-02 00:40] falls asleep")))

    (it "should be able to parse a 'wakes up' record."
      (should== {:day "1518-11-02", :hour "00", :minutes 50, :type :wakes-up}
        (parse-record "[1518-11-02 00:50] wakes up")))

    (it "should be able to parse a 'begins shift' record."
      (should== {:day "1518-11-03", :hour "00", :minutes 5, :type :begins-shift, :guard 10}
        (parse-record "[1518-11-03 00:05] Guard #10 begins shift")))

    (it "should be able to parse multiple records"
      (let [input "[1518-11-02 00:40] falls asleep\n[1518-11-02 00:50] wakes up\n[1518-11-03 00:05] Guard #10 begins shift\n"]
        (should= [{:day "1518-11-02", :hour "00", :minutes 40, :type :fall-asleep}
                  {:day "1518-11-02", :hour "00", :minutes 50, :type :wakes-up}
                  {:day "1518-11-03", :hour "00", :minutes 5, :type :begins-shift, :guard 10}]
          (parse-records input))))

    (it "should sort records by date"
      (let [input "[1518-11-03 00:05] Guard #10 begins shift\n[1518-11-02 00:50] wakes up\n[1518-11-02 00:40] falls asleep\n"]
        (should= [{:day "1518-11-02", :hour "00", :minutes 40, :type :fall-asleep}
                  {:day "1518-11-02", :hour "00", :minutes 50, :type :wakes-up}
                  {:day "1518-11-03", :hour "00", :minutes 5, :type :begins-shift, :guard 10}]
          (parse-records input)))))

  (context "Replay records"

    (it "should keep track which guard begins a shift."
      (should= {:guard 10, :minutes-asleep-by-guard {}}
        (replay {:minutes-asleep-by-guard {}} {:day "1518-11-03", :hour "00", :minutes 5, :type :begins-shift, :guard 10}))
      (should= {:guard 99}
        (replay {} {:day "1518-11-01", :hour "23", :minutes 58, :type :begins-shift, :guard 99})))

    (it "should keep track when a guard falls asleep."
      (should= {:guard 10, :asleep-since 40}
        (replay {:guard 10} {:day "1518-11-02", :hour "00", :minutes 40, :type :fall-asleep})))

    (it "should record the minutes when a guard wakes up."
      (should= {:guard 10, :minutes-asleep-by-guard {10 [5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24]}}
        (replay {:guard 10, :asleep-since 5, :minutes-asleep-by-guard {}}
          {:day "1518-11-01", :hour "00", :minutes 25, :type :wakes-up}))
      (should= {:guard 10, :minutes-asleep-by-guard {10 [1 2 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24]}}
        (replay {:guard 10, :asleep-since 5, :minutes-asleep-by-guard {10 [1 2]}}
          {:day "1518-11-01", :hour "00", :minutes 25, :type :wakes-up}))))

  (context "Finding guard"

    (it "should find the guard that has the most minutes asleep."
      (should= 10 (find-guard-with-most-minutes-asleep {5 [1 2], 10 [5 6 7]})))
    (it "should find the minute that a guard spends asleep the most"
      (should= 3 (find-minute-most-asleep [1 2 3 3 4 5 6 6 3]))))

  (context "Examples"

    (it "should solve part 1"
      (should= 240 (solve1 (slurp example-file)))))

  (context "Solutions"

    (it "should solve part 1"
      (should= 67558 (solve1 (slurp input-file))))))

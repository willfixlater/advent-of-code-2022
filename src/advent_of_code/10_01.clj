(ns advent-of-code.10-01
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [advent-of-code.lib :as aoc.lib]))

(defn line->inst [line]
  (let [[inst & args] (string/split line #" ")]
    (case inst
      "noop" {:inst :noop}
      "addx" {:inst :addx
              :args [(edn/read-string (first args))]}
      (throw (Exception. (str "Unknown instruction: " inst))))))

(defn inst->cycles [inst]
  (case (:inst inst)
    :noop [inst]
    :addx [{:inst :noop} inst]
    (throw (Exception. (str "Unknown instruction: " (:inst inst))))))

(defn run-inst [state inst]
  (case (:inst inst)
    :noop state
    :addx (update state :X + (first (:args inst)))
    (throw (Exception. (str "Unknown instruction: " (:inst inst))))))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        cycles (mapcat (comp inst->cycles line->inst)
                       input-lines)
        initial-state {:X 1}
        c20   (reduce run-inst initial-state (take 19 cycles))
        ss20  (* 20 (:X c20))
        c60   (reduce run-inst c20 (->> cycles (drop 19) (take 40)))
        ss60  (* 60 (:X c60))
        c100  (reduce run-inst c60 (->> cycles (drop 59) (take 40)))
        ss100 (* 100 (:X c100))
        c140  (reduce run-inst c100 (->> cycles (drop 99) (take 40)))
        ss140 (* 140 (:X c140))
        c180  (reduce run-inst c140 (->> cycles (drop 139) (take 40)))
        ss180 (* 180 (:X c180))
        c220  (reduce run-inst c180 (->> cycles (drop 179) (take 40)))
        ss220 (* 220 (:X c220))]
    (+ ss20 ss60 ss100 ss140 ss180 ss220)))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

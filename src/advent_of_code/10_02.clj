(ns advent-of-code.10-02
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

(defn draw-screen [state]
  (let [{:keys [X cycle]} state]
    (if (<= (dec X) (mod cycle 40) (inc X))
      (update state :screen conj "#")
      (update state :screen conj "."))))

(defn process-step [state inst]
  (-> state
      draw-screen
      (run-inst inst)
      (update :cycle inc)))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        cycles (mapcat (comp inst->cycles line->inst)
                       input-lines)
        initial-state {:X 1
                       :cycle 0
                       :screen []}]
    (->> cycles
         (reduce process-step initial-state)
         :screen
         (partition 40)
         (run! (fn [line]
                 (println (apply str line)))))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

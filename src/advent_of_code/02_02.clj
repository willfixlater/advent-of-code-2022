(ns advent-of-code.02-02
  (:require [advent-of-code.lib :as aoc.lib]))

(def ->opponent
  {\A :rock
   \B :paper
   \C :scissors})

(def ->outcome
  {\X :loss
   \Y :draw
   \Z :win})

(def opponent-&-outcome->you
  {[:rock :draw]         :rock
   [:rock :win]          :paper
   [:rock :loss]         :scissors
   [:paper :loss]        :rock
   [:paper :draw]        :paper
   [:paper :win]         :scissors
   [:scissors :win]      :rock
   [:scissors :loss]     :paper
   [:scissors :draw]     :scissors})

(def outcome-scores
  {:win  6
   :draw 3
   :loss 0})

(def hand-scores
  {:rock     1
   :paper    2
   :scissors 3})

(defn calculate-score [opponent-&-outcome]
  (+ (hand-scores (opponent-&-outcome->you opponent-&-outcome))
     (outcome-scores (second opponent-&-outcome))))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        opponent-&-outcomes (map (fn [[opponent _ you]]
                                   [(->opponent opponent)
                                    (->outcome you)])
                                 input-lines)
        scores (map calculate-score opponent-&-outcomes)]
    (reduce + scores)))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

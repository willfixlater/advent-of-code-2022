(ns advent-of-code.02-01
  (:require [advent-of-code.lib :as aoc.lib]))

(def ->opponent
  {\A :rock
   \B :paper
   \C :scissors})

(def ->you
  {\X :rock
   \Y :paper
   \Z :scissors})

(def game->outcome
  {[:rock :rock]         :draw
   [:rock :paper]        :win
   [:rock :scissors]     :loss
   [:paper :rock]        :loss
   [:paper :paper]       :draw
   [:paper :scissors]    :win
   [:scissors :rock]     :win
   [:scissors :paper]    :loss
   [:scissors :scissors] :draw})

(def outcome-scores
  {:win  6
   :draw 3
   :loss 0})

(def hand-scores
  {:rock     1
   :paper    2
   :scissors 3})

(defn calculate-score [game]
  (+ (hand-scores (second game))
     (outcome-scores (game->outcome game))))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        games (map (fn [[opponent _ you]]
                     [(->opponent opponent)
                      (->you you)])
                   input-lines)
        scores (map calculate-score games)]
    (reduce + scores)))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

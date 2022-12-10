(ns advent-of-code.09-01
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [advent-of-code.lib :as aoc.lib]))

(def char->direction
  {"L" :left
   "D" :down
   "R" :right
   "U" :up})

(defn parse-command [line]
  (let [[dir-char n] (string/split line #" ")
        times (edn/read-string n)]
    (repeat times (char->direction dir-char))))

(defn move [state end direction]
  (case direction
    :left  (update-in state [end 0] dec)
    :down  (update-in state [end 1] dec)
    :right (update-in state [end 0] inc)
    :up    (update-in state [end 1] inc)
    (throw (Exception. (str "Unknown command direction: " direction)))))

(defn update-tail [state]
  (let [{[hx hy] :head
         [tx ty] :tail} state]
    (cond
      (< hx (dec tx))
      (cond
        (= hy ty) (move state :tail :left)
        (> hy ty) (-> state
                      (move :tail :left)
                      (move :tail :up))
        (< hy ty) (-> state
                      (move :tail :left)
                      (move :tail :down)))
      
      (< hy (dec ty))
      (cond
        (= hx tx) (move state :tail :down)
        (> hx tx) (-> state
                      (move :tail :down)
                      (move :tail :right))
        (< hx tx) (-> state
                      (move :tail :down)
                      (move :tail :left)))

      (> hx (inc tx))
      (cond
        (= hy ty) (move state :tail :right)
        (> hy ty) (-> state
                      (move :tail :right)
                      (move :tail :up))
        (< hy ty) (-> state
                      (move :tail :right)
                      (move :tail :down)))

      (> hy (inc ty))
      (cond
        (= hx tx) (move state :tail :up)
        (> hx tx) (-> state
                      (move :tail :up)
                      (move :tail :right))
        (< hx tx) (-> state
                      (move :tail :up)
                      (move :tail :left)))
      
      :else state)))

(defn record-tail-position [state]
  (update state :prev-tail conj (:tail state)))

(defn process-step [state command]
  (-> state
      (move :head command)
      update-tail
      record-tail-position))

(defn solution [reader]
  (let [input (line-seq reader)
        commands (mapcat parse-command input)]
    (->> commands
         (reduce process-step
                 {:head [0 0]
                  :tail [0 0]
                  :prev-tail #{[0 0]}})
         :prev-tail count)))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

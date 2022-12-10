(ns advent-of-code.09-02
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

(defn move [state knot direction]
  (case direction
    :left  (update-in state [:knots knot 0] dec)
    :down  (update-in state [:knots knot 1] dec)
    :right (update-in state [:knots knot 0] inc)
    :up    (update-in state [:knots knot 1] inc)
    (throw (Exception. (str "Unknown command direction: " direction)))))

(defn update-knot [state this-knot]
  (let [prev-knot (dec this-knot)
        [ix iy] (get-in state [:knots prev-knot])
        [jx jy] (get-in state [:knots this-knot])]
    (cond
      (< ix (dec jx))
      (cond
        (= iy jy) (move state this-knot :left)
        (> iy jy) (-> state
                      (move this-knot :left)
                      (move this-knot :up))
        (< iy jy) (-> state
                      (move this-knot :left)
                      (move this-knot :down)))

      (< iy (dec jy))
      (cond
        (= ix jx) (move state this-knot :down)
        (> ix jx) (-> state
                      (move this-knot :down)
                      (move this-knot :right))
        (< ix jx) (-> state
                      (move this-knot :down)
                      (move this-knot :left)))

      (> ix (inc jx))
      (cond
        (= iy jy) (move state this-knot :right)
        (> iy jy) (-> state
                      (move this-knot :right)
                      (move this-knot :up))
        (< iy jy) (-> state
                      (move this-knot :right)
                      (move this-knot :down)))

      (> iy (inc jy))
      (cond
        (= ix jx) (move state this-knot :up)
        (> ix jx) (-> state
                      (move this-knot :up)
                      (move this-knot :right))
        (< ix jx) (-> state
                      (move this-knot :up)
                      (move this-knot :left)))

      :else state)))

(defn record-tail-position [state]
  (update state :prev-tail conj (last (:knots state))))

(defn process-step [knot-count]
  (fn [state command]
    (as-> state state
      (move state 0 command)
      (reduce update-knot state (range 1 knot-count))
      (record-tail-position state))))

(defn solution [reader]
  (let [knot-count 10
        input (line-seq reader)
        commands (mapcat parse-command input)]
    (->> commands
         (reduce (process-step knot-count)
                 {:knots (vec (repeat knot-count [0 0]))
                  :prev-tail #{[0 0]}})
         :prev-tail count)))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

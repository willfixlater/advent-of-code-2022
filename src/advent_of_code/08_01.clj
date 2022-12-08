(ns advent-of-code.08-01
  (:require [clojure.core.matrix :as matrix]
            [clojure.core.matrix.operators :as matrix.operators]
            [advent-of-code.lib :as aoc.lib]))

(defn rotate-cw [mat]
  (loop [new []
         idx 0]
    (if (= idx (count mat))
      new
      (recur (conj new (mapv #(nth % idx) (reverse mat)))
             (inc idx)))))

(defn rotate-ccw [mat]
  (loop [new []
         idx (dec (count mat))]
    (if (= idx -1)
      new
      (recur (conj new (mapv #(nth % idx) mat))
             (dec idx)))))

(defn visibility-sweep [mat]
  (mapv (fn [row]
          (first
           (reduce (fn [[bitrow tallest] tree]
                     [(conj bitrow (if (> tree tallest) 0 1))
                      (max tallest tree)])
                   [[] -1]
                   row)))
        mat))

(defn solution [reader]
  (let [input (line-seq reader)
        grid (mapv (fn [line]
                     (mapv #(Character/digit % 10) line))
                   input)
        left-bm (visibility-sweep grid)
        grid (rotate-cw grid)
        top-bm (rotate-ccw (visibility-sweep grid))
        grid (rotate-cw grid)
        right-bm (-> (visibility-sweep grid)
                     rotate-ccw
                     rotate-ccw)
        grid (rotate-cw grid)
        bottom-bm (rotate-cw (visibility-sweep grid))
        visibility-bm (matrix.operators/* left-bm top-bm right-bm bottom-bm)]
    (- (matrix/ecount grid)
       (reduce + (map #(reduce + %) visibility-bm)))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

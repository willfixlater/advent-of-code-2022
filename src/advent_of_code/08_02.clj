(ns advent-of-code.08-02
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

(defn scenic-sweep [mat]
  (mapv (fn [row]
          (first
           (reduce (fn [[scored-row prev] tree]
                     (let [[low blocked] (split-with #(< % tree) prev)
                           score (+ (count low) (count (take 1 blocked)))]
                       [(conj scored-row score)
                        (conj prev tree)]))
                   [[] '()]
                   row)))
        mat))

(defn solution [reader]
  (let [input (line-seq reader)
        grid (mapv (fn [line]
                     (mapv #(Character/digit % 10) line))
                   input)
        left-scored (scenic-sweep grid)
        grid (rotate-ccw grid)
        top-scored (rotate-cw (scenic-sweep grid))
        grid (rotate-ccw grid)
        right-scored (-> (scenic-sweep grid)
                         rotate-cw
                         rotate-cw)
        grid (rotate-ccw grid)
        bottom-scored (rotate-ccw (scenic-sweep grid))
        all-scored (matrix.operators/* left-scored
                                       top-scored
                                       right-scored
                                       bottom-scored)]
    (matrix/emax all-scored)))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

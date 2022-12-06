(ns advent-of-code.06-02
  (:require [advent-of-code.lib :as aoc.lib]))

(defn char-seq
  [^java.io.Reader rdr]
  (let [chr (.read rdr)]
    (when (>= chr 0)
      (cons chr (lazy-seq (char-seq rdr))))))

(defn solution [reader]
  (let [input (char-seq reader)]
    (loop [input input
           cnt 14]
      (let [[window remaining] (split-at 14 input)]
        (if (= 14 (count (set window)))
          cnt
          (recur (concat (rest window) remaining)
                 (inc cnt)))))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

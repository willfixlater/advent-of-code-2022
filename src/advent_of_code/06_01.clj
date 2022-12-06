(ns advent-of-code.06-01
  (:require [advent-of-code.lib :as aoc.lib]))

(defn char-seq
  [^java.io.Reader rdr]
  (let [chr (.read rdr)]
    (when (>= chr 0)
      (cons chr (lazy-seq (char-seq rdr))))))

(defn solution [reader]
  (let [input (char-seq reader)]
    (loop [[a b c d & remaining] input
           cnt 4]
      (if (= 4 (count (set [a b c d])))
        cnt
        (recur (concat [b c d] remaining)
               (inc cnt))))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

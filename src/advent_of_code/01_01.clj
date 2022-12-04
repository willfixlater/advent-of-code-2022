(ns advent-of-code.01-01
  (:require [clojure.edn :as edn]
            [advent-of-code.lib :as aoc.lib]))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        elves-with-food (->> input-lines
                             (map edn/read-string)
                             (partition-by nil?)
                             (remove #{'(nil)}))
        elves-by-calories (map #(reduce + %) elves-with-food)]
    (apply max elves-by-calories)))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

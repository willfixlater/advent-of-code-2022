(ns advent-of-code.03-02
  (:require [clojure.set :as set]
            [advent-of-code.lib :as aoc.lib]))

(def item-type->priority
  (let [char-range (concat
                    (range 97 123)
                    (range 65 91))]
    (zipmap
     (map char char-range)
     (map inc (range)))))

(defn rucksack-trio->item-type [trio]
  (let [trio-set (map set trio)]
    (first (apply set/intersection trio-set))))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        item-types  (->> input-lines
                         (partition 3)
                         (map rucksack-trio->item-type))]
    (->> item-types
         (map item-type->priority)
         (reduce +))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

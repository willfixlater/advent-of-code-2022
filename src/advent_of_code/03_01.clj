(ns advent-of-code.03-01
  (:require [clojure.set :as set]
            [advent-of-code.lib :as aoc.lib]))

(def item-type->priority
  (let [char-range (concat
                    (range 97 123)
                    (range 65 91))]
    (zipmap
     (map char char-range)
     (map inc (range)))))

(defn rucksack->item-type [rucksack]
  (let [cmpt-size   (/ (count rucksack) 2)
        first-cmpt  (set (take cmpt-size rucksack))
        second-cmpt (set (drop cmpt-size rucksack))]
    (first (set/intersection first-cmpt second-cmpt))))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        item-types  (map rucksack->item-type input-lines)]
    (->> item-types
         (map item-type->priority)
         (reduce +))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

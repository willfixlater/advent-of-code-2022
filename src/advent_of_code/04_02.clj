(ns advent-of-code.04-02
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [advent-of-code.lib :as aoc.lib]))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        section-pairs (->> input-lines
                           (map #(string/split % #","))
                           (map (fn [[a b]]
                                  [(mapv edn/read-string (string/split a #"-"))
                                   (mapv edn/read-string (string/split b #"-"))])))]
    (reduce (fn [cnt [[a1 a2] [b1 b2]]]
              (if (or (and (<= a1 b2) (>= a2 b2))
                      (and (<= a1 b1) (>= a2 b1))
                      (and (<= b1 a2) (>= b2 a2))
                      (and (<= b1 a1) (>= b2 a1)))
                (inc cnt)
                cnt))
            0
            section-pairs)))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution))

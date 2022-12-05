(ns advent-of-code.05-01
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [advent-of-code.lib :as aoc.lib]))

(defn parse-matrix [input]
  (let [raw-chars (->> input
                       (map #(partition 3 4 %))
                       (drop-last)
                       (map #(map second %)))]
    (->> raw-chars
         (apply map list)
         (map #(remove #{\space} %))
         (into [] (comp (map reverse)
                        (map vec))))))

(defn parse-instructions [input]
  (->> input
       (map #(string/split % #" "))
       (map rest)
       (map #(take-nth 2 %))
       (mapv #(mapv edn/read-string %))))

(defn apply-instruction [mat [qty src des]]
  (-> mat
      (update (dec des) into
              (reverse (take-last qty (get mat (dec src)))))
      (update (dec src) #(nth (iterate pop %) qty))))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        [crate-input [_ & instructions-input]] (split-with seq input-lines)
        crate-matrix (parse-matrix crate-input)
        instructions (parse-instructions instructions-input)]
    (->> instructions
           (reduce apply-instruction crate-matrix)
           (map last)
           (apply str))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )

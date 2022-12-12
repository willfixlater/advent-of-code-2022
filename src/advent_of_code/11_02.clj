(ns advent-of-code.11-02
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [advent-of-code.lib :as aoc.lib]))

(defn parse-operation [operation]
  (let [formula (string/replace operation #"  Operation: new = " "")
        [x op y] (string/split formula #" ")]
    (case op
      "+" (fn [old] (+ (if (= "old" x) old (edn/read-string x))
                       (if (= "old" y) old (edn/read-string y))))
      "*" (fn [old] (* (if (= "old" x) old (edn/read-string x))
                       (if (= "old" y) old (edn/read-string y))))
      (throw (Exception. (str "Unknown operation: " op))))))

(defn parse-monkey [monkey]
  (let [idx (edn/read-string (re-find #"\d+" (nth monkey 0)))
        items (->> (nth monkey 1)
                   (re-seq #"\d+")
                   (mapv edn/read-string))
        operation (parse-operation (nth monkey 2))
        test (edn/read-string (re-find #"\d+" (nth monkey 3)))
        then (edn/read-string (re-find #"\d+" (nth monkey 4)))
        else (edn/read-string (re-find #"\d+" (nth monkey 5)))]
    {:idx idx
     :items items
     :op operation
     :test test
     :then then
     :else else
     :inspected 0}))

(defn monkey-business [monkeys]
  (let [worry-limit (apply * (map :test monkeys))]
    (reduce (fn [monkeys {:keys [idx op test then else]}]
              (reduce (fn [monkeys item]
                        (let [new-item (mod (op item) worry-limit)
                              new-monkey (if (= 0 (mod new-item test)) then else)]
                          (-> monkeys
                              (update-in [idx :inspected] inc)
                              (update-in [idx :items] (comp vec rest))
                              (update-in [new-monkey :items] conj new-item))))
                      monkeys
                      (get-in monkeys [idx :items])))
            monkeys
            monkeys)))

(defn solution [reader]
  (let [input-lines (line-seq reader)
        monkeys (->> input-lines
                     (partition-by #{""})
                     (remove #{'("")})
                     (mapv parse-monkey))]
    (loop [cnt 1
           monkeys monkeys]
      (if (> cnt 10000)
        (->> monkeys
             (map :inspected)
             sort
             (take-last 2)
             (apply *))
        (recur (inc cnt) (monkey-business monkeys))))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution)
  )
(ns advent-of-code.07-02
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [advent-of-code.lib :as aoc.lib]))

(def initial-fs
  {:dir-sizes {}
   :cwd ["/"]})

(defn parse-cd [fs cmd]
  (let [[_ _ dir] (string/split cmd #" ")]
    (case dir
      "/" (assoc fs :cwd ["/"])
      ".." (update fs :cwd pop)
      (update fs :cwd conj dir))))

(defn parse-file [fs cmd]
  (let [[size-str _] (string/split cmd #" ")
        size (edn/read-string size-str)]
    (loop [fs fs
           path (:cwd fs)]
      (if (empty? path)
        fs
        (recur (update-in fs [:dir-sizes path] (fnil + 0) size)
               (pop path))))))

(defn parse-command [fs cmd]
  (cond
    (re-matches #"^\$ ls" cmd) fs
    (re-matches #"^dir.*" cmd) fs
    (re-matches #"^\$ cd.*" cmd) (parse-cd fs cmd)
    (re-matches #"^\d+.*" cmd) (parse-file fs cmd)
    :else (throw (Exception. (str "Unrecognised command: " cmd)))))

(defn solution [reader]
  (let [input (line-seq reader)
        {:keys [dir-sizes]} (reduce parse-command initial-fs input)
        root-size (dir-sizes ["/"])
        available-space (- 70000000 root-size)
        required-space (- 30000000 available-space)]
    (->> (vals dir-sizes)
         (filter #(>= % required-space))
         (apply min))))

(defn -main []
  (aoc.lib/run-and-save-result solution))

(comment
  (aoc.lib/run-with-input solution))

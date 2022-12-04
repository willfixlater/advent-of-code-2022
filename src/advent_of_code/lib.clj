(ns advent-of-code.lib
  (:require [clojure.string :as string]
            [ clojure.java.io :as io])
  (:import [java.io File]))

(defn ns->input-dir [ns]
  (-> ns
      ns-name
      str
      (string/split #"\.")
      last
      (string/split #"-")
      first))

(defn run-with-input [f]
  (let [resource-dir (ns->input-dir *ns*)
        input-path (str resource-dir File/separator "input")]
    (-> input-path
        io/resource
        io/reader
        f)))

(defn run-and-save-result [f]
  (spit "output" (run-with-input f)))
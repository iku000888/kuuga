(ns example.kuuga-ext
  (:require [kuuga.growing :as growing]
            [kuuga.tool :as tool]))

(defmethod growing/transform-by-class :form-group
  [_ options tag-vector]
  (let [[tagkw tagopts contents] (tool/parse-tag-vector tag-vector)
        contents (reduce (fn [contents' tagvec']
                           (let [[tk to _] (tool/parse-tag-vector tagvec')
                                 [_ t] (tool/parse-tag-keyword tk)]
                             (cond-> (conj contents' tagvec')
                               (= t "input") (conj `(example.core/invalid-feedback ~options ~to)))))
                         []
                         contents)]
    `[~tagkw
      ~tagopts
      ~@contents]))

(defmethod growing/transform-by-tag :input
  [_ options tag-vector]
  (let [[tagkw tagopts contents] (tool/parse-tag-vector tag-vector)]
    `[~tagkw
      (example.core/update-input-opts ~options ~tagopts)
      ~@contents]))

; Copyright 2017 Frederic Merizen
;
; Redistribution and use in source and binary forms, with or without
; modification, are permitted provided that the following conditions are met:
;
; 1. Redistributions of source code must retain the above copyright notice, this
;    list of conditions and the following disclaimer.
;
; 2. Redistributions in binary form must reproduce the above copyright notice,
;    this list of conditions and the following disclaimer in the documentation
;    and/or other materials provided with the distribution.
;
; 3. Neither the name of the copyright holder nor the names of its contributors
;    may be used to endorse or promote products derived from this software
;    without specific prior written permission.
;
; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
; AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
; IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
; DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
; FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
; DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
; SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
; CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
; OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
; OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

(ns plumula.impl.convert
  (:require [clojure.string :refer [split capitalize]]))

(defn options->js [exp]
  (cond
    (keyword? exp)
    (let [[head & tail] (-> exp
                            name
                            (split #"-"))]
      (apply str head (map capitalize tail)))

    (map? exp)
    (let [r (js-obj)]
      (doseq [[k v] exp]
        (aset r (options->js k) (options->js v)))
      r)

    (coll? exp)
    (let [a (array)]
      (doseq [v exp]
        (.push a (options->js v)))
      a)

    :else
    exp))

(defn js->delta [json]
  {:ops (js->clj (.-ops json) :keywordize-keys true)})

(defn delta->js [delta]
  (clj->js delta))

(defn embed->js [embed]
  (clj->js embed))

(defn format-name->js [name]
  (cljs.core/name name))

(defn format-value->js [value]
  (name value))

(defn formats->js [formats]
  (clj->js formats))

(defn source->js [source]
  (name source))

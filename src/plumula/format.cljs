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

(ns plumula.format
  (:require [cljsjs.quill]
            [plumula.impl.convert :as c]))

(set! *warn-on-infer* true)

(defn format
  "Format text at user’s current selection, returning a Delta representing the
  change. If the user’s selection length is 0, i.e. it is a cursor, the format
  will be set active, so the next character the user types will have that
  formatting. Source may be \"user\", \"api\", or \"silent\". Calls where the
  source is \"user\" when the editor is disabled are ignored.

  Examples
  (format editor :color :red)
  (format editor :align :right)
  "
  ([^js/Quill editor name value]
   (c/js->delta (.format editor (c/format-name->js name) (c/format-value->js value))))
  ([^js/Quill editor name value source]
   (c/js->delta (.format editor (c/format-name->js name) (c/format-value->js value) (c/source->js source)))))

(defn format-line
  "Formats all lines in given range, returning a Delta representing the change.
  See formats for a list of available formats. Has no effect when called with
  inline formats. To remove formatting, pass false for the value argument. The
  user’s selection may not be preserved. Source may be :user, :api, or :silent.
  Calls where the source is :user when the editor is disabled are ignored.

  Examples
  (set-text editor \"Hello\\nWorld!\\n\")

  (format-line editor 1 2 :align :right)  ; right aligns the first line
  (format-line editor 4 4 :align :center) ; center aligns both lines
  "
  {:arglists '([editor index length source?]
               [editor index length formats source?]
               [editor index length format value source?])}

  ([^js/Quill editor index length]
   (c/js->delta (.formatLine editor index length)))
  ([^js/Quill editor index length source-or-formats]
   (if (map? source-or-formats)
     (c/js->delta (.formatLine editor index length (c/formats->js source-or-formats)))
     (c/js->delta (.formatLine editor index length (c/source->js source-or-formats)))))
  ([^js/Quill editor index length formats-or-format source-or-value]
   (if (map? formats-or-format)
     (c/js->delta (.formatLine editor index length (c/formats->js formats-or-format) (c/source->js source-or-value)))
     (c/js->delta (.formatLine editor index length (c/format-name->js formats-or-format) (c/format-value->js source-or-value)))))
  ([^js/Quill editor index length format value source]
   (c/js->delta (.formatLine editor index length (c/format-name->js format) (c/format-value->js value) (c/source->js source)))))


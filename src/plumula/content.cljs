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

(ns plumula.content
  (:require [cljsjs.quill]
            [plumula.impl.convert :as c]))

(set! *warn-on-infer* true)

(defn delete-text
  "Deletes text from the editor, returning a Delta representing the change.
  Source may be :user, :api, or :silent. Calls where the source is :user when
  the editor is disabled are ignored.

  Examples
  (delete-text editor 6 4)
  "
  ([^js/Quill editor index length]
   (c/js->delta (.deleteText editor index length)))
  ([^js/Quill editor index length source]
   (c/js->delta (.deleteText editor index length (c/source->js source)))))

(defn get-contents
  "Retrieves contents of the editor, with formatting data, represented by a
  Delta object.

  Examples
  (get-contents editor)
  "
  ([^js/Quill editor]
   (c/js->delta (.getContents editor)))
  ([^js/Quill editor index]
   (c/js->delta (.getContents editor index)))
  ([^js/Quill editor index length]
   (c/js->delta (.getContents editor index length))))

(defn get-length
  "Retrieves the length of the editor contents. Note even when Quill is empty,
  there is still a blank line represented by \"\\n\", so getLength will return
  1.

  Examples
  (let [length (get-length editor)])
  "
  [^js/Quill editor]
  (.getLength editor))

(defn get-text
  "Retrieves the string contents of the editor. Non-string content are omitted,
  so the returned string’s length may be shorter than the editor’s as returned
  by getLength. Note even when Quill is empty, there is still a blank line in
  the editor, so in these cases getText will return \"\\n\".

  The length parameter defaults to the length of the remaining document.

  Examples
  (let [text (get-text editor 0 10)])
  "
  ([^js/Quill editor]
   (.getText editor))
  ([^js/Quill editor index]
   (.getText editor index))
  ([^js/Quill editor index length]
   (.getText editor index length)))

(defn insert-embed
  "Insert embedded content into the editor, returing a Delta representing the
  change. Source may be :user, :api, or :silent. Calls where the source is :user
  when the editor is disabled are ignored.

  Examples
  (insert-embed editor 10 \"image\" \"http://quilljs.com/images/cloud.png\")
  "
  ([^js/Quill editor index type value]
   (c/js->delta (.insertEmbed editor index type (c/embed->js value))))
  ([^js/Quill editor index type value source]
   (c/js->delta (.insertEmbed editor index type (c/embed->js value) (c/source->js source)))))

(defn insert-text
  "Inserts text into the editor. Returns a Delta representing the change. Source
  may be :user, :api, or :silent. Calls where the source is :user when the
  editor is disabled are ignored.

  Examples
  (insert-text editor 0 \"Hello\" :bold true)
  (insert-text editor 5 \"Quill\" {:color \"#ffff00\" :italic true})
  "
  {:arglists '[[editor index text source?]
               [editor index text formats source?]
               [editor index text format value source?]]}

  ([^js/Quill editor index text]
   (c/js->delta (.insertText editor index text)))
  ([^js/Quill editor index text source-or-formats]
   (if (map? source-or-formats)
     (c/js->delta (.insertText editor index text (c/formats->js source-or-formats)))
     (c/js->delta (.insertText editor index text (c/source->js source-or-formats)))))
  ([^js/Quill editor index text formats-or-format source-or-value]
   (if (map? formats-or-format)
     (c/js->delta (.insertText editor index text (c/formats->js formats-or-format) (c/source->js source-or-value)))
     (c/js->delta (.insertText editor index text (c/format-name->js formats-or-format) (c/format-value->js source-or-value)))))
  ([^js/Quill editor index text format value source]
   (c/js->delta (.insertText editor index text (c/format-name->js format) (c/format-value->js value) (c/source->js source)))))

(defn set-contents
  "Overwrites editor with given contents. Contents should end with a newline.
  Returns a Delta representing the change. This will be the same as the Delta
  passed in, if given Delta had no invalid operations. Source may be :user,
  :api, or :silent. Calls where the source is :user when the editor is disabled
  are ignored.

  Examples
  (set-contents editor
    [{:insert \"Hello \"}
     {:insert \"World!\" :attributes {:bold true}}
     {:insert \"\\n\"}])
  "
  ([^js/Quill editor delta]
   (c/js->delta (.setContents editor (c/delta->js delta))))
  ([^js/Quill editor delta source]
   (c/js->delta (.setContents editor (c/delta->js delta) (c/source->js source)))))

(defn set-text
  "Sets contents of editor with given text, returing a Delta representing the
  change. Note Quill documents must end with a newline so one will be added for
  you if omitted. Source may be :user, :api, or :silent. Calls where the source
  is :user when the editor is disabled are ignored.

  Examples
  (set-text editor \"Hello\\n\")
  "
  ([^js/Quill editor text]
   (c/js->delta (.setText editor text)))
  ([^js/Quill editor text source]
   (c/js->delta (.setText editor text (c/source->js source)))))

(defn update-contents
  "Applies Delta to editor contents, returing a Delta representing the change.
  These Deltas will be the same if the Delta passed in had no invalid
  operations. Source may be :user, :api, or :silent. Calls where the source is
  :user when the editor is disabled are ignored.

  Examples
  ; Assuming editor currently contains [{:insert \"Hello World!\"}]
  (update-contents editor
    {:ops [{:retain 6}                             ; Keep 'Hello '
           {:delete 5}                             ; 'World' is deleted
           {:insert \"Quill\"}                       ; Insert 'Quill'
           {:retain 1 :attributes {:bold true}}]}) ; Apply bold to exclamation mark
  ; Editor should now be [{:insert \"Hello Quill\"}{:insert \"!\" :attributes {:bold true}}]
  "
  ([^js/Quill editor delta]
   (js->clj (.updateContents editor (c/delta->js delta))))
  ([^js/Quill editor delta source]
   (js->clj (.updateContents editor (c/delta->js delta) (c/source->js source)))))

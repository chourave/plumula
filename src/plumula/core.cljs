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

(ns plumula.core
  (:require [cljsjs.quill]
            [plumula.impl.convert :refer [options->js]]))

(set! *warn-on-infer* true)

(defn quill
  "Quill requires a container where the editor will be appended. You can pass in
   either a CSS selector or a DOM object.

   Examples

   (def editor (quill \".editor\")) ; First matching element will be used

   (def editor
     (let [container (.getElementById js/document \"editor\")]
       (quill container)))

   To configure Quill, pass in an options object:
   (quill
     \"#editor\"
     {:debug       :info
      :modules     {:toolbar \"#toolbar\"}
      :placeholder \"Compose an epic...\"
      :read-only   true
      :theme       \"snow\"})

   The following keys are recognized:

   :bounds (Default: document.body)
   DOM Element, or a CSS selector for a DOM Element, within which the editor’s
   ui elements (i.e. tooltips, etc.) should be confined. Currently, it only
   considers left and right boundaries.

   :debug (Default: warn)
   Shortcut for debug. Note debug is a static method and will affect other
   instances of Quill editors on the page. Only warning and error messages are
   enabled by default.

   :formats (Default: All formats)
   Whitelist of formats to allow in the editor. See Formats for a complete list.

   :modules
   Collection of modules to include and respective options. See Modules for more
   information.

   :placeholder (Default: \"\")
   Placeholder text to show when editor is empty.

   :read-only (Default: false)
   Whether to instantiate the editor to read-only mode.

   :scrolling-container (Default: nil)
   Specifies which container has the scrollbars (i.e. overflow-y: auto), if
   changed with CSS from the default ql-editor. Necessary to fix scroll jumping
   bugs when Quill is set to auto grow its height, and another ancestor
   container is responsible from the scrolling.

   :strict (Default: true)
   Some improvements and modifications, under a strict interpretation of semver,
   would warrant a major version bump. To prevent small changes from inflating
   Quill’s version number, they are disabled by this strict flag. Specific
   changes can be found in the Changelog and searching for “strict”. Setting
   this to false opts into potential future improvements.

   :theme
   Name of theme to use. The builtin options are \"bubble\" or \"snow\". An
   invalid or falsy value will load a default minimal theme. Note the theme’s
   specific stylesheet still needs to be included manually. See Themes for more
   information.
   "
  ([container]
   (js/Quill. container))
  ([container options]
   (js/Quill. container (options->js options))))

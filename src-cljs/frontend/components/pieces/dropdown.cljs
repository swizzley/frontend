(ns frontend.components.pieces.dropdown
  (:require [devcards.core :as dc :refer-macros [defcard-om]]
            [frontend.utils.seq :refer [find-index]]
            [om.core :as om :include-macros true])
  (:require-macros [frontend.utils :refer [html]]))

(defn dropdown
  "A standard dropdown select control.

  :on-change - A function called with the new value when the selection changes.
               Unlike the DOM node's onclick handler, this function is *not*
               called with an event object. It receives the value itself.
  :value     - The currently selected value.
  :options   - A sequence of pairs, [value label]. The label is the text shown
               for each option. The value can be any object, and will be passed
               to the :on-click handler when that option is selected."
  [{:keys [on-change value options]}]
  (let [values (map first options)
        labels (map second options)]
    [:select {:data-component `dropdown
              :on-change #(on-change (nth values (-> % .-target .-value int)))
              :value (find-index #(= value %) values)}
     (for [[index label] (map-indexed vector labels)]
       [:option {:value index} label])]))

(dc/do
  (defn dropdown-parent [{:keys [selected-value] :as data} owner]
    (om/component
        (html
         [:div
          [:div
           (dropdown
            {:on-change #(om/update! data :selected-value %)
             :value selected-value
             :options [["value" "String Value"]
                       [:value "Keyword Value"]
                       [{:map "value"} "Map Value"]]})]
          [:div
           "Selected: " (if selected-value
                          (pr-str selected-value)
                          "(Nothing)")]])))

  (defcard-om dropdown
    dropdown-parent
    {:selected-value nil}))

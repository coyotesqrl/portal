(ns portal.ui.lazy
  (:refer-clojure :exclude [lazy-seq])
  (:require ["react-visibility-sensor" :as react-visibility-sensor]
            [portal.ui.styled :as s]
            [portal.ui.state :as state]
            [reagent.core :as r]))

(defn lazy-seq [_settings _ opts]
  (let [{:keys [default-take step]
         :or   {default-take 0 step 10}} opts
        n (r/atom default-take)
        VisibilitySensor (.-default react-visibility-sensor)]
    (fn [settings seqable]
      [:<>
       (doall (take @n seqable))
       (if-not (seq (drop @n seqable))
         (when (= (:depth settings) 1)
           (state/more settings (:value settings))
           nil)
         [:> VisibilitySensor
          {:key @n
           :on-change
           #(when % (swap! n + step))}
          [s/div {:style {:height "1em" :width "1em"}}]])])))

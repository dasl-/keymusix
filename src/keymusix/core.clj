(ns keymusix.core)

(require '[seesaw.core :as s])
(use 'overtone.live)

(def notes (vec (map (comp midi->hz note) [:g1 :g2 :d2 :f2 :c2 :c3 :bb1 :bb2
                                           :a1 :a2 :e2 :g2 :d2 :d3 :c2 :c3])))


(definst hat [volume 1.0 freq 440]
  (let [src (sin-osc freq)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* volume 1 src env)))

(def textArea (s/text :multi-line? true :font "monaco-plain-14" :background "#000" :foreground "#0F0" :text "May the music be with you."))

(s/listen textArea :key-pressed (fn [e] (hat (first notes)) ;;(s/alert e (.toString e))
))

(defn -main [& args]
  (s/invoke-later
   (-> (seesaw.core/frame :title "Life's missing sound track",
              :content textArea,
              :on-close :exit)
       seesaw.core/pack!
       seesaw.core/show!))
  )

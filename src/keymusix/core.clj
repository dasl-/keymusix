(ns keymusix.core)

;; (require '[seesaw.core :as s])
(import java.awt.event.KeyEvent)
(use 'overtone.live)


(definst keyboard [freq 440 volume 1.0]
  (let [src (sin-osc freq)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* volume 1 src env)))

(defn play-keyboard [ notename ]
  (println (name notename))
  (println (do (note notename)))
  (keyboard :freq (midi->hz (note notename))))

(defn map-note [k]
  (println (format "%x" k))
    (cond
      (= 0x0E k) (play-keyboard :c4) ;; e
      (= 0x11 k) (play-keyboard :d4) ;; t
      (= 0x00 k) (play-keyboard :e4) ;; a
      (= 0x1F k) (play-keyboard :g4) ;; o
      (= 0x22 k) (play-keyboard :a4) ;; i
      (= 0x2D k) (play-keyboard :c3) ;; n
      (= 0x01 k) (play-keyboard :d3) ;; s
      (= 0x04 k) (play-keyboard :e3) ;; h
      (= 0x0F k) (play-keyboard :g3) ;; r
      (= 0x02 k) (play-keyboard :a3) ;; d
      (= 0x25 k) (play-keyboard :c5) ;; l
      (= 0x08 k) (play-keyboard :d5) ;; c
      (= 0x20 k) (play-keyboard :e5) ;; u
      (= 0x2E k) (play-keyboard :g5) ;; m
      (= 0x0D k) (play-keyboard :a5) ;; w
      (= 0x03 k) (play-keyboard :c2) ;; f
      (= 0x05 k) (play-keyboard :d2) ;; g
      (= 0x10 k) (play-keyboard :e2) ;; y
      (= 0x23 k) (play-keyboard :g2) ;; p
      (= 0x0B k) (play-keyboard :a2) ;; b
      (= 0x09 k) (play-keyboard :c6) ;; v
      (= 0x28 k) (play-keyboard :d6) ;; k
      (= 0x26 k) (play-keyboard :e6) ;; j
      (= 0x07 k) (play-keyboard :g6) ;; x
      (= 0x0C k) (play-keyboard :a6) ;; q
      (= 0x06 k) (play-keyboard :c1) ;; z
      :else (println "not valid"))
  )

(defn notes [start]
  (let [hang (map note [:c4 :d4 :e4 :g4 :a4 :c5])
        intervals (cycle (map - (rest hang) hang))]
  (reductions + start intervals)))

(defn play [inst notes]
  (inst (midi->hz (first notes)))
  (Thread/sleep 500)
  (recur inst (next notes)))

(into {} (map vector [0x0E 0x11 0x00 0x1F 0x22 0x2D 0x01 0x04 0x0F 0x02 0x25 0x08 0x20 0x2E 0x0D 0x03 0x05 0x10 0x23 0x0B 0x09 0x28 0x26 0x07 0x0C 0x06] (notes 60)))


(defn -main [& args]
  (let [ fileInput (java.io.FileInputStream. (java.io.File. (first args)))] 
    (loop [ch (.read fileInput)]
      (when (not= -1 ch)
        (map-note ch)
        (println "foo")
        (recur (.read fileInput))))))

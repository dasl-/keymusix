(ns keymusix.core 
  (:import java.awt.event.KeyEvent)
  (:use overtone.live overtone.inst.sampled-piano)
  (:import org.jnativehook.GlobalScreen)
  (:import org.jnativehook.keyboard.NativeKeyListener))

(definst keyboard [freq 440]
  (let [src (sin-osc freq)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* src env)))

(defn play-keyboard [ notename ]
  (keyboard :freq (* (midi->hz notename))))

(defn notes [start]
  (let [hang (map note [:c4 :d4 :e4 :g4 :a4 :c5])
        intervals (cycle (map - (rest hang) hang))]
  (reductions + start intervals)))

(defn play [inst notes]
  (inst (midi->hz (first notes)))
  (Thread/sleep 500)
  (recur inst (next notes)))

(def notes (vec (take 10 (notes 70))))

(def seed (rand-int 997))

(defn map-note [n]
  (sampled-piano (get notes (mod (bit-xor n seed) (- (count notes) 1)))))

(defn myGlobalKeyListener []
  (reify
    NativeKeyListener
    (nativeKeyPressed [this event] (map-note (.getKeyCode event)))))

(defn -main [& args]
  (GlobalScreen/registerNativeHook)
  (.addNativeKeyListener (GlobalScreen/getInstance) (myGlobalKeyListener)))

(ns keymusix.core 
  (:import javax.sound.midi.MidiSystem)
  (:import org.jnativehook.GlobalScreen)
  (:import org.jnativehook.keyboard.NativeKeyListener))

(def synth (MidiSystem/getSynthesizer))
(def ch (first (.getChannels synth)))
(def sb (.getDefaultSoundbank synth))
(def inst (first (.getInstruments sb)))

(defn play-note [n]
  (.noteOn ch n 600))

(defn notes [start]
  (reductions + start (cycle [2 2 3 2 3])))

(def notes (vec (take 10 (notes 60))))

(def seed (rand-int 997))

(defn map-note [n]
  (play-note (get notes (mod (bit-xor (* n 269) seed) (- (count notes) 1)))))

(defn myGlobalKeyListener []
  (reify
    NativeKeyListener
    (nativeKeyPressed [this event] (map-note (.getKeyCode event)))))

(defn -main [& args]
  (.open synth)
  (.loadInstrument synth inst)

  (GlobalScreen/registerNativeHook)
  (.addNativeKeyListener (GlobalScreen/getInstance) (myGlobalKeyListener)))

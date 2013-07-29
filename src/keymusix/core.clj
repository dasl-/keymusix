(ns keymusix.core
  (:import [javax.sound.midi MidiSystem Synthesizer Soundbank])
  (:import org.jnativehook.GlobalScreen)
  (:import org.jnativehook.keyboard.NativeKeyListener)
  (:gen-class))

(def ^Synthesizer synth (MidiSystem/getSynthesizer))
(def ch (first (.getChannels synth)))

(def playing (atom {}))

(defn play-note [n]
  (when-not (get @playing n)
    (swap! playing assoc n true)
    (.noteOn ch n 600)))

(defn stop-note [n]
  (swap! playing assoc n false)
  (.noteOff ch n))

(defn notes [start]
  (reductions + start (cycle [2 2 3 2 3])))

(def notes (vec (take 10 (notes 60))))

(def seed (rand-int 997))

(defn map-note [n]
  (get notes (mod (bit-xor (* n 269) seed) (- (count notes) 1))))

(defn listen []
  (GlobalScreen/registerNativeHook)
  (.addNativeKeyListener
    (GlobalScreen/getInstance)
    (reify
      NativeKeyListener
      (nativeKeyReleased [this event] (stop-note (map-note (.getKeyCode event))))
      (nativeKeyPressed [this event] (play-note (map-note (.getKeyCode event)))))))

(defn run
  ([nr]
   (.open synth)
   (.programChange ch nr)
   (listen))
  ([^Soundbank soundbank bank nr]
   (.open synth)
   (.loadAllInstruments synth soundbank)
   (.programChange ch bank nr)
   (listen)))


(defn -main
  ([] (run 0))
  ([nr] (run (Integer/parseInt nr)))
  ([soundbank nr] (-main soundbank "0" nr))
  ([soundbank bank nr]
   (run (MidiSystem/getSoundbank (java.io.File. soundbank))
        (Integer/parseInt bank)
        (Integer/parseInt nr))))

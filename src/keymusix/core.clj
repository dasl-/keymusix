(ns keymusix.core 
  (:import javax.sound.midi.MidiSystem)
  (:import org.jnativehook.GlobalScreen)
  (:import org.jnativehook.keyboard.NativeKeyListener))

(def synth (MidiSystem/getSynthesizer))
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

(defn myGlobalKeyListener []
  (reify
    NativeKeyListener
    (nativeKeyReleased [this event] (stop-note (map-note (.getKeyCode event))))
    (nativeKeyPressed [this event] (play-note (map-note (.getKeyCode event))))))

(defn -main [soundbank nr]
  (let [sb (MidiSystem/getSoundbank (java.io.File. soundbank))
        nr (Integer/parseInt nr)]
    (doseq [inst (.getInstruments sb)]
      (println (.toString inst)))
    (.open synth)
    (.loadInstrument
      synth
      (nth (.getInstruments sb) nr))
    (.programChange ch nr)

    (GlobalScreen/registerNativeHook)
    (.addNativeKeyListener (GlobalScreen/getInstance) (myGlobalKeyListener))))

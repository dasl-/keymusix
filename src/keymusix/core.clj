(ns keymusix.core
  (:gen-class)
  (:import java.awt.event.KeyEvent)
  (:use overtone.live)
  (:import org.jnativehook.GlobalScreen)
  (:import org.jnativehook.keyboard.NativeKeyListener))

(definst keyboard [volume 1.0 freq 440]
  (let [src (sin-osc freq)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* volume 1 src env)))

(defn play-keyboard [ notename ]
  (keyboard :freq (* (midi->hz ( note notename)) 4)))

(defn map-note [e]
  (let [k (.getKeyCode e)]
    (cond
      (= KeyEvent/VK_E k) (play-keyboard :c4)
      (= KeyEvent/VK_T k) (play-keyboard :d4)
      (= KeyEvent/VK_A k) (play-keyboard :e4)
      (= KeyEvent/VK_O k) (play-keyboard :g4)
      (= KeyEvent/VK_I k) (play-keyboard :a4)
      (= KeyEvent/VK_N k) (play-keyboard :c3)
      (= KeyEvent/VK_S k) (play-keyboard :d3)
      (= KeyEvent/VK_H k) (play-keyboard :e3)
      (= KeyEvent/VK_R k) (play-keyboard :g3)
      (= KeyEvent/VK_D k) (play-keyboard :a3)
      (= KeyEvent/VK_L k) (play-keyboard :c5)
      (= KeyEvent/VK_C k) (play-keyboard :d5)
      (= KeyEvent/VK_U k) (play-keyboard :e5)
      (= KeyEvent/VK_M k) (play-keyboard :g5)
      (= KeyEvent/VK_W k) (play-keyboard :a5)
      (= KeyEvent/VK_F k) (play-keyboard :c2)
      (= KeyEvent/VK_G k) (play-keyboard :d2)
      (= KeyEvent/VK_Y k) (play-keyboard :e2)
      (= KeyEvent/VK_P k) (play-keyboard :g2)
      (= KeyEvent/VK_B k) (play-keyboard :a2)
      (= KeyEvent/VK_V k) (play-keyboard :c6)
      (= KeyEvent/VK_K k) (play-keyboard :d6)
      (= KeyEvent/VK_J k) (play-keyboard :e6)
      (= KeyEvent/VK_X k) (play-keyboard :g6)
      (= KeyEvent/VK_Q k) (play-keyboard :a6)
      (= KeyEvent/VK_Z k) (play-keyboard :c1)

      (= KeyEvent/VK_0 k) (play-keyboard :c4)
      (= KeyEvent/VK_1 k) (play-keyboard :d4)
      (= KeyEvent/VK_2 k) (play-keyboard :e4)
      (= KeyEvent/VK_3 k) (play-keyboard :g4)
      (= KeyEvent/VK_4 k) (play-keyboard :a4)
      (= KeyEvent/VK_5 k) (play-keyboard :c3)
      (= KeyEvent/VK_6 k) (play-keyboard :d3)
      (= KeyEvent/VK_7 k) (play-keyboard :e3)
      (= KeyEvent/VK_8 k) (play-keyboard :g3)
      (= KeyEvent/VK_9 k) (play-keyboard :a3)

      )
    )
  )


(defn myGlobalKeyListener []
  (reify
    NativeKeyListener
    (nativeKeyPressed [this event] (map-note event))))

(defn -main [& args]
  (GlobalScreen/registerNativeHook)
  (.addNativeKeyListener (GlobalScreen/getInstance) (myGlobalKeyListener)))

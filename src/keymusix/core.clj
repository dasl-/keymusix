(ns keymusix.core
  (:require [clojure.core])
  (:import java.awt.event.KeyEvent)
  (:require [overtone.live :as ot])
  (:import org.jnativehook.GlobalScreen)
  (:import org.jnativehook.keyboard.NativeKeyListener)
  (:use quil.core)
  (:use [quil.applet])
  (:gen-class))

(def halos (atom []))

(def dots (atom []))

(ot/definst keyboard [volume 1.0 freq 440]
  (let [src (ot/sin-osc freq)
        env (ot/env-gen (ot/perc 0.001 0.3) :action ot/FREE)]
    (* volume 1 src env)))

(defn new-halo [x y color]
  (let [
        diameter (+ 15 (rand-int 50))
        stroke-r (rand-int 255)
        stroke-g (rand-int 255)
        stroke-b (rand-int 255)
        stroke-weight (rand-int 10)
        speed (+ 0.3 (rand 1.7))
       ]
    {
     :color color,
     :x x,
     :y y,
     :diameter diameter
     :stroke [stroke-r stroke-g stroke-b]
     :stroke-weight stroke-weight
     :speed speed
     :alpha 255
     }))

(defn new-dot [x y color]
  {:color color
   :x x
   :y y
   :diameter 3})

(defn insert-halo [x y color]
  (swap! halos assoc (count @halos) (new-halo x y color)))

(defn insert-dot [x y color]
  (swap! dots assoc (count @dots) (new-dot x y color)))

(defn play-keyboard [notename]
  (keyboard :freq (* (ot/midi->hz (ot/note notename)) 4))
)

(defn do-key-event [note color]
  (play-keyboard note)

  (let [x (rand-int (screen-width))
        y (rand-int (screen-height))]
    (insert-halo x y color)
    (insert-dot x y color)))

; keycodes: http://code.google.com/p/jnativehook/source/browse/trunk/src/java/org/jnativehook/keyboard/NativeKeyEvent.java
(defn map-note-and-color [e]
  (let [k (.getKeyCode e)]
    (cond
      (= KeyEvent/VK_E k) (do-key-event :c4 [0 93 182])
      (= KeyEvent/VK_T k) (do-key-event :d4 [0 171 174])
      (= KeyEvent/VK_A k) (do-key-event :e4 [54 52 229])
      (= KeyEvent/VK_O k) (do-key-event :g4 [142 179 0])
      (= KeyEvent/VK_I k) (do-key-event :a4 [15 182 0])
      (= KeyEvent/VK_N k) (do-key-event :c3 [102 250 166])
      (= KeyEvent/VK_S k) (do-key-event :d3 [1 83 249])
      (= KeyEvent/VK_H k) (do-key-event :e3 [0 248 166])
      (= KeyEvent/VK_R k) (do-key-event :g3 [0 135 180])
      (= KeyEvent/VK_D k) (do-key-event :a3 [0 143 245])
      (= KeyEvent/VK_L k) (do-key-event :c5 [226 245 0])
      (= KeyEvent/VK_C k) (do-key-event :d5 [99 200 248])
      (= KeyEvent/VK_U k) (do-key-event :e5 [0 182 62])
      (= KeyEvent/VK_M k) (do-key-event :g5 [98 249 104])
      (= KeyEvent/VK_W k) (do-key-event :a5 [2 47 183])
      (= KeyEvent/VK_F k) (do-key-event :c2 [0 198 240])
      (= KeyEvent/VK_G k) (do-key-event :d2 [0 242 232])
      (= KeyEvent/VK_Y k) (do-key-event :e2 [0 183 137])
      (= KeyEvent/VK_P k) (do-key-event :g2 [192 163 0])
      (= KeyEvent/VK_B k) (do-key-event :a2 [97 251 226])
      (= KeyEvent/VK_V k) (do-key-event :c6 [97 231 244])
      (= KeyEvent/VK_K k) (do-key-event :d6 [88 247 0])
      (= KeyEvent/VK_J k) (do-key-event :e6 [0 248 63])
      (= KeyEvent/VK_X k) (do-key-event :g6 [104 161 251])
      (= KeyEvent/VK_Q k) (do-key-event :a6 [43 32 158])
      (= KeyEvent/VK_Z k) (do-key-event :c1 [107 125 245])

      (= KeyEvent/VK_0 k) (do-key-event :a6 [111 108 0])
      (= KeyEvent/VK_1 k) (do-key-event :c5 [45 13 80])
      (= KeyEvent/VK_2 k) (do-key-event :d5 [7 16 102])
      (= KeyEvent/VK_3 k) (do-key-event :e5 [0 40 111])
      (= KeyEvent/VK_4 k) (do-key-event :g5 [0 65 105])
      (= KeyEvent/VK_5 k) (do-key-event :a5 [0 92 104])
      (= KeyEvent/VK_6 k) (do-key-event :c6 [0 107 99])
      (= KeyEvent/VK_7 k) (do-key-event :d6 [0 109 58])
      (= KeyEvent/VK_8 k) (do-key-event :e6 [0 109 2])
      (= KeyEvent/VK_9 k) (do-key-event :g6 [52 107 0])

      (= KeyEvent/VK_ENTER k)         (do-key-event :c4 [255 78 0])
      (= KeyEvent/VK_BACK_SPACE k)    (do-key-event :d4 [114 25 0]) ; delete key on mac
      (= KeyEvent/VK_TAB k)           (do-key-event :e4 [110 26 104])
      (= KeyEvent/VK_SHIFT k)         (do-key-event :g4 [176 122 193])
      ; (= KeyEvent/VK_CONTROL k) unfortunately the control key maps to the same keycode as SHIFT on a mac :(
      ; (= KeyEvent/VK_ALT k) (do-key-event :a3) unfortunately the option/alt key maps to the same keycode as
        ; SHIFT on a mac :(
      (= KeyEvent/VK_META k)          (do-key-event :a4 [233 252 184]) ; maps to the right command key on a mac
        ;(left command key maps to same keycode as SHIFT :( )
      (= KeyEvent/VK_ESCAPE k)        (do-key-event :c3 [21 0 10])
      (= KeyEvent/VK_SPACE k)         (do-key-event :d3 [188 254 239])
      (= KeyEvent/VK_CAPS_LOCK k)     (do-key-event :e3 [147 42 146])

      (= KeyEvent/VK_COMMA k)         (do-key-event :g3 [196 248 101])
      (= KeyEvent/VK_MINUS k)         (do-key-event :a3 [113 84 0])
      (= KeyEvent/VK_PERIOD k)        (do-key-event :c2 [253 238 102])
      (= KeyEvent/VK_SLASH k)         (do-key-event :d2 [255 205 101])
      (= KeyEvent/VK_EQUALS k)        (do-key-event :e2 [111 58 0])
      (= KeyEvent/VK_SEMICOLON k)     (do-key-event :g2 [255 206 0])
      (= KeyEvent/VK_OPEN_BRACKET k)  (do-key-event :a2 [190 124 0])
      (= KeyEvent/VK_BACK_SLASH k)    (do-key-event :c4 [191 37 0])
      (= KeyEvent/VK_CLOSE_BRACKET k) (do-key-event :d4 [190 81 0])
      (= KeyEvent/VK_QUOTE k)         (do-key-event :e4 [255 156 0])
      (= KeyEvent/VK_BACK_QUOTE k)    (do-key-event :g4 [78 9 44])

      (= KeyEvent/VK_UP k)            (do-key-event :c3 [255 200 167])
      (= KeyEvent/VK_RIGHT k)         (do-key-event :d3 [255 212 206])
      (= KeyEvent/VK_DOWN k)          (do-key-event :e3 [254 223 205])
      (= KeyEvent/VK_LEFT k)          (do-key-event :g3 [255 236 206])

    )
  )
)

(defn myGlobalKeyListener []
  (reify
    NativeKeyListener
    (nativeKeyPressed [this event] (map-note-and-color event))))




; quil stuff

(defn update-halo [halo]
  (assoc halo :diameter (+ (:diameter halo) (:speed halo)) :alpha (- (:alpha halo) (:speed halo))))

(defn wait-event-dispatch-thread
  "Blocks current thread until all events in AWT event dispatch thread are processed."
   []
   (javax.swing.SwingUtilities/invokeAndWait (fn [])))

(defn fullscreen []
  (wait-event-dispatch-thread) ; Wait until size set from :size
  (let [screen (.screen (current-applet))]
    (.size (current-applet) (.width screen) (.height screen)))
  (wait-event-dispatch-thread) ; Wait until new size is set
)

(defn setup []
  (smooth)                          ;;Turn on anti-aliasing
  (frame-rate 60)                    ;;Set framerate to 1 FPS
  (background 0)                 ;;Set the background colour to a nice shade of grey.
  (fullscreen)                    ; hack to make it full screen
)

(defn draw []
  (background 0)

  (doseq [halo @halos]
      (apply stroke (conj (:stroke halo) (:alpha halo)))
      (stroke-weight (:stroke-weight halo))
      (apply fill (conj (:color halo) (:alpha halo)))
      (apply ellipse [(:x halo) (:y halo) (:diameter halo) (:diameter halo)]))

  (reset! halos (vec (map update-halo @halos)))
  (reset! halos (vec (filter (fn [halo] (> (:alpha halo) 0)) @halos)))

  (doseq [dot @dots]
    (stroke 0)
    (stroke-weight 0)
    (apply fill (:color dot))
    (apply ellipse [(:x dot) (:y dot) (:diameter dot) (:diameter dot)])))

(defsketch seurat
  :title "may algorithmic beauty pour forth from your fingertips today"
  :setup setup
  :draw draw
)

; end quil stuff

(defn -main [& args]
  (GlobalScreen/registerNativeHook)
  (.addNativeKeyListener (GlobalScreen/getInstance) (myGlobalKeyListener)))

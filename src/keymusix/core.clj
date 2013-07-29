(ns keymusix.core
  (:require [clojure.core])
  (:import java.awt.event.KeyEvent)
  (:require [overtone.live :as ot])
  (:import org.jnativehook.GlobalScreen)
  (:import org.jnativehook.keyboard.NativeKeyListener)
  (:use quil.core)
  (:use [quil.applet])
  (:gen-class))

(def circles (atom []))

(ot/definst keyboard [volume 1.0 freq 440]
  (let [src (ot/sin-osc freq)
        env (ot/env-gen (ot/perc 0.001 0.3) :action ot/FREE)]
    (* volume 1 src env)))

(defn new-circle []
  (let [
        r (rand-int 255)
        g (rand-int 255)
        b (rand-int 255)
        x (rand-int (screen-width))
        y (rand-int (screen-height))
        diameter (+ 15 (rand-int 50))
        stroke-r (rand-int 255)
        stroke-g (rand-int 255)
        stroke-b (rand-int 255)
        stroke-weight (rand-int 10)
        speed (+ 0.3 (rand 1.7))
       ]
    {
     :color [r g b],
     :x x,
     :y y,
     :diameter diameter
     :stroke [stroke-r stroke-g stroke-b]
     :stroke-weight stroke-weight
     :speed speed
     :alpha 255
    }
  )

)

(defn insert-circle []
  (swap! circles assoc (count @circles) (new-circle))
)

(defn play-keyboard [notename]
  (keyboard :freq (* (ot/midi->hz (ot/note notename)) 4))
  (insert-circle)
)


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




; quil stuff

(defn update-circle [circle]
  (assoc circle :diameter (+ (:diameter circle) (:speed circle)) :alpha (- (:alpha circle) (:speed circle))))

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
  (doseq [circle @circles]
      (apply stroke (conj (:stroke circle) (:alpha circle)))
      (stroke-weight (:stroke-weight circle))
      (apply fill (conj (:color circle) (:alpha circle)))
      (apply ellipse [(:x circle) (:y circle) (:diameter circle) (:diameter circle)])
  )

  (reset! circles (vec (map update-circle @circles)))
  (reset! circles (vec (filter (fn [circle] (> (:alpha circle) 0)) @circles)))



  ; (doseq [circle @circles]
  ;     (stroke (get circle :stroke))
  ;     (stroke-weight (get circle :stroke-weight))
  ;     (apply fill (get circle :color))
  ;     (apply ellipse [(get circle :x) (get circle :y) (get circle :diameter) (get circle :diameter)])
  ; )

  ; (stroke (random 255))             ;;Set the stroke colour to a random grey
  ; (stroke-weight (random 10))       ;;Set the stroke thickness randomly
  ; (fill (random 255))               ;;Set the fill colour to a random grey

  ; (let [diam (random 100)           ;;Set the diameter to a value between 0 and 100
  ;       x    (random (width))       ;;Set the x coord randomly within the sketch
  ;       y    (random (height))]     ;;Set the y coord randomly within the sketch
  ;   (ellipse x y diam diam)       ;;Draw a circle at x y with the correct diameter
  ; )
)

; (defn increase-circle-size [circles]
;  (map circles (fn (c) (assoc c :rad (inc (:rad c))) )))

(defsketch example                  ;;Define a new sketch named example
  :title "may algorithmic beauty pour forth from your fingertips today"  ;;Set the title of the sketch
  :setup setup                      ;;Specify the setup fn
  :draw draw                        ;;Specify the draw fn
)

; end quil stuff





(defn -main [& args]
  (GlobalScreen/registerNativeHook)
  (.addNativeKeyListener (GlobalScreen/getInstance) (myGlobalKeyListener)))

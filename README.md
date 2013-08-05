# keymusix

This is an extension of the master branch. It will draw circles to the canvas using Quil upon receiving a keypress event.

## Usage

* `lein run`
* Toggle mute: `escape + m`

On a mac:
* Go to System Preferences
* Select 'Universal Access'
* Check the box for: Enable access for assistive devices.

## Key to color mapping (Mac laptop)
![keyboard key to color map](https://raw.github.com/davidleibovic/keymusix/quil/color_map.png)
Unfortunately, `Control`, `Option/alt`, and `Left Command` all get mapped to the same keycode as `shift`. This is a limitation/bug on the jnativehook key listener library we are using.

## License

Copyright © 2012 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

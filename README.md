# keymusix

This version of keymusix uses the jnativehook library to listen for keyboard events. It uses overtone to produce sounds upon receiving those keyboard events!

It is set by default to produce sine wave tones.

Don't forget to checkout all of the other branches:
* algomap (piano tones)
* midi (any midi bank on your computer tones!)
* quil (an extension of the master branch that uses quil!)

## Usage

`lein run -m keymusix.core`

On a mac:
* Go to System Preferences
* Select 'Universal Access'
* Check the box for: Enable access for assistive devices.

## License

Copyright © 2012 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

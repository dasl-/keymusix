# keymusix

This branch of keymusix also uses jnativehook to listen for keyboard events and produces sound upon receiving those events via overtone. It does not have an explicit mapping of keyboard events to musical notes. Instead it produces a mapping via functions.

It is set by default to play piano notes.

## Usage

`lein run -m keymusix.core`

## License

Copyright © 2012 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

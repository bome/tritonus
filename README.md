[![Release](https://jitpack.io/v/umjammer/tritonus.svg)](https://jitpack.io/#umjammer/tritonus) [![Parent](https://img.shields.io/badge/Parent-vavi--sound--sandbox-pink)](https://github.com/umjammer/vavi-sound-sandbox)

# tritonus

This is mavenized Tritonus

Tritonus is an implementation of the Java Sound API and several 
Java Sound plugins ("service providers"). For pre-compiled
versions of these components, see: 
http://www.tritonus.org/plugins.html

| module        | status | comment | library |
|---------------|:------:|---------|---------|
| share         | ✅    |         | |
| remaining     | ✅    |         | |
| dsp           | ✅    |         | |
| core          | ✅    |         | |
| gsm           | ✅    |         | |
| javasequencer | ✅    |         | |
| jorbis        | ✅    |         | |
| midishare     | ✅    |         | |
| mp3           | ✅    |         | |
| esd           | 🚫    | linux only | libesd |
| alsa          | 🚫    | linux only | libasound |
| vorbis        | ✅    |         | |
| pvorbis       | 🚧    | test | |
| cdda          | 🚫    | linux only | libcdda_interface libcdda_paranoia |
| fluidsynth    | ✅    |         | liblfluidsynth |
| src           | 🚫    |         | |
| aos           | 🚫    |         | |
| saol          | 🚫    |         | |
| timidity      | 🚧    |         | [libtimidity](https://github.com/sezero/libtimidity) |


## License
Tritonus is distributed under the terms of the Apache License,
Version 2.0. See the file LICENSE for details.

### License Exceptions
- the low level GSM code (package org.tritonus.lowlevel.gsm)
  is licensed under the GNU GPL
- BladeMP3EncDLL.h for Windows is licensed under the GNU LGPL.
- the pvorbis lib is licensed under a BSD style license

## Installation
For installation instructions, see the file INSTALL.

For new features and bug fixes, see file NEWS
You may also have a look at the Tritonus page:
http://www.tritonus.org/

Have fun!

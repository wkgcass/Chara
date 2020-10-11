#!/usr/bin/env python2

import os

TO_ADD = '// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info\n\n'

def attach(fname):
    f = open(fname, 'r')
    s = f.read()
    f.close()
    if s.startswith(TO_ADD):
        return
    print ('attach: ' + fname)
    f = open(fname, 'w')
    s = TO_ADD + s
    f.write(s)
    f.flush()
    f.close()

def travel(base):
    files = os.listdir(base)
    for fname in files:
        fullname = base + '/' + fname
        if os.path.isfile(fullname):
            if fname.endswith('.java'):
                attach(fullname)
        elif os.path.isdir(fullname):
            travel(fullname)

travel('.')

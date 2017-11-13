#!/usr/bin/python

import sys
import subprocess

# bare minimum release script, if anything goes wrong you have to handle it manually.

if not '* master\n' in subprocess.check_output(["git", "branch"]):
    sys.stderr.write('This command can only be executed in master.\n')
    sys.exit(2)

returnval = subprocess.call(["mvn", "release:prepare", "-s", "settings.xml", "--batch-mode"])

if returnval != 0:
    sys.exit(returnval)

returnval = subprocess.call(["mvn", "release:perform", "-s", "settings.xml", "--batch-mode"])
sys.exit(returnval)

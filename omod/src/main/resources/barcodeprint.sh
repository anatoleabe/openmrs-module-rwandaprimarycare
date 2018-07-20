#!/bin/bash

# Simple shell script to enable bar code printing on Linux machines

# To use:
# 1) add the Linux user to the lp group
# 2) chmod a+x barcodeprint.sh
# Then, when the web browser asked you what to use to open the downloaded .epl file, open it with this program

cat $1 > /dev/usb/lp0
SUMMARY = "A small image just capable of allowing a device to boot."

IMAGE_INSTALL = "packagegroup-base \
                 util-linux-lsblk  \
                 wireless-regdb \
                 "         

IMAGE_LINGUAS ?= "en-us"

LICENSE = "MIT"

inherit core-image


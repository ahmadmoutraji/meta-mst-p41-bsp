SUMMARY = "A microsensytech image for development purpose"

LICENSE = "MIT"

require mst-core-image.inc

IMAGE_FEATURES += "ssh-server-openssh weston"

IMAGE_INSTALL:append = " polkit  \
                         kmscube \
                       "                   

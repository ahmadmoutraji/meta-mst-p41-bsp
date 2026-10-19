FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

SUMMARY = "Linux kernel for Toradex Freescale i.MX based modules"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM ?= "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

require recipes-kernel/linux/linux-imx.inc

SRC_URI = " \
    git://gitlab.com/mst-p41/linux-toradex.git;protocol=https;branch=${SRCBRANCH};name=machine \
"

S = "${WORKDIR}/git"

KCONFIG_MODE = "--alldefconfig"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

inherit kernel-yocto kernel pkgconfig

export DTC_FLAGS = "-@"

KERNEL_VERSION_SANITY_SKIP = "1"
do_kernel_configcheck[noexec] = "1"

DEPENDS += "bc-native"
COMPATIBLE_MACHINE = "(mx8-nxp-bsp|mx9-nxp-bsp)"

LINUX_VERSION = "6.6.157"
SRCBRANCH = "toradex_mst_6.6-2.2.x-imx"
SRCREV_machine = "9e0dcce00efb3ad7432736f5f5d35923876132bc"


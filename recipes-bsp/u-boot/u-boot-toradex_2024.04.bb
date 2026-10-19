require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"
DEPENDS += " \
    bc-native \
    dtc-native \
    gnutls-native \
    python3-jsonschema-native \
    python3-pyelftools-native \
    python3-pyyaml-native \
    python3-yamllint-native \
"
PROVIDES += "u-boot"
SRCBRANCH = "toradex_imx_mst_v2022.04"
SRCREV = "8d4c900b731bdc72c46072f0f8388607913159c0"
SRC_URI = " \
    git://github.com/ahmadmoutraji/u-boot-toradex.git;protocol=https;branch=${SRCBRANCH} \
"

CVE_PRODUCT = "denx:u-boot"

UBOOT_INITIAL_ENV = "u-boot-initial-env"

# build imx-boot from within U-Boot
inherit ${@oe.utils.ifelse(d.getVar('UBOOT_PROVIDES_BOOT_CONTAINER') == '1', 'imx-boot-container', '')}
# if final boot binary blob is created by U-Boot, create a tagged version
# for wic images
inherit ${@ '' if d.getVar('UBOOT_PROVIDES_BOOT_CONTAINER') == '0' and ('mx8-generic-bsp' in d.getVar('OVERRIDES').split(':') or 'mx9-generic-bsp' in d.getVar('OVERRIDES').split(':')) else 'uuu_bootloader_tag'}

BOOT_TOOLS = "imx-boot-tools"
do_deploy:append:mx8m-generic-bsp() {
    # Deploy u-boot-nodtb.bin and fsl-imx8m*-XX.dtb for mkimage to generate boot binary
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/u-boot-nodtb.bin  ${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${type}
                    uboot_dtb=$(grep "CONFIG_DEFAULT_DEVICE_TREE=" ${B}/${config}/.config | sed 's/CONFIG_DEFAULT_DEVICE_TREE=\"\(.*\)\"/\1.dtb/')
                    if grep -q "^CONFIG_OF_UPSTREAM=y" ${B}/${config}/.config; then
                        install -m 0777 ${B}/${config}/dts/upstream/src/arm64/${uboot_dtb} ${DEPLOYDIR}/${BOOT_TOOLS}
                    else
                        install -m 0777 ${B}/${config}/arch/arm/dts/${uboot_dtb} ${DEPLOYDIR}/${BOOT_TOOLS}
                    fi
                fi
            done
            unset  j
        done
        unset  i
    fi
}

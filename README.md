# meta-mst-p41-bsp

Yocto Project BSP layer for MST P41 using the Toradex SMARC i.MX8M Plus module.
Based on Scarthgap, with Poky as the default distribution.

## Download the BSP sources

Follow the [MST P41 manifest README](https://github.com/ahmadmoutraji/mst-p41-manifest/blob/scarthgap-7.8.0/README.md)
to download the sources into `/work/yocto/scarthgap-mst`.

## Prepare the Docker environment

Git and Docker must be installed on the Linux host. Run the following commands
on the host:

```bash
cd /work/yocto
git clone https://github.com/ahmadmoutraji/yocto-docker-tools.git
cd yocto-docker-tools
sh docker-build-image.sh
sh docker-run.sh
```

## Build the image

Inside the container, run:

```bash
cd /work/yocto/scarthgap-mst
MACHINE=mst-p41-imx8mp source init-build-env.sh mst-p41-build
bitbake mst-image-minimal
```

## Build output

The generated files are available on the host and inside the container at:

```text
/work/yocto/scarthgap-mst/mst-p41-build/tmp/deploy/images/mst-p41-imx8mp/
```

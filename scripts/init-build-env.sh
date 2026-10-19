#!/usr/bin/env bash

# ------------------------------------------------------------
# Yocto Build Setup Script
# ------------------------------------------------------------

# --- Variables -----------------------------------------------------------
BSP="meta-mst-p41-bsp"
CONFIG_PATH="$(pwd)/layers/$BSP/conf/templates"
MST_META_SCRIPTS="$(pwd)/layers/$BSP/scripts"
# --- Usage function -----------------------------------------------------------
usage() {
    echo "Usage: $0 [build-directory]"
    echo "  -h, --help      Show this help and exit"
}

# --- Parameter handling -------------------------------------------------------
if [[ $# -gt 1 ]]; then
    echo "Error: Too many parameters."
    usage
    return 1
fi

build_dir=""

if [[ $# -eq 1 ]]; then
    case "$1" in
        -h|--help)
            usage
            return 0
            ;;
        *)
            build_dir="$1"
            ;;
    esac
fi

# --- Check MACHINE environment variable --------------------------------------
if [[ -z "$MACHINE" ]]; then
    echo "Error: MACHINE environment variable is not set!"
    return 1
fi

# If no parameter was given → build_dir=build-$MACHINE
if [[ -z "$build_dir" ]]; then
    build_dir="build-$MACHINE"
fi

# --- Check if build_dir already exists ---------------------------------------
if [[ -d "$build_dir" ]]; then
    build_dir_needed=false
else
    build_dir_needed=true
fi

# --- Create build_dir if needed ----------------------------------------------
if [[ "$build_dir_needed" == true ]]; then
    echo "Creating build directory: $build_dir"
    mkdir -p "$build_dir" || { echo "Failed to create $build_dir"; return 1; }
fi

# --- Case statement for config_file -------------------------------------
case "$MACHINE" in
    toradex-smarc-imx8mp|mst-p41-imx8mp)
        config_file="$CONFIG_PATH/bblayers.conf.toradex-smarc-imx8mp"
        ;;
    *)
        echo "Error: No config defined for MACHINE='$MACHINE'"
        return 1
        ;;
esac

# --- Create conf directory inside build_dir ----------------------------------
mkdir -p "$build_dir/conf"

# --- Copy config file ---------------------------------------------------------
if [[ ! -f "$config_file" ]]; then
    echo "Error: Config file not found: $config_file"
    return 1
fi

cp "$config_file" "$build_dir/conf/bblayers.conf" || {
    echo "Failed to copy bblayer file."
    return 1
}

cp "$CONFIG_PATH/local.conf.sample" "$build_dir/conf/local.conf" || {
    echo "Failed to copy local config file."
    return 1
}

cat <<EOF >> $build_dir/conf/local.conf
MACHINE = "$MACHINE"
DL_DIR ="\${BSPDIR}/../downloads"
SSTATE_DIR = "\${BSPDIR}/../sstate-cache"
ACCEPT_FSL_EULA = "1"
EOF

source layers/openembedded-core/oe-init-build-env $build_dir > /dev/null

echo "Yocto build setup completed successfully."
echo "Build directory: $build_dir"


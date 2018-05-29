#!/bin/bash

if [ $# -eq 0 ]
  then
    echo "No arguments supplied. convert_svg.sh <svg file to convert>"
    exit
fi

echo "Converting "$@
f=$(basename -s .svg $@)

mkdir -p ../256
mkdir -p ../128
mkdir -p ../64
mkdir -p ../32
mkdir -p ../24
mkdir -p ../16

magick $@ -transparent white -resize 256x256 ../256/$f"_256.png"
magick $@ -transparent white -resize 128x128 ../128/$f"_128.png"
magick $@ -transparent white -resize 64x64 ../64/$f"_64.png"
magick $@ -transparent white -resize 32x32 ../32/$f"_32.png"
magick $@ -transparent white -resize 24x24 ../24/$f"_24.png"
magick $@ -transparent white -resize 16x16 ../16/$f"_16.png"


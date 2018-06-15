#!/bin/bash

if [ $# -eq 0 ]
  then
    echo "No arguments supplied. convert_svg.sh <svg file to convert>"
    exit
fi

echo "Converting "$@
f=$(basename -s .svg $@)

mkdir -p ../128
mkdir -p ../96
mkdir -p ../64
mkdir -p ../32
mkdir -p ../24
mkdir -p ../22
mkdir -p ../20
mkdir -p ../16

convert $@ -transparent white -resize 128x128 ../128/$f".png"
convert $@ -transparent white -resize 96x96 ../96/$f".png"
convert $@ -transparent white -resize 64x64 ../64/$f".png"
convert $@ -transparent white -resize 32x32 ../32/$f".png"
convert $@ -transparent white -resize 24x24 ../24/$f".png"
convert $@ -transparent white -resize 22x22 ../22/$f".png"
convert $@ -transparent white -resize 20x20 ../20/$f".png"
convert $@ -transparent white -resize 16x16 ../16/$f".png"

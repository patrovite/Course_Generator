#!/bin/bash

for i in *.svg
do
    echo "Converting "$i
    f=$(basename -s .svg $i)

    mkdir -p ../128
    mkdir -p ../96
    mkdir -p ../64
    mkdir -p ../32
    mkdir -p ../24
    mkdir -p ../16

    magick $i -transparent white -resize 128x128 ../128/$f"_128.png"
    magick $i -transparent white -resize 96x96 ../96/$f"_96.png"
    magick $i -transparent white -resize 64x64 ../64/$f"_64.png"
    magick $i -transparent white -resize 32x32 ../32/$f"_32.png"
    magick $i -transparent white -resize 24x24 ../24/$f"_24.png"
    magick $i -transparent white -resize 16x16 ../16/$f"_16.png"
   
done




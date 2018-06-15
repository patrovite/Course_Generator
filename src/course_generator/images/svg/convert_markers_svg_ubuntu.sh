#!/bin/bash


mkdir -p ../128
mkdir -p ../96
mkdir -p ../64
mkdir -p ../32
mkdir -p ../24
mkdir -p ../22
mkdir -p ../20
mkdir -p ../16

n="markers_1.svg"

echo "Converting "$n
f=$(basename -s .svg $n)


convert $n -transparent white -resize 128x128 ../128/$f".png"
convert $n -transparent white -resize 96x96 ../96/$f".png"
convert $n -transparent white -resize 64x64 ../64/$f".png"
convert $n -transparent white -resize 32x32 ../32/$f".png"
convert $n -transparent white -resize 24x24 ../24/$f".png"
convert $n -transparent white -resize 22x22 ../22/$f".png"
convert $n -transparent white -resize 20x20 ../20/$f".png"
convert $n -transparent white -resize 16x16 ../16/$f".png"

n="markers_2.svg"

echo "Converting "$n
f=$(basename -s .svg $n)


convert $n -transparent white -resize 128x128 ../128/$f".png"
convert $n -transparent white -resize 96x96 ../96/$f".png"
convert $n -transparent white -resize 64x64 ../64/$f".png"
convert $n -transparent white -resize 32x32 ../32/$f".png"
convert $n -transparent white -resize 24x24 ../24/$f".png"
convert $n -transparent white -resize 22x22 ../22/$f".png"
convert $n -transparent white -resize 20x20 ../20/$f".png"
convert $n -transparent white -resize 16x16 ../16/$f".png"

n="markers_3.svg"

echo "Converting "$n
f=$(basename -s .svg $n)


convert $n -transparent white -resize 256x128 ../128/$f".png"
convert $n -transparent white -resize 192x96 ../96/$f".png"
convert $n -transparent white -resize 128x64 ../64/$f".png"
convert $n -transparent white -resize 64x32 ../32/$f".png"
convert $n -transparent white -resize 48x24 ../24/$f".png"
convert $n -transparent white -resize 44x22 ../22/$f".png"
convert $n -transparent white -resize 40x20 ../20/$f".png"
convert $n -transparent white -resize 32x16 ../16/$f".png"

n="markers_4.svg"

echo "Converting "$n
f=$(basename -s .svg $n)


convert $n -transparent white -resize 128x128 ../128/$f".png"
convert $n -transparent white -resize 96x96 ../96/$f".png"
convert $n -transparent white -resize 64x64 ../64/$f".png"
convert $n -transparent white -resize 32x32 ../32/$f".png"
convert $n -transparent white -resize 24x24 ../24/$f".png"
convert $n -transparent white -resize 22x22 ../22/$f".png"
convert $n -transparent white -resize 20x20 ../20/$f".png"
convert $n -transparent white -resize 16x16 ../16/$f".png"

n="markers_5.svg"

echo "Converting "$n
f=$(basename -s .svg $n)


convert $n -transparent white -resize 256x128 ../128/$f".png"
convert $n -transparent white -resize 192x96 ../96/$f".png"
convert $n -transparent white -resize 128x64 ../64/$f".png"
convert $n -transparent white -resize 64x32 ../32/$f".png"
convert $n -transparent white -resize 48x24 ../24/$f".png"
convert $n -transparent white -resize 44x22 ../22/$f".png"
convert $n -transparent white -resize 40x20 ../20/$f".png"
convert $n -transparent white -resize 32x16 ../16/$f".png"

n="markers_6.svg"

echo "Converting "$n
f=$(basename -s .svg $n)


convert $n -transparent white -resize 256x128 ../128/$f".png"
convert $n -transparent white -resize 192x96 ../96/$f".png"
convert $n -transparent white -resize 128x64 ../64/$f".png"
convert $n -transparent white -resize 64x32 ../32/$f".png"
convert $n -transparent white -resize 48x24 ../24/$f".png"
convert $n -transparent white -resize 44x22 ../22/$f".png"
convert $n -transparent white -resize 40x20 ../20/$f".png"
convert $n -transparent white -resize 32x16 ../16/$f".png"

n="markers_7.svg"

echo "Converting "$n
f=$(basename -s .svg $n)


convert $n -transparent white -resize 384x128 ../128/$f".png"
convert $n -transparent white -resize 288x96 ../96/$f".png"
convert $n -transparent white -resize 192x64 ../64/$f".png"
convert $n -transparent white -resize 96x32 ../32/$f".png"
convert $n -transparent white -resize 72x24 ../24/$f".png"
convert $n -transparent white -resize 66x22 ../22/$f".png"
convert $n -transparent white -resize 60x20 ../20/$f".png"
convert $n -transparent white -resize 48x16 ../16/$f".png"

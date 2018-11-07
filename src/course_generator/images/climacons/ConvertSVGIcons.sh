#!/bin/bash

for filename in ./SVG/*.svg; do
	echo  ${filename%.*}
	convert -background white $filename "${filename%.*}.png"
	convert "${filename%.*}.png" -crop 49x40+26+30 "${filename%.*}.png"
done

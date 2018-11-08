#!/bin/bash

for filename in ./SVG/*.svg; do
	baseFileName=$(basename $filename)
	if [ "$baseFileName" == "Sun.svg" ] ||
		[ "$baseFileName" == "Moon.svg" ] ||
		[ "$baseFileName" == "Cloud-Rain.svg" ] ||
		[ "$baseFileName" == "Cloud-Snow.svg" ] ||
		[ "$baseFileName" == "Wind.svg" ] ||
		[ "$baseFileName" == "Cloud-Fog.svg" ] ||
		[ "$baseFileName" == "Cloud.svg" ] ||
		[ "$baseFileName" == "Sun.svg" ] ||
		[ "$baseFileName" == "Cloud-Sun.svg" ] ||
		[ "$baseFileName" == "Cloud-Moon.svg" ] ||
		[ "$baseFileName" == "Thermometer-Zero.svg" ] ||
		[ "$baseFileName" == "Thermometer-25.svg" ] ||
		[ "$baseFileName" == "Thermometer-50.svg" ] ||
		[ "$baseFileName" == "Thermometer-75.svg" ] ||
		[ "$baseFileName" == "Thermometer-100.svg" ] 
	then
		convert -background white $filename "./PNG/${baseFileName%.*}.png"
		convert "./PNG/${baseFileName%.*}.png" -crop 65x65+20+15 "./PNG/${baseFileName%.*}.png"
fi
done

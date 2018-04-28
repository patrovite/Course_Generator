#!/bin/bash
# -- Course Generator - (c) Pierre Delore
#
# - English html file generator
# -

# Set the output file name
name=en_cg_doc_4.00.html
# Set the language
lang=en

pandoc --standalone --toc --number-sections --epub-cover-image=./images/CG40_Image.png --css cg_doc.css -o $name md/00-Title.md md/01-Introduction.md md/02-Principle.md md/03-Interface.md md/04-Menus.md md/05-Toolbars.md md/06-Statusbar.md md/07-Tabs.md md/08-Use.md md/09-Advanced_use.md md/10-Tools.md

# Create the folders
mkdir -p ../../build/help/$lang
mkdir -p ../../build/help/$lang/images

# Copy images
cp -Ru ./images/* ../../build/help/$lang/images

# copy files
cp -u ./$name ../../build/help/$lang
cp -u ./cg_doc.css ../../build/help/$lang

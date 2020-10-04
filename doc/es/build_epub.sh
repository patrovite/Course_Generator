#!/bin/bash
# -- Course Generator - (c) Pierre Delore
#
# - English epub file generator
# -

# Set the output file name
name=en_cg_doc_4.00.epub
# Set the language
lang=en

pandoc --toc --number-sections --epub-cover-image=./images/CG40_Image.png --css cg_doc.css -o $name md/00-Title.md md/01-Introduction.md md/02-Principle.md md/03-Interface.md md/04-Menus.md md/05-Toolbars.md md/06-Statusbar.md md/07-Tabs.md md/08-Use.md md/09-Advanced_use.md md/10-Tools.md 

# copy files
cp -u ./$name ../../build/

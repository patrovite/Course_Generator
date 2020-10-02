#!/bin/bash
# -- Course Generator - (c) Pierre Delore
#
# - French pdf file generator
# -


# Set the output file name
name=fr_cg_doc_4.00.pdf
# Set the language
lang=fr

pandoc --toc --css cg_doc.css --pdf-engine=xelatex -o $name md/00-Title.md md/01-Introduction.md md/02-Principle.md md/03-Interface.md md/04-Menus.md md/05-Toolbars.md md/06-Statusbar.md md/07-Tabs.md md/08-Use.md md/09-Advanced_use.md md/10-Tools.md

# copy files
cp -u ./$name ../../build/

echo "Done! 'pdf' file generated in the local folder and copied in the 'build' folder"

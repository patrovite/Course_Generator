#!/bin/bash
# Build the zip file

# Change dir
cd ../build

# Add all the files and dir of the "build" dir
zip -r ../distrib/Course_Generator.zip help *.*

# Add the licence file
cd ..
zip -u distrib/Course_Generator.zip gpl-3.0.txt

# Return to the original folder
cd tools

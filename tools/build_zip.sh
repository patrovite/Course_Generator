#!/bin/bash
# Build the zip file

# Change dir
cd ../build

# Add all the files and dir of the "build" dir
zip -r ../distrib/Course_Generator_x.x.x.Linux.MacOS.Windows.portable.zip help *.*
zip -r ../distrib/Course_Generator_x.x.x.Linux.MacOS.Windows.portable.zip course_generator_lib *.*
zip -r ../distrib/Course_Generator_x.x.x.Linux.MacOS.Windows.portable.zip curves *.*

# Add the licence file
cd ..
zip -u distrib/Course_Generator_x.x.x.Linux.MacOS.Windows.portable.zip gpl-3.0.txt

# Return to the original folder
cd tools

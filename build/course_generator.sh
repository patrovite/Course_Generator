#!/bin/bash

# Create an environnement variable to store the current directory (needed by the application)
export CGInstallFolder=$(pwd)
 
# Linux options (taken from freeplane project) - This fix some font anti aliasing problem
 if [ "${JAVA_TYPE}" != "sun" ]; then
  # OpenJDK(7) fixes (don't use OpenJDK6!!)
  JAVA_OPTS="-Dgnu.java.awt.peer.gtk.Graphics=Graphics2D $JAVA_OPTS"

  # this fixes font rendering for some people, see:
  # http://www.freeplane.org/wiki/index.php/Rendering_Issues
  JAVA_OPTS="-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true $JAVA_OPTS"
fi

# Go!
java -jar -Xmx512m $JAVA_OPTS course_generator.jar 

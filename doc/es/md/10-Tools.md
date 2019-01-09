#  Useful tools

##  Altitude correction

The altitude measurements made by a GPS are based, as for the position, on triangulation. Unfortunately, this triangulation is less accurate than for the position. Over long distances, these errors are not negligible and can cause calculation errors on total distance or the total elevation gain.

It is then necessary to carry out a calibration operation of the altitudes. This operation consists of replacing, for each GPS point, the altitude measured by the true altitude.

There are several tools available to do this operation. Among these, GPSVisualizer will fix, for any given GPX file, the altitude of each GPS point and generate an output with the corrected values.

The tool is available at the following address http://www.gpsvisualizer.com/elevation


> Note:  
> **Course Generator** does not contain an elaborate algorithm for filtering altitudes. The only filter used will work only if the elevation sum is greater than a specific threshold (10m). This is designed to hide the small asperities of the terrain like a rock or a tree trunk. The GPS devices being more and more accurate, these asperities are taken into account in the calculations and can create inaccuracies in the different computations.

## Removing unnecessary points

Some tracks, especially when they have been recorded out in the field with a GPS device, contain several thousand points. This can cause problems with some software, websites or GPS.

The GPSVisualizer website smartly reduces the number of points without loss of "information" on the track.

The tool is available at the following address http://www.gpsvisualizer.com/convert_input

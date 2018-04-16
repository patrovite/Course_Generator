#  Useful tools

##  Altitude correction

The altitude measurements made by a GPS are based, as for the position, on a triangulation. Unfortunately this triangulation is less precise than for the position. Over long distances these errors are not negligible and can cause calculation errors on total distance or the total climb.

It is then necessary to carry out a calibration operation of the altitudes. This operation consists of replacing, for each GPS point, the altitude measured by the true altitude.

There are several tools that do this operation. Among these, there is GPSVisualizer which from your GPX file corrects the altitudes and generate an output with the corrected values.

The tool is at the following web address http://www.gpsvisualizer.com/elevation


> Note:  
> **Course Generator** does not contain an elaborate algorithm for filtering altitudes. The only filter present concerns the climb diiference where that only works if there is a variation of altitude higher than a certain threshold (10m). This makes it possible to hide the small asperities of the ground like a rock or a tree trunk. The GPS being more and more precise, these asperities are taken into account in the calculations and come to distort them.

## Removing unnecessary points

Some tracks, especially when they have been recorded in by GPS, contain several thousand points. This can cause problems with some software, websites or GPS.

The GPSVisualizer site smartly reduces the number of points without loss of "information" on the track.

The tool is at the following web address http://www.gpsvisualizer.com/convert_input

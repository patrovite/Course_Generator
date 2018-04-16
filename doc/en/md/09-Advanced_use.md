# Advanced use of Course Generator

This chapter will introduce you to more complete uses of **Course Generator**.

## Find a GPS point

It is possible to search for a GPS point on the track via the "Edit>Search a point..." menu or with the keyboard shortcut [Ctrl + F].

![Search dialog](./images/CG40_Search.png)

In the dialog box, it is necessary to enter the latitude and longitude of the point and then press the search button ![search button](./images/Toolbar/search.png).

**Course Generator** will search for the nearest point of the entered coordinates. It will indicate the line corresponding to the point found as well as the distance between the point found and the coordinates entered. The line containing the found point is then selected.

## Find the high and low points of the track

The "Tools>Find Min/Max" menu is used to automatically find the high and low points of the track.  
Each found position is marked with an indicator ![high point indicator](./images/Tags/high_point.png) (high point) or ![low point indicator](./images/Tags/low_point.png) (low point).

## Change the altitude of a point on the track

It can happen that an altitude of the track is incoherent. This can happen if you, or an Openrunner type website, have made an automatic correction of altitudes. The SRTM database on which these sites are based has area without altitude. The given base then returns an altitude of 32768m. In order to overcome this problem you can edit the altitude of the concerned point(s).

To do this, go to the corresponding cell and open the line editor by double-clicking.

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The "Altitude" field is used to enter the new altitude.

It will be necessary to restart a calculation with the button ![refresh button](./images/Toolbar/refresh_data.png) to take into account the new altitude.

## Merge two track

**Course Generator** offers the opportunity to merge two courses.

The procedure is as follows:

* Load the first track in **Course Generator** (GPX or CGX format).
* Import the second track using "File>Import a GPX File" or "File>Import a CGX File".
* The dialog box below appears:  
![Import track dialog](./images/CG40_Import_Track.png)
* Select "Insert at the beginning" if you want to insert the track before the one present in memory. If not select "Append" and the track will be added after the current track in memory.
* The file selection dialog box appears. Make your choice then click on "Open"
* The merge is then realized

If you have other tracks to merge, simply repeat the process.

After the merge of the tracks you will have to modify the settings of the fatigue coefficients and restart a computation (butotn ![refresh button](./images/Toolbar/refresh_data.png) or [F5]) because the time data will be wrong.

Once you have completed the merge and restarted a calculation, you will be able to save the track.

## Save part of the track

**Course Generator** offers the opportunity to save part of a track.  

The procedure is as follows:

* In the table select the first line you want to save,
* While holding down the SHIFT key, select the last line to save.
* Select "File>Save selection as xxx" (xxx corresponds to the file format you want).
* The file save dialog box appears. Enter the file name and confirm.
* It's finish!

## Export tags as waypoints

**Course Generator** offers the ability to save GPS points containing tags (or indicator) as a waypoint. These waypoints can be added to your GPS to have, for example, the display of the track with additional information such as passes, aid stations and cities.

The procedure is as follows:

* Select "File>Export tags as waypoint".
* The dialog box below appears:  
![Export as waypoints dialog](./images/CG40_Export_Waypoints.png)
* Select the types of tags you want to export.
* The save dialog box appears. 
* Waypoints will be saved to a file that has the name entered and with a GPX extension.

The operation of this file can be done, for example, with Garmin's Basecamp software.

## Copy the contents of a cell

The contents of a cell in the track table can be copied to the clipboard for use in other software. To perform this action simply select "Edit>Copy". The content of the selected cell is copied to the clipboard as text.

## Generate a mini roadbook

**Course Generator** offers the possibility to generate a mini roadbook. This will contain the profile of the track and informations on your way points. The mini roadbook is accessible via "Display>Generate mini roadbook".

At the end of the process the mini-roadbook is an image. This image can be printed using drawing software such as Paint, Photoshop or The Gimp. It can also be used in other software like Word, Excel, Inkscape, Illustrator ...

The settings made to generate the mini roadbook are saved in the CGX file of the track. If you exchange a track in CGX format, you will exchange the track with all its informations (refueling, field quality, cut-off times ...) and the mini roadbook.

To be able to use the mini roadbook it is necessary to have previously set the parameters of your track. This includes:

* Have entered the field difficulty (useful for the type "Roads/Trails").
* Fill in the fatigue coefficient, refueling times, cut-off times, names of important points.
* Have the calculation updated (F5 key).
* Have marked the important positions with the indicator ![mark indicator](./images/Tags/roadbook.png).
* Have marked the important points with one or more of the following indicators: ![hight point](./images/Tags/high_point.png) ![low point](./images/Tags/low_point.png) ![eat](./images/Tags/eat.png) ![drink](./images/Tags/drink.png) ![flag](./images/Tags/flag.png)


### Presentation

"Display>Generate mini roadbook" or the button ![mrb](./images/Toolbar/mrb.png)displays the following window:  
![Mini raodbook dialog](./images/MRB/CG40_MRB_Global_Simple_Nigh_Day.png)

It contains the following elements:

* Top: the icon bar to perform actions
* Middle right: Contains a table with all the rows of the track table that contained the indicator ![roadbook](./images/Tags/roadbook.png) and one or more of the following indicators: ![](./images/Tags/high_point.png) ![](./images/Tags/low_point.png) ![](./images/Tags/eat.png) ![](./images/Tags/drink.png) ![](./images/Tags/flag.png). Each line generates a label in the mini roadbook
* Middle left: This area allows you to change the content of the currently selected table row
* Bottom: Contains the mini roadbook with tags

When a line is selected then the corresponding label color change (salmon color).

### The types of mini roadbook

In the top bar, the "Profile type" drop-down list allows you to choose among the 3 types of profile:

The type "Simple":  
![Simple type](./images/MRB/CG40_MRB_Simple_Label.png)    
The track profile does not contain any additional information.

The type "Roads/Trails":  
![Road/trails type](./images/MRB/CG40_MRB_Road_Track_Label.png)  
The profile highlights the portions of the roads and trails with a color code. The roads are the points of the track table whose field coefficient is equal to 100%. The others are considered as trails.

The type "Slope":  
![Slope type](./images/MRB/CG40_MRB_Slope_Label.png)  
The profile highlights the degree of slope by a color code.

The colors used in the mini roadbook can be set in the configuration window accessible with the button ![](./images/MRB/Toolbar/settings.png).

### The icon bar

![](./images/MRB/CG40_MRB_Toolbar.png)  


* ![save](./images/MRB/Toolbar/save.png) : Save the mini roadbook as an image. The available format is PNG.
* ![settings](./images/MRB/Toolbar/settings.png) : Opens the mini roadbook configuration window
* ![](./images/MRB/Toolbar/pipette.png) : Copy the current label format to reproduce it on one or more other labels. This function is also accessible with the keyboard shortcut CTRL + C
* ![](./images/MRB/Toolbar/replicate.png) :  Paste the formatting on the selected label. Only properties selected in the function's configuration window will be pasted. This function is also accessible with the keyboard shortcut CTRL + V
* ![](./images/MRB/Toolbar/replicate_config.png) : Opens a window for configuring the duplicate formatting feature. This opens the next window:  
![Duplicate configuration dialog](./images/MRB/CG40_MRB_Replicate_Dlg.png)    
Select the settings you want to use when copying.

* ![](./images/MRB/Toolbar/label_to_bottom.png) : Allows you to specify that labels must be connected at the bottom of the profile.
* ![](./images/MRB/Toolbar/label_to_profil.png) : Allows you to specify that labels must be connected to the profile.
* ![](./images/MRB/Toolbar/night_day.png) : Specify whether to show the day and night areas on the profile.
* "Profile type" : Allows you to select the type of mini roadbook.
    * Simple
    * With roads/trails
    * With slope
* ![](./images/MRB/Toolbar/favoris1.png) ![](./images/MRB/Toolbar/favoris2.png) ![](./images/MRB/Toolbar/favoris3.png) ![](./images/MRB/Toolbar/favoris4.png)
![](./images/MRB/Toolbar/favoris5.png) : These 5 buttons are used to store display formats.
    * A right click on a button memorizes in the button the setting displayed in the current format field.
    * A left click on a button restores the format stored in the button in the format field.
* "Width" : Selects the width of the profile (Maximum value 4000).
* "Height" : Allows you to select the height of the profile (Maximum value 2000).


### Set the size of the mini roadbook

The maximum size of the mini roadbook is 4000x2000 pixels (Width x Height). It is also the size of the final image.

The size setting is made by the "Width" and "Height" fields located in the icon bar.

### Configure the mini roadbook

The icon ![](./images/MRB/Toolbar/settings.png) opens the configuration window.

The "General" tab allows you to:  
![General tab](./images/MRB/CG40_MRB_Settings1.png)

* Set the filter value to apply to the data when displaying the profile
* Set the top area height of the profile. This size is in pixel.

The "Simple" tab is used to adjust the colors of the "Simple" display of the profile.  
![Simple tab](./images/MRB/CG40_MRB_Settings2.png)  
The "Default Colors" button is used to replace the current colors with the default colors.

The "Roads/Trails" tab is used to adjust the colors of the "Roads/Trails" display of the profile.  
![Roads/trails tab](./images/MRB/CG40_MRB_Settings3.png)  
The "Default Colors" button is used to replace the current colors with the default colors.

The "Slope" tab is used to adjust the colors of the "Slope" display of the profile.  
![Slope tab](./images/MRB/CG40_MRB_Settings4.png)  
The "Default Colors" button is used to replace the current colors with the default colors.

### Configure each label

Each line of the table represents a label. It can be set by the setting items on the left of the table.  
![MRB editor](./images/MRB/CG40_MRB_Editor.png)

* Selected : Indicate if you want to take this label into account in the profile. If the box is unchecked then the table row is grayed out and the label is not displayed in the mini roadbook.
* Position : Adjusts the vertical position of the label. The number indicates the number of pixels relative to the highest position of the label.
* Alignment : Used to define the position of the label relative to the line connecting it to the profile. To the left of the line, centered on the line or on the right of the line.
* Format : Allows you to specify the contents of the label ([see below](#format)).
* Size : Sets the size of the font used in the labels.
* Tags : Indicate if you want the indicators to be displayed in the labels.
* Comment : Enter a specific comment for the label. This comment is different from the main table comment.

### Set the display format for labels {#format}

To ensure optimal flexibility the format of labels uses tags. These tags represented by the sign "%" plus one or more characters, allow to specify the type of data to display. For example, "% N" represents the name of the point. When the tag is displayed, the tags are replaced by their meanings.

The following tags are available:

* %N : Represents the name of the point.
* %A : Represents the altitude of the point. The unit is meter/feet.
* %D : Represents the distance since the start. The unit is kilometer/miles.
* %T : Represents travel time since the start. The format is "hh:mm".
* %Tl : Represents the travel time since the start in long format "hh:mm:ss".
* %Ts : Represents travel time since the start in short format "hh:mm".
* %H : Represents the time at this point. The format is "ddd hh:mm" (ddd = abbreviated day).
* %h : Represents the time at this point. The format is "hh:mm".
* %hl :Represents the time at this point in long format "hh:mm:ss".
* %hs :Represents the time at this point in short format "hh:mm".
* %B : Represents the cut-off time at this point (time). The format is "hh:mm".
* %b : Represents the cut-off time at this point (hour). The format is "hh:mm".
* %C : Represents the specific comment on the label (Entered in the "Comment" field of the label).
* %c : Represents the comment from the main table.
* %L : Insert a line break.
* %R : Represents refueling time this point. The format is "hh:mm".
* %Rl : Represents refueling time this point in long format "hh:mm:ss".
* %Rs : Represents refueling time this point in short format "hh:mm".
* %+ : Represents the cumulative positive climb since the start. The unit is the meter/feet.
* %- : Represents the cumulative negative climb since the start. The unit is the meter/feet. 

The "..." button, next to the "Format" field, opens a window allowing you to simplify the content of the selected label.

![Label editor dialog](./images/MRB/CG40_MRB_Label_Editor.png)

### Showing days and nights zone

If in the track settings, the night effect has been activated then the button ![](./images/MRB/Toolbar/night_day.png) select the display of days and Nights zones.

![Days and nights zones](./images/MRB/CG40_MRB_Global_Simple_Nigh_Day.png)  
The nights zones are displayed with a gray background.

## Reverse track direction

**Course Generator** offers is able to reverse the direction of the track. To perform this action select "Tools>Reverse track".
After the operation, it is necessary to redefine the fatigue coefficients and cut-off time and then restart a calculation (button ![](./images/Toolbar/refresh_data.png) or [F5]).

## Set a new starting point on a looped track

**Course Generator** offers the possibility, if the track loaded in memory is a loop, to define a new starting point.

The procedure is as follows:

* Select the position that will become your new starting point.
* Select "Tools>Define a new start".
* A following dialog box will appear:  
![Confirm new start dialog](./images/CG40_Dlg_Confirm_New_Start.png)
* Confirm the order by clicking on "Yes".

After the operation it is necessary to redefine the fatigue coefficients and cut-off times and then restart a calculation (button ![](./images/Toolbar/refresh_data.png) or [F5]).

## The general parameters of Course Generator

The "Settings>Course Generator Settings" menu displays the following window:  
![Global settings dialog](./images/CG40_Settings.png)

The possible settings are:

* "language" : Selects the language used in the interface. "System" uses operating system settings to determine which language to use. If the system language is not managed by **Course Generator** then English is selected.
* "Units" : Choose between "km/m" and "Miles/Pieds".
* "Speed format" : Choose the type of speed for the display (speed or pace).

## Import and export marked points

In some cases it is necessary to save only the marked points of the current track in a file. This allows if you have a new version of the course to be able to import these points on this track.

For example:  
You have the track of the UTMB. You've spent time spotting each pass, aid station, and you've entered comments on parts of the track. Unfortunately, the tracke of the following year is slightly different but the main points are the same. The import/export function of marked points will save you a lot of time.

The procedure is as follows:

* Take the course of the previous year
* Export marked points with "File>Export points"
* Select the types of points to export in the following dialog box:    
![Export points dialog](./images/CG40_Export_Points.png)
* Validate your selection and enter the file name (extension '.CGP')
* Open the course of the following year
* Import points with "File>Import points"
* Select the file (extension .CGP) to import.
* The following dialog box appears:    
![Import point dialog](./images/CG40_Import_Points.png)
    * The column "Dist" indicates the distance in meter between the point found in the track and the point to be imported. The green background color indicates that the point found is at less than 100m, a yellow color indicates that the point is between 100m and 1000m and a red color indicates that the point is at more than 1000m. If the distance is high this indicates that the new course does not pass through this point. 
    * The column "Line" indicates the line of the track where the found point is
    * The column "Sel." select the points to import. "X" indicates that the line is selected.
* Select the points to import and click on "Import"

## Analyze data after a race

**Course Generator** allows you to analyze the data after completing the track. Just open the file containing the GPS data (often a GPX file). You will find in the table all your data. Reports allow you to have information that data.

The data will remain unchanged until you ask for a calculation of the travel time. A window will ask you if you want to overwrite the temporal data.

## Use of the map features

**Course Generator** show the course on an OpenStreetMap map.  
![Map](./images/Map/CG40_Map_Area.png)

On the right, a vertical bar of buttons allows actions on this map.

* ![](./images/Map/marker.png) : Adds an start mark to the selected location
* ![](./images/Map/hide_marker.png) : Delete the mark
* ![](./images/Map/undo.png) : Cancel the last operation
* ![](./images/Map/track_very_easy.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Very easy"
* ![](./images/Map/track_easy.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Easy"
* ![](./images/Map/track_average.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Average"
* ![](./images/Map/track_hard.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Hard"
* ![](./images/Map/track_very_hard.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Very hard".
* ![](./images/Map/flag.png) : Adds a mark to the current point.
* ![](./images/Map/eat.png) : Adds an aid station at the current point.
* ![](./images/Map/drink.png) : Adds a water point to the current point.
* ![](./images/Map/select_map.png) : Allows you to select the type of map to use.

The mouse commands are as follows:

* Left-click on the map set the marker on track (nearest position).
* An holded left click move the map.
* A double click on the map allows you to zoom on the selected point.


To change the quality of the field for a part of the track, you must:

* Position the cursor at the beginning of the area to change
* Click on the button ![](./images/Map/marker.png) to set the marker
* Position the cursor at the end of the area to change
* Click on the button corresponding to the required field quality (for example ![](./images/Map/track_average.png))

In the status bar the indicator ![](./images/Statusbar/CG40_Statusbar_Map_Size.png) indicates the disk size used by the cards. The menu "Tools>Open 'Speed/slope' folder" open the file manager and display the contents of the directory containing the curves, the logs and the directory containing the maps. The directory "OpenStreetMapTileCache" contains the map files. If necessary you can delete the content to save space.

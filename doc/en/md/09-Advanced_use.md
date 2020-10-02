# Advanced use of Course Generator

This chapter will introduce you to more advanced uses of **Course Generator**.

## Find a GPS point

It is possible to search for a GPS point on the track via the "Edit>Search a point..." menu or with the keyboard shortcut [Ctrl + F].

![Search dialog](./images/CG40_Search.png)

In the dialog box, it is necessary to enter the latitude and longitude of the point and then press the search button ![search button](./images/Toolbar/search.png).

**Course Generator** will search for the closest point given a set of coordinates. It will indicate the line corresponding to the point found as well as the distance between the point found and the coordinates entered. The line containing the found point will then be selected.

## Find the high and low points of the track

The "Tools>Find Min/Max" menu is used to automatically find the highest and lowest point of the track.  
Each found position is marked with an indicator ![high point indicator](./images/Tags/high_point.png) (high point) or ![low point indicator](./images/Tags/low_point.png) (low point).

## Change the altitude of a point on the track

It can happen that the elevation data of a track is inaccurate. This can happen if you, or an Openrunner type website, have made an automatic correction of altitudes. The SRTM database on which these sites are based has area without altitude. The given base then returns an altitude of 32768m. In order to overcome this problem you can edit the altitude of the erroneous point(s).

To do this, go to the corresponding cell and open the line editor by double-clicking.

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The "Altitude" field is used to enter the new altitude.

It will be necessary to restart a calculation with the button ![refresh button](./images/Toolbar/refresh.png) to take into account the new altitude.

## Merge two tracks

**Course Generator** offers the opportunity to merge two tracks.

The procedure is as follows:

* Load the first track in **Course Generator** (GPX or CGX format).
* Import the second track using "File>Import a GPX File" or "File>Import a CGX File".
* The dialog box below appears:  
![Import track dialog](./images/CG40_Import_Track.png)
* Select "Insert at the beginning" if you want to insert the track before the one present in memory. If not, select "Append" and the track will be added after the current loaded track.
* The file selection dialog box appears. Make your choice then click on "Open"
* The merge is then realized

If you have other tracks to merge, simply repeat the process.

After merging the tracks you will have to modify the settings of the fatigue coefficients and restart a computation (button ![refresh button](./images/Toolbar/refresh.png) or [F5]) because the time data will be wrong.

Once you have completed the merge and restarted a calculation, you will be able to save the track.

## Save part of the track

**Course Generator** allows to save a section of a track.  

The procedure is as follows:

* In the table select the first line you want to save,
* While holding down the SHIFT key, select the last line to save.
* Select "File>Save selection as xxx" (xxx corresponds to the file format you want).
* The file save dialog box appears. Enter the file name and confirm.
* It's done!

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

The settings made to generate the mini roadbook are saved in the CGX file of the track. If you exchange a track in CGX format, you will exchange the track with all its informations (refueling, terrain quality, cut-off times ...) and the mini roadbook.

To be able to use the mini roadbook it is necessary to have previously set the parameters of your track. This includes:

* Having entered the terrain difficulty (useful for the type "Roads/Trails").
* Fill in the fatigue coefficient, refueling times, cut-off times, names of important points.
* Having the calculation updated (F5 key).
* Having marked the important positions with the indicator ![mark indicator](./images/Tags/roadbook.png).
* Having marked the important points with one or more of the following indicators: ![hight point](./images/Tags/high_point.png) ![low point](./images/Tags/low_point.png) ![eat](./images/Tags/eat.png) ![drink](./images/Tags/drink.png) ![flag](./images/Tags/flag.png) ![](./images/Tags/dropbag.png) ![](./images/Tags/crew.png) ![](./images/Tags/first_aid.png)


### Presentation

"Display>Generate mini roadbook" or the button ![mrb](./images/Toolbar/roadbook.png) displays the following window:  
![Mini raodbook dialog](./images/MRB/CG40_MRB_Global_Simple_Nigh_Day.png)

It contains the following elements:

* Top: the toolbar to perform actions
* Middle right: Contains a table with all the rows of the track table that contained the indicator ![roadbook](./images/Tags/roadbook.png) and one or more of the following indicators: ![](./images/Tags/high_point.png) ![](./images/Tags/low_point.png) ![](./images/Tags/eat.png) ![](./images/Tags/drink.png) ![](./images/Tags/flag.png) ![](./images/Tags/dropbag.png) ![](./images/Tags/crew.png) ![](./images/Tags/first_aid.png). Each line generates a label in the mini roadbook
* Middle left: This area allows you to change the content of the currently selected table row
* Bottom: Contains the mini roadbook with tags

When a line is selected then the corresponding label color change (salmon color).

### The types of mini roadbook

In the top bar, the "Profile type" drop-down list allows you to choose among the 3 types of profile:

The "Simple" type:  
![Simple type](./images/MRB/CG40_MRB_Simple_Label.png)    
The track profile does not contain any additional information.

The "Roads/Trails" type:  
![Road/trails type](./images/MRB/CG40_MRB_Road_Track_Label.png)  
The profile highlights the portions of the roads and trails with a color code. The roads are the points of the track table whose field coefficient is equal to 100%. The others are considered as trails.

The "Slope" type :  
![Slope type](./images/MRB/CG40_MRB_Slope_Label.png)  
The profile highlights the degree of slope by a color code.

The colors used in the mini roadbook can be set in the configuration window accessible with the button ![](./images/MRB/Toolbar/setting.png).

### The toolbar

![](./images/MRB/CG40_MRB_Toolbar.png)  


* ![save](./images/MRB/Toolbar/save.png) : Saves the mini roadbook as an image. The available format is PNG.
* ![settings](./images/MRB/Toolbar/setting.png) : Opens the mini roadbook configuration window
* ![](./images/MRB/Toolbar/pipette.png) : Copies the current label format to reproduce it on one or more other labels. This function is also accessible with the keyboard shortcut CTRL + C
* ![](./images/MRB/Toolbar/replicate.png) :  Paste the formatting on the selected label. Only the properties selected in the function's configuration window will be pasted. This function is also accessible with the keyboard shortcut CTRL + V
* ![](./images/MRB/Toolbar/replicate_config.png) : Opens a window for configuring the duplicate formatting feature as shown below:
![Duplicate configuration dialog](./images/MRB/CG40_MRB_Replicate_Dlg.png)    
Select the settings you want to duplicate when copying.

* ![](./images/MRB/Toolbar/label_to_bottom.png) : Allows you to specify that labels must be connected to the bottom of the profile.
* ![](./images/MRB/Toolbar/label_to_profil.png) : Allows you to specify that labels must be connected to the profile.
* ![](./images/MRB/Toolbar/night_day.png) : Specify whether to show the day and night areas on the profile.
* "Profile type" : Allows you to select the type of mini roadbook.
    * Simple
    * With roads/trails
    * With slope
* ![](./images/MRB/Toolbar/favori1.png) ![](./images/MRB/Toolbar/favori2.png) ![](./images/MRB/Toolbar/favori3.png) ![](./images/MRB/Toolbar/favori4.png)
![](./images/MRB/Toolbar/favori5.png) : These 5 buttons are used to store display formats.
    * A right click on a button memorizes in the button the setting displayed in the current format field.
    * A left click on a button restores the format stored in the button in the format field.
* "Width" : Selects the width of the profile (Maximum value 4000 pixels).
* "Height" : Allows you to select the height of the profile (Maximum value 2000 pixels).


### Set the size of the mini roadbook

The maximum size of the mini roadbook is 4000x2000 pixels (Width x Height). It is also the size of the final image.

The size setting is made by the "Width" and "Height" fields located in the icon bar.

### Configure the mini roadbook

The icon ![](./images/MRB/Toolbar/setting.png) opens the configuration window.

The "General" tab allows you to:  
![General tab](./images/MRB/CG40_MRB_Settings1.png)

* Set the filter value to apply to the data when displaying the profile
* Set the top area height of the profile. This size is in pixel.
* Set the default text format when you create a new mark

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

Each line of the table represents a label. It can be configured by the setting items on the left of the table.  
![MRB editor](./images/MRB/CG40_MRB_Editor.png)

* Selected : Indicates if you want to take this label into account in the profile. If the box is unchecked then the table row is grayed out and the label is not displayed in the mini roadbook.
* Position : Adjusts the vertical position of the label. The number indicates the number of pixels relative to the highest position of the label.
* Alignment : Used to define the position of the label relative to the line connecting it to the profile. To the left of the line, centered on the line or on the right of the line.
* Format : Allows you to specify the contents of the label ([see below](#format)).
* Size : Sets the size of the font used in the labels.
* Tags : Indicate if you want the indicators to be displayed in the labels.
* Comment : Enter a specific comment for the label. This comment is different from the main table comment.

### Set the display format for labels {#format}

To ensure optimal flexibility, the format of labels uses tags. These tags, represented by the sign "%" plus one or more characters, allow to specify the type of data to display. For example, "% N" represents the name of the point. When the tag is displayed, the tags are replaced by their meanings.

The following tags are available:

* %N : Represents the name of the point.
* %A : Represents the altitude of the point. The unit is meter/feet.
* %D : Represents the distance since the start. The unit is kilometer/miles.
* %T : Represents the travel time since the start. The format is "hh:mm".
* %Tl : Represents the travel time since the start in long format "hh:mm:ss".
* %Ts : Represents the travel time since the start in short format "hh:mm".
* %Td : Represents the travel time since the last location in short format "hh:mm".
* %H : Represents the time at this point. The format is "ddd hh:mm" (ddd = abbreviated day).
* %h : Represents the time at this point. The format is "hh:mm".
* %hl : Represents the time at this point in long format "hh:mm:ss".
* %hs : Represents the time at this point in short format "hh:mm".
* %B : Represents the cut-off time at this point (time). The format is "hh:mm".
* %b : Represents the cut-off time at this point (hour). The format is "hh:mm".
* %C : Represents the specific comment on the label (Entered in the "Comment" field of the label).
* %c : Represents the comment from the main table.
* %L : Inserts a line break.
* %R : Represents refueling time this point. The format is "hh:mm".
* %Rl : Represents refueling time this point in long format "hh:mm:ss".
* %Rs : Represents refueling time this point in short format "hh:mm".
* %+ : Represents the cumulative positive climb since the start. The unit is the meter/feet.
* %- : Represents the cumulative negative climb since the start. The unit is the meter/feet. 
* %+d : Represents the positive climb since the location. The unit is the meter/feet.
* %-d : Represents the negative climb since the location. The unit is the meter/feet. 

The "..." button, next to the "Format" field, opens a window allowing you to simplify the content of the selected label.

![Label editor dialog](./images/MRB/CG40_MRB_Label_Editor.png)

### Showing day and night times

If in the track settings the night effect has been activated, then the button ![](./images/MRB/Toolbar/night_day.png) will display the day and night times.

![Day and night times](./images/MRB/CG40_MRB_Global_Simple_Nigh_Day.png)  
The night times are displayed with a gray background.

## Reverse track direction

**Course Generator** can reverse the direction of any given track. To perform this action select "Tools>Reverse track".
After the operation, it is necessary to redefine the fatigue coefficients and cut-off time and then restart a calculation (button ![](./images/Toolbar/refresh.png) or [F5]).

## Set a new starting point on a looped track

**Course Generator** offers the possibility, if the track loaded in memory is a loop, to define a new starting point.

The procedure is as follows:

* Select the position that will become your new starting point.
* Select "Tools>Define a new start".
* A following dialog box will appear:  
![Confirm new start dialog](./images/CG40_Dlg_Confirm_New_Start.png)
* Confirm the order by clicking on "Yes".

After the operation it is necessary to redefine the fatigue coefficients and cut-off times and then restart a calculation (button ![](./images/Toolbar/refresh.png) or [F5]).

## Find the best "Speed/Slope" curve from the final time

Le menu "Tools>Search "Speed/Slope" curve from the final time" allow to find the best curve from the final time.

![](./images/CG40_Search_Curve.png)

You enter the final time (hour:minute:second) then you press the search button.

**Course Generator** will search the best curve. Most of the time the software will not find the exact curve and it will show the curve bellow and over your final time. For every curve, you will have the estimated time.
The you can :

* Select the curve bellow the final time withe the "Select" button
* Select the curve over the final time withe the "Select" button
* Exit from the dialog box with the "Cancel" button

The press o one "Select" button will select the the corresponding curve. It will be used for your next calculation.


## The general parameters of Course Generator

Le menu "Paramètres>Paramètres de Course Generator" affiche la fenêtre de configuration du logiciel.
The "Settings>Course Generator Settings" menu displays the software configuration window.

### "General" tab  
![](./images/CG40_Settings_General.png)

The possible settings are:

* "language" : Selects the language used in the interface. "System" uses operating system settings to determine which language to use. If the system language is not managed by **Course Generator** then English is selected.
* "Units" : Chooses between "km/m" and "Miles/Feet".
* "Speed format" : Chooses the type of speed to display (speed or pace).
* "Threshold for position filter (in %)" : Threshold, in %, from which the software asks if we want to apply a filter on GPS points when loading a track.
* "Threshold for climb calculation (in meter)" : Threshold, in meter, from where you take into account a difference of elevation. Used the by the software to calcultate the ascending and descending climb.
* "Check for update at startup" : Allows you to choose if you want to check for a newer version of **Course Generator** available at application startup.

### "Display" tab  
![](./images/CG40_Settings_Display.png)

The possible settings are:

* "Default font" : Allows you to choose the font that will be used for the display.
* Icons size : Specify the size of the interface icons.

### "Maps" tab  
![](./images/CG40_Settings_Maps.png)

Provides a field to enter a Thunderforest API key in order to show the Outdoors map layer. A free API key can be requested [here](https://thunderforest.com/docs/apikeys/).

## Import and export marked points

In some cases it is necessary to save only the marked points of the current track in a file. This allows, if you have a new version of the course, to import these points on the new track.

For example:  
You have the track of the UTMB. You've spent time scouting each pass, aid station, and you've entered comments on parts of the track. Unfortunately, the track of the following year is slightly different but the main points are the same. The import/export function of marked points will save you a lot of time.

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
    * The column "Dist" indicates the distance between the point found in the track and the point to be imported. The green background color indicates that the point found is less than 100m, a yellow color indicates that the point is between 100m and 1000m and a red color indicates that the point is more than 1000m. If the distance is high, this indicates that the new course does not pass through this point. 
    * The column "Line" indicates the line of the track where the found point is
    * The column "Sel." select the points to import. "X" indicates that the line is selected.
* Select the points to import and click on "Import"

## Analyzing data after a race

**Course Generator** allows you to analyze the data after completing the track. Just open the file containing the GPS data (often a GPX file). You will find in the table all your data. Reports allow you to have information that data.

The data will remain unchanged until you ask for a calculation of the travel time. A window will ask you if you want to overwrite the temporal data.

## Using the map features

**Course Generator** displaying a course on an OpenStreetMap map.  
![Map](./images/Map/CG40_Map_Area.png)

On the left, a vertical bar of buttons allows actions on this map.

* ![](./images/Map/center.png) : Centers the track on the map
* ![](./images/Map/marker.png) : Adds a start mark to the selected location
* ![](./images/Map/hide_marker.png) : Deletes the mark
* ![](./images/Map/undo.png) : Cancels the last operation
* ![](./images/Map/track_very_easy.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Very easy"
* ![](./images/Map/track_easy.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Easy"
* ![](./images/Map/track_average.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Average"
* ![](./images/Map/track_hard.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Hard"
* ![](./images/Map/track_very_hard.png) : Indicates that the field between ![](./images/Map/marker.png) and the current point is "Very hard".
* ![](./images/Map/flag.png) : Adds a mark to the current point.
* ![](./images/Map/eat.png) : Adds an aid station to the current point.
* ![](./images/Map/drink.png) : Adds a water point to the current point.
* ![](./images/Map/show_hide_markers.png) : Display or hide the marks.
* ![](./images/Map/save_png.png) : Save the current map as a PNG image.
* ![](./images/Map/select_map.png) : Allows you to select the map layer to be displayed.

The mouse commands are as follows:

* Left-click on the map sets the marker on track (nearest position).
* Holding the left mouse button moves the map.
* A double click on the map allows you to zoom in.


To change the quality of the terrain for a part of the track, you must:

* Position the cursor at the beginning of the area to be modified.
* Click on the button ![](./images/Map/marker.png) to set the marker.
* Position the cursor at the end of the area to change.
* Click on the button corresponding to the required field quality (for example ![](./images/Map/track_average.png)).

In the status bar, the indicator ![](./images/Statusbar/CG40_Statusbar_Map_Size.png) indicates the disk size used by the maps. The menu "Tools>Open 'Speed/slope' folder" will open the file manager and display the contents of the directory containing the curves, the logs and the directory containing the maps. The directory "OpenStreetMapTileCache" contains the map files. If needed, you can delete its contents to save space.

## Filter altitudes

If the altitude profile of your track is noisy (presence of peaks), **Course Generator** offers you the possibility to filter it. To perform this action, select "Tools>Smooth elevation data".

![](./images/CG40_Elev_Filter.png)

The original profile is displayed as a background image and the corrected profile appears in red.
The "filter" field allows you to adjust the intensity of the profile smoothing. The uncorrected and corrected positive and negative climbs are displayed.

Once you have obtained the desired profile you have 3 possibilities:

* Press "Select normal elevations" to select unfiltered elevations. This closes the dialog box and transfers the unfiltered elevations to the track elevations.
* Press "Select smoothed elevations" to select filtered elevations. This closes the dialog box and transfers the filtered elevations to the elevations of the track.
* Press "Cancel" to close the dialog box without changing the elevation of the track.

The CGX format saves, per point of the track, the 3 elevations (active, unfiltered, filtered). For the calculations only the "active" elevation is used. You can switch from filtered to unfiltered elevation via the dialog box.
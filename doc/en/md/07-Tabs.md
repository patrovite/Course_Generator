# Tabs

The top area contains a series of tabs that allow you to choose the type of data to display.

## "GPS data" tab

This tab displays all the data of the track.  
![GPS data tab](./images/Tabs/CG40_Tab_Data.png)

A double click on one of the lines opens the edit window.

The details of the columns are described below:

* **Num** : This is the row number of the table. It allows you to find quickly a line in the table.
* **Lat** : Contains the latitude of the point in degree.
* **Lon** : Contains the longitude of the point in degree.
* **Alt** : Contains the altitude of the point in meters/feet.
    * On the left of the altitude an arrow indicates the inclination of the field.
        * Upwards, this indicates that we have climbed since the last point.
        * Donwards, this indicates that we have descended from the last point.
        * To the right, this indicates that the field is flat.
    * The background color indicates the degree of slope of the field.
        * Brown if climbing (positive slope). The higher the slope, the darker the color.
        * White if the field is flat.
        * Green if going down (negative slope). The higher the slope, the darker the color.
* **Tag** : Displays the marks associated with the point.
    * ![](./images/Tags/high_point.png) : Indicates a high point.
    * ![](./images/Tags/low_point.png) : Indicates a low point.
    * ![](./images/Tags/eat.png) : Indicates a refueling point.
    * ![](./images/Tags/drink.png) : Indicates a water point.
    * ![](./images/Tags/flag.png) : Indicates that the point has been marked.
    * ![](./images/Tags/photo.png) : Indicates a view place.
    * ![](./images/Tags/note.png) : Indicates a note.
    * ![](./images/Tags/info.png) : Indicates information.
    * ![](./images/Tags/roadbook.png) : Indicates the beginning or the end of a roadbook section.
    * ![](./images/Tags/dropbag.png) : Indicates a drop bag
    * ![](./images/Tags/crew.png) : Indicates the presence of crew
    * ![](./images/Tags/first_aid.png) : Indicates a first aid
* **Dist** : Contains the distance, in meters/miles, since the previous point.
* **Total** : Contains the distance, in kilometers/miles, that has been traveled to this point.
* **Diff** : Contains the field difficulty between the previous point and this point. The initial value is 100, this corresponds to a flat road. The lower the value, the more rugged it is. If you enter 80, this indicates that compared to a flat road you will spend 20% more time to cover the distance. The button ![](./images/Toolbar/fill_diff.png) allows you to quickly fill a set of position. You will find below the values ​​used by Softrun (www.softrun.fr). Thanks to Rémi Poisvert for these informations.
    * "Easy field" = 98
    * "Average field" (normal mountain trail) = 95
    * "Hard field" = 88
    * "Very hard field" = 80
    * "Extremely hard field" = < 80
* **Coeff** : Contains the fatigue coefficient to be applied between the previous point and this point. The value is between 1 and 200. The initial value is 100, this corresponds to your initial state (fresh state). If you enter 80, it indicates that you will spend 20% more time to travel the distance.
The button ![](./images/Toolbar/fill_coeff.png) allows to define a rule of global evolution of the fatigue coefficient.
* **Recovery** : Contains the recovery coefficient that will be added to the fatigue coefficient. It is a relative value that is between 0 and 100. Initial value is 0 (not displayed). The sum "Coeff" + "Recovery" is limited to 100%.
This parameter is used to indicate recovery after a break (refueling, nap at a aid station...). Once the value entered, it's necessary to restart an overall calculation of the coefficient of fatigue with the button ![](./images/Toolbar/fill_coeff.png).
* **Time** : Contains the total time needed to reach this point from the start.
* **Cut-off** : Contains the cut-off time at this point of the track. This is the time since the start and not the hour (this avoids the problems related to the departure times shift). If at a point in the track the time is greater than the cut-off time then an indicator appears in the status bar. A click on the indicator will select the first line where the cut-off time has been exceeded.
* **Hours** : Contains the day and time of passage at this point. Start date and time are adjustable in the track parameters.
If the background is green it indicates that the travel is made during day time. If the background is blue then the trip is done during night time.
* **Aid station** : Contains the total refueling time you expect to spend at this point.
If no refueling is planned for this point (time equal to 00:00.00) then the cell is empty.
* **Name** : Contains the name of the point. It is used in profile view, reports and mini roadbook.
* **Comment** : Contains a comment on the point. It is used in reports and the mini roadbook.

## "Profile" tab

This tab displays the track profile.  
![Profile tab](./images/Tabs/CG40_Tab_Profil.png)

If a mark has been positioned in the table then a point appears on the curve as well as its serial number. This number corresponds to the table row of the Summary tab.

A left click on the curve makes it possible to position a cursor and to obtain information on the point (information coming from the data table).

A right click on the curve displays a menu to adjust the display of the curve and save the display as an image (PNG format).

The button ![](./images/Tabs/profil_marker.png) displays the row position of the "Summary" table.


## "Statistics" tab

This tab provides statistics on the track.  
![Statistics tab](./images/Tabs/CG40_Tab_Stat.png)

Statistics contain among other things:

* Average speed, distance and time for various slope areas
* Average speed, distance and time for various altitude ranges
* Average speed, distance and time for the daylight period
* Average speed, distance and time for the night period
* The average percentage of slope of the ascent and descent
* The distance traveled uphill, on the flat and downhill
* The temperature difference between the low point of the course and the high point. This value is purely indicative and theoretical. It does not take into account the wind chill effect and local phenomena. The basis of calculation is 0.6°C per 100m of elevation gain.

The button ![](./images/Tabs/save_html.png) saves this data in HTML format.  
The button ![](./images/Tabs/refresh.png) refreshes the data.

## "Weather" tab

This tab displays the historical weather data and the event summary information for a given track.  
![Statistics tab](./images/Tabs/CG40_Tab_Weather.png)

The historical weather data contains the following information:

* Maximum temperature, minimum temperature and precipitation for the last 3 years (if an appropriate weather station is found).
* The daily and monthly normals (maximum temperature, average temperature and minimum temperature) for the same date period (if an appropriate weather station is found).
* The weather station(s) name, URL and distance from start.

The event summary data contains:
* The start date.
* The sunrise and sunset times.
* The total daylight time.
* The moon phase description, illumination percentage and image.

The button ![](./images/Tabs/save_html.png) saves this data in HTML format.  
The button ![](./images/Tabs/retrieve_weather.png) retrieves the weather data from the NOAA servers.

## "Analysis" tab
This tab allows to obtain an analysis of your track. It contains 3 sub-tabs which are detailed below.

### "Analysis>Time/Distance" tab

This tab allows you to analyze your speed over time.

![Time/Distance tab](./images/Tabs/CG40_Tab_Analyze_Dist_Time.png)

Two curves are displayed:

* A curve of the altitude over the distance
* A curve of the time (in seconds) over the distance

The study of the curve of time/distance allows to see its evolution on the track (acceleration, slowdown, stopped).
The change of slope of the red curve indicates a variation of speed.

The following cases are possible:

* The slope of the curve becomes steeper. This indicates a slowdown (due to the terrain or fatigue).
* The slope of the curve becomes less steep. This indicates an acceleration.
* The slope changes abruptly. This indicates a stop.

### "Analysis>Speed" tab

This tab allows you to get an analysis of the speed on your track.  
![Speed tab](./images/Tabs/CG40_Tab_Analyze_Speed.png)

Two curves are present:

* A curve of the speed over the distance
* A regression curve of speed

A right click on the curve display a menu allowing to save the curve as an image.

### "Analysis>Speed​​/Slope" tab

This tab is used to extrapolate a speed curve over the slope.  
![Speed/slope tab](./images/Tabs/CG40_Tab_Analyze_Speed_Slope.png)

This curve is useful for creating its own speed/slope curves according to a previous track.

Two curves are present:

* A scatter plot that includes all the points acquired during your race
* A speed/slope curve (extrapolated)


The button ![](./images/Tabs/save_curve.png) saves the result curve (red curve) in the speed/slope curve library. It will be useable in the curves dialog.  

The following dialog appears:  
![](./images/Tabs/CG40_Tab_Analyze_Speed_Slope_Save.png)  
The "Name" input field is used to enter the name of the curve.  
The "Comment" input field is used to enter a comment.

The button ![](./images/Tabs/correction.png) corrects the speed/slope curve with the track parameters (terrain difficulty and fatigue).  
The button ![](./images/Tabs/speed.png) allows to filter the speed of the speed/slope curve.


### "Summary" tab

This tab displays a table containing all the lines of the track containing a mark.  
![Summary tab](./images/Tabs/CG40_Tab_Resume.png)

For each line you have:

* A number.
* The name of the point.
* The line of the data table where is the point.
* The altitude of the point.
* The accumulated elevation gain up to the point.
* The accumulated elevation loss up to the point.
* The distance traveled up to the point.
* The time at the point.
* The hour at the point.
* The travel time since the last point.
* The cut-off expressed in time since the start.
* The refueling time.
* The distance from the last point.
* The elevation gain since the last point.
* The elevation loss since the last point.
* The climb speed since the last point.
* The descent speed since the last point.
* The average slope of the climbs since the last point.
* The average slope of the descents since the last point.
* The average speed since last point.
* The comment on this point.

The button ![](./images/Tabs/save_csv.png) saves the table data in CSV format in order to be used in a spreadsheet (Excel, OpenOffice Calc ...).  
The button ![](./images/Tabs/refresh.png) allows you to refresh the data.  

A double clic on a line open's the line editor for the corresponding point.

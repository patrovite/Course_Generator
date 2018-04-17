# Using Course Generator

The normal procedure for using **Course Generator** is:

* Upload a GPS file
* Set the global track parameters (Name, date and start time)
* Choose the "Speed/Slope"curve
* Set the track parameters (field difficulty, fatigue coefficient, aid stations...)
* Start the calculation
* Save the track in CGX or GPX format

The subchapters will describe all these manipulations (And much more).

## Load a track

Two types of files can be opened by **Course Generator**.

* The GPX format contains a GPS track from a recording made with a GPS, a mapping software or a website. This format does not contain specific **Course Generator** data. This format is to use if you don't have a CGX file of your track.
* The CGX format, which is the format of **Course Generator**, stores all the specific data of the software. It is preferred if you want to keep your work.

The loading of a track is done by the menu "File> Open GPX" or "File> Open CGX" according to the chosen format.

When opening the file you may get the following message:
((image message))

This message appears because some files do not contain time data, which prevents **Course Generator** from displaying the time and time of passage for each point. These columns are then set to 0. Setting of parameters of the track then the execution of the calculation of the track are then necessary.

## Set up a track

The setting of the track is done with "Settings>Track settings..." or [F9].

The configuration window below is then displayed.

![Track parameters dialog](./images/CG40_Track_Param.png)

You can then:

* Enter the name of the track (maximum 15 characters).
* Add a description of the track. This description will appear in the roadbook.
* Set the date and time of start.
* Select if you want to use the altitude effect feature.
* Select if you want to use the night effect feature. You must then enter the times for the beginning and ending of the night and the correction factor to be applied to each position. The "Auto-adjustment" button opens the dialog box to automatically calculate the sunset and sunrise time. The calculation is made from the information of the first GPS point of the table(GPS coordinates, date and hours).

The following window appears:

![Sun dialog](./images/CG40_Track_Param_Sun_Dlg.png)

Enter the time zone (1 for France).

Once the setting is completed, press "Ok" to validate.
If you have chosen to take into account the night effect, you will see that the "Time" column shows a blue background during the night and green periods during the daytime periods.

## Set the speed/slope curve

In order to have a track time consistent with your level, you have to choose or create a speed curve according to the slope. This curve goes from -50% slope (downhill) to + 50% slope (climb). The choice of the curve is made by the choice of the speed that one wants to "hold" when the slope is null (0%) on a road. A set of curves have been created to cover most running uses.

The menu "Settings>Speed/Slope curves..." or the button ![curve button](./images/Toolbar/curve.png) to display the dialog box for selecting and managing curves.

![Curves dialog](./images/Curve/CG40_Dlg_Curves.png)

On the left appears the list of curves already created. During the creation, an explicit name was given to them to find them quickly. Try to preserve this principle if you create new curves.

The button ![open curve button](./images/Curve/chart_curve_open.png) load the data from the selected curve file into the list.
The button ![edit curve button](./images/Curve/chart_curve_edit.png) allows you to modify the data of the selected curve.
The button ![add curve button](./images/Curve/chart_curve_add.png) allows you to create a new curve.
The button ![duplicate curve button](./images/Curve/chart_curve_duplicate.png) duplicate the selected curve.
The button ![delete curve button](./images/Curve/chart_curve_delete.png) deletes the selected curve.

> **How to choose a speed curve?**
> This will depend on you, your goals... You can base yourself on a percentage of your vVO2max.
> For example for a long trail you can take 60% of your vVO2max. About 10km/h in my case, I then select the curve of 10km/h.
> There is also the pratice. At the beginning you will surely underestimate or over-evaluate your speed. But over time your choice will become more and more precise.

**Notes:**  
* If you create new curves that seem interesting to you, do not hesitate to send them to me so that I can add them on the website as well as in the next versions.
* Each curve is a file whose extension is '.par'. These files are accessible through the menu "Tools>Open "Speed/Slope" folder". This will open the file manager and display the contents of the directory.

## Enter the field difficulty

The "Diff" column is used to 'quantify' the difficulty of the field.

You can quickly enter terrain difficulty for a set of points. Simply select the first line and then while holding down the SHIFT key you select the following lines (with mouse or keyboard). The button ![diff button](./images/Toolbar/fill_diff.png) to display the auto-fill dialog.

![Difficulty dialog](./images/CG40_Dlg_Fill_Diff.png)

The "Start" area is used to define the start line (from the beginning or from a specific line number).
The "End" area is used to define the end line (to the end or to a specific line number).
The zone "Difficulty" makes it possible to choose the difficulty of the field. You can either use the pre-determined values ​​or enter your own difficulty value.

This action can also be done on the tracke map (see below).

**Note:**  
It is sometimes impossible to determine the quality of the field because of the ignorance of it. It may be wise to set an average field quality for the entire track. For example, the "Montagn'hard 100" has been qualified in middle filed on the whole track. Even if some passages were very difficult (scree, slippery areas ...) and other very easy (roads or tracks).
Don't try to take intop account the slope of the field because this is taken into account via the "Speed​​/Slope" curve and the calculation of the slope is automatically done by **Course Generator**.

## Enter fatigue coefficient

The "Coeff" column is used to 'quantify' fatigue over time.

You can quickly enter the fatigue coefficient for a set of points (more generally for the entire track). Simply select the first line and then while holding down the SHIFT key you select the following lines (with mouse or keyboard). The button ![coeff button](./images/Toolbar/fill_coeff.png) allows to display the auto-fill dialog.

![Health coefficient dialog](./images/CG40_Dlg_Fill_Coeff.png)

The "Start" area is used to define the start line (from the beginning or from a specific line number). The "Initial value" field is used to enter the corresponding value.

The "End" area is used to define the end line (to the end or to a specific line number). The "Ending value" field is used to enter the corresponding value.

If the ending value is not equal to the initial value then the intermediate lines will have a gradual and linear variation of the values. Manual edits made through the line editor will be overwritten.

The settings made in this window will be global and stored in the CGX file.

The area "Help" allows according to your estimated to give you an approximate value of the coefficient of fatigue. This value can be copied to in the "Initial value" and "Ending value" fields with the buttons "> Start" and "> End".

## Enter refueling times

In order to stick to reality, you can enter for a given point the time you plan for your refueling (or your rest).

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The "Aid station time" fields are used to enter the time at this location (hours, minutes and seconds). The "0" button allows you to reset the time to 00h00mm00s.

> **FAQ!**
> The time or hour displayed on the line containing a refueling time is the time or hour you plan to leave this position.
> It's the design of **Course Generator** which oblige this method of calculation.
>
> To summarize:
> [Hour] = [Hour of the previous position] + [Travel time between 2 positions] + [Refueling time]
>
> [Time] = [Time of the previous position] + [Travel time between 2 positions] + [Refueling time]

## Enter recovery times

You can enter for a given point the recovery coefficient after refueling or rest. To do this, go to the corresponding cell and open the line editor by double-clicking.

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The "Recovery" field is used to enter the recovery coefficient (between 0 and 100). This value is relative. If you think that you will recover 5% of fatigue coefficient, you must enter 5 and not the value you think you will have (eg from 85% to 90%).

**Note :**  
After modifying the "Recovery" column, it is necessary to restart a global calculation with the button ![fill coeff](./images/Toolbar/fill_coeff.png) in order to have your input taken into account.

## Enter cut-off times

You can enter for a given point the scheduled time barrier. This time barrier is expressed in time since the start and not the time at the point. This makes it possible to take into account the departure delays (for example the UTMB 2011 with 5 hours delay). To do this, go to the corresponding cell and open the line editor by double-clicking.

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The "Cut-off time" fields are used to enter the cut-off time (hours, minutes and seconds). The "0" button allows you to reset the time to 00h00mm00s.

In order to take into account the input it is necessary to run a calculation with the button  ![refresh button](./images/Toolbar/refresh_data.png). After the calculation if one of the position time exceeds a cut-off time then a red indicator "Cut-off time" will appear in the lower bar. A click on the indicator will select the first line of the track having a cut-off time.

## Indicators or tags

For each point you can have indicators or tags that indicates a particularity of the point.

The different indicators are as follows:

* ![high point indicator](./images/Tags/high_point.png) : Indicates a high point. This indicator selected manually or automatically by the function "Find Min/Max".
* ![low point indicator](./images/Tags/low_point.png) : Indicates a low point. This indicator selected manually or automatically by the function "Find Min/Max".
* ![eat indicator](./images/Tags/eat.png) : Indicates a refueling point (eat or drink)
* ![drink indicator](./images/Tags/drink.png) : Indicates a water point
* ![photo indicator](./images/Tags/photo.png) : Indicates a view point
* ![mark indicator](./images/Tags/flag.png) : Indicates a special point. This indicator is called "Mark" and allows you to split the track into steps. Each mark adds a line in the summary table.
* ![note indicator](./images/Tags/note.png) : Indicates a note.
* ![info indicator](./images/Tags/info.png) : Indicates information.
* ![roadbook indicator](./images/Tags/roadbook.png) : Indicates the beginning of a new part of the roadbook

To set the indicators for a position, select the corresponding cell concerned and open the line editor by double-clicking.

The following dialog box is displayed:

![Line editor](./images/CG40_Line_Editor.png)

The indicators appear in front of "Tags".

In order to save time, keyboard shortcuts are available:

* [F6] allows to put or remove a "Mark" on the selected line.
* [F7] allows you to move quickly to the next line containing an indicator.
* [Ctrl+F7] allows you to move quickly to the previous line containing an indicator.

## Calculate the track time

Once the track parameters have been entered, it is necessary to press the button ![refresh button](./images/Toolbar/refresh_data.png) to start the calculation of the time for each point.
The columns 'Time' and 'Hours' are then updated according to the settings you have made previously.In the status bar at the bottom of the window, the total time is updated.

## Save the track

**Course Generator**offers the possibility to save your track in several formats.

* "File>Save as GPX" save the track in GPX format which is the standard track exchange format. The problem with this format is that it does not store specific data of **Course Generator**.
* "File>Save as CGX" saves the track in CGX format which is the standard file format of **Course Generator**. This format should be used as soon as you want to keep the settings made on a track.
* "File>Save as CSV" saves the track in CSV format which is a standard format for saving data as semicolon-separated text. These files can be opened by a spreadsheet program such as EXCEL, OpenOffice Calc or Libre Office Calc.

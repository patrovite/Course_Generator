# Course Generator

Course Generator is a software which can estimate your time in trail races. 

STATUS : Version 4.0.0 alpha 1 - Usable but not all the features has been rewritten from the C# version.

I created Course Generator in 2008 for my first 100 km ultra trail. Since then I added a lot of features and it has been used by a lot of french ultra-runner. Up to the version 3.88 it has been written in C# and now java is used in order to be cross platform (Windows, Mac OS and Linux) and multilingual. 

Course Generator is free (donation are welcome ;) ) and open source under GPL V3.

In order to works Course Generator (also called CG) need a GPS file and some extra information.

With Course Generator you can :

* Estimate your time on each point of the track. 
* Set field type (the difficulty to the track: road, track...) of each point of the track
* Set your health status and how it will evolve during the race
* Set the station time (to eat, to drink, to sleep...)
* Set the time limits on points of the track. CG will give you an alert if you are late
* Set the recovery value after a rest
* Set a name and a comment for each position of the track
* Take into account the effect of the night during
* Take into account the effect of the elevation (over 1500m)
* See your track on an Openstreet map
* Generate a mini road book where you will see the track profile and all the most important information of your race (position name, time, hour, elevation, ascend...)
* Get statistics of your race
* Analyze a race
* Invert the track direction
* Define a new start point for "loop track"
* Insert an external track at the beginning or the end of a track
* Extract and save a part of the track
* Save all your specific settings in a CGX file (a Course Generator specific format)

  
How to run Course Generator : 

* Currently (alpha version) there no installer.
* You need a version 7 (minimum) of the java runtime environment (JRE) 
* Copy the "course_generator.jar" in a folder (where you want).
* Execute "course_generator.jar"
* Once the main window is displayed exit from CG
* A "Course Generator" folder has been created in your home/user directory. Inside a "config.xml" file is present
* Download the ".par" file from the github "default_curves" directory and put them next to the "config.xml" file.
* Restart CG
* That's all. Enjoy ;)

More information at [www.techandrun.com](http://www.techandrun.com)


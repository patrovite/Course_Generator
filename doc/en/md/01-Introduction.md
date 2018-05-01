# Introduction

**Course Generator** allows you to process your GPS files in order to :

* Calculate your travel time by having previously adjusted the parameters corresponding to the nature of the terrain and your abilities,
* Calculate your times at each point of the track,
* To add on the course the nature of the terrain, your fatigue coefficient over time, refueling or rest periods and comments,
* Define cut-off time (an indicator will show you an overrun),
* To define recovery coefficients,
* To define the night periods and the associated efficiency decline,
* To select the decline of performance according to the altitude,
* View your track on an map (OpenStreetMap, OpenTopoMap, etc...),
* To generate a mini-roadbook with the profile of your track and annotations on the waypoints (name, time, altitude, Elevation gain ...),
* To generate a report in text format (CSV),
* Get statistics on the track (HTML format),
* Reverse the direction of the track,
* Set a new starting point for a loop track,
* Insert a track at the beginning or at the end of another track,
* To extract part of the track,
* To save the modified track, containing calculated track times, in GPX format. This will allow you, for example, to use the "Virtual Partner" function of GARMIN GPS,
* Save the track in CGX format which is the backup format of **Course Generator** in order to be able to exchange tracks while keeping the ground data, markers, the comments ...

And many other things...

Writing conventions have been adopted in this manual. They are detailed below.

* *"File> Open GPX"* indicates that it is necessary to select the "File" menu then select, in this menu, "Open GPX". This allows to simply describe a sequence of manipulation to be done with the mouse.
* *"[CTRL + O]"* indicates one or a series of keys to activate in order to trigger an action. For example [CTRL + O] indicates that it is necessary to press the CTRL key while at the same time press the O key.

## Author's Note

I created **Course Generator** in 2008 to help me prepare for my first 100km Ultramarathon trail race. I improved the software based on my needs. A small article in Ultrafondu (French magazine) allowed me to start making it known and I then made it grow with the user feedbacks.
It has evolved enormously and many times, I have rewritten it to fit my needs. Each time it was a challenge and like the races that I ran, it was an adventure that made me grow (knowledge, questioning, open-mindedness ...).

**Course Generator** does not pretend to give you totally accurate results. Too much depends on you and external conditions. Consider this software as a help to prepare your futur adventures.

You notice that I didn't use the term "race" because for me the "adventure" approach of a race, as hard as it is, has always allowed me to go through with its positive approach (not to mention the chrono anyway:) ).

The development of **Course Generator** has been and continues to be an adventure.

If you like this software you can contribute in different ways:

* By donating, by going to **Course Generator** website. This allows me to pay for hosting the site, tools and books allowing me to continue the adventure. The development of the application is done on my free time.
* By advertising. By choice, I am not very active on forums and social networks to use my free time to improve the software. If you have the opportunity, do not hesitate to talk about **Course Generator**. Twitter, Facebook, forums and also Reddit which if it is little used in France is a tool widely used in English-speaking countries.
* By feedbakcs on the software. Bugs, documentation corrections and requests for improvements are welcome.
* By participating in the translation of the software into another language. It's simple, I send you a text file with English texts and you translate them into the target language by following a few simple rules.
* By participating in software development. Nothing very complicated, you have to know the Java language, Git and Github. Since version 4, **Course Generator** is Open Source and hosted on Github (github.com/patrovite/Course_Generator) so that other people can improve the software with me. The subject is vast, there is still plenty to do.

Go on an adventure with **Course Generator**.

Pierre DELORE

## Personal data protection

The software collects information about your hardware and software configuration in the logs. These data are in the 'logs' directory which can be accessed via the menu "Tools> Open the "Speed/Slope" folder". Nothing exits from your computer. It is only in case of problem that I will ask you to send me the 'logs' files.

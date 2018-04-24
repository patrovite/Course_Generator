# The principle

To work, **Course Generator** needs, as input, data containing a sequence of GPS points representing a track.

This data can be:

* A file in GPX format. This file contains the data of a track that has been created or downloaded on websites,
* A file in CGX format which is the recording format of **Course Generator**.

## What is a GPX file?

The GPX format is a standard cartographic data exchange format created by GARMIN.These data are either from a GPS or from a software or website.

It allows to exchange:

* Waypoints. These are GPS points, containing latitude, longitude and altitude, which is associated with information such as a name or symbol. The number of waypoints is usually limited on GPS (often 500 waypoints on GARMIN GPS).
* Routes. They consist of a set of waypoints. They are therefore limited by the number of waypoints that can contain a GPS.
* Tracks. A track consists of a set of GPS points (and not waypoints). Each GPS point contains at least the latitude and longitude of the point. Altitude and recording time are generally included in each point.

![Image Wikipedia](./images/CG40_GPX.png)  
Wikipedia source

When opening a GPX file, **Course Generator** only read tracks. Other type of data are ignored.

## The CGX format

The CGX format is the native format of **Course Generator**. It allows in addition to the latitude, longitude and altitude, to store all specific data of **Course Generator**. This includes for example: ground difficulty, aid station, comments, mini-roadbook data ... This format allows you to exchange a track with a complete set of information about it.

## The operating principle of Course Generator

The diagram below shows the operating principle of **Course Generator**.

![Principle of operation](./images/CG40_Principe.png)

## Use cases

The following use cases could be considered (non-exhaustive list):

* Prepare a race. After downloading the race GPS track you will adjust your parameters, adjust the terrain "quality", indicate the aid station and the planned downtime, add comments and many other things. Finally, **Course Generator** will calculate your time of passage for each point of the track. This will allow you to have your time at each point of the track, statistics (for example time spent at more than 2000m at night) and generate a mini-roadbook.
* For a race direction to share the track in which they would have indicated for the terrain "quality", the aid station, the cut-off times.
* Generation of a GPX file with pre-calculated time data to use the GARMIN GPS virtual partner. This allows to have a virtual partner running with you. If you have chosen the right parameters, you will be able to run at his side. This function also displays your position and the partner's position on the track and on the track profile. It gives you the remaining track time as well as the remaining distance. It's very convenient to manage your effort. The screenshot below shows you the profile view in virtual partner mode on a Forerunner 205/305. The dark point is you and the clear point is the virtual partner.

![](./images/CG40_Virtual_Partner.jpg)


> __Must I have Garmin GPS to use **Course Generator**?__
>
> No! But it's a plus if you want to use the virtual partner feature. That's what pushed me to create **Course Generator** (even though now I almost don't use this feature anymore).

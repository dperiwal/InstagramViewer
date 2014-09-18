InstagramViewer
===============
by Damodar Periwal

The following user stories have been completed:

- User can scroll through current popular photos from Instagram
- For each photo displayed, user can see the following details:
     Graphic, Caption, Username 
     (Optional) relative timestamp, like count, user profile image, location

The following advanced optional user stories have been completed:

- Advanced: Add pull-to-refresh for popular stream with SwipeRefreshLayout
- Show the profile picture in a circular area (works on a device but not on the emulator)
- Show comments count and maximum of two comments

  
<b>Issue:</b> 
pull-to-refresh works properly in an AVD with API level 16 but hasmemory problems in an AVD with API level 19.

<b>Third-party libraries:</b>
The following third party libraries are used in this project under their respective licenses:

     android-async-http-1.4.6.jar
     picasso-2.3.4.jar
     com.mikhaellopez.circularimageview.CircularImageView
     
Used Iconmonstr to create icons for watch and a heart sign.

Walkthrough of all user stories:

![Animated Walkthrough](DamodarInstagramViewer2.gif "Animation that shows the working of the app in an emulator")

The file DamodarInstagramViewer1.gif shows the working of an earlier version of the app in an emulator.

GIF created with [LiceCap](http://www.cockos.com/licecap/).





# Landmark-Notes

## Background
A small app that allows users to save location based notes on a map. These notes can be displayed on the map where they were saved and viewed by the user that created the note as well as other users of the application. 
The application must demonstrate the functionality captured in the following user stories:
1. As a user (of the application) I can see my current location on a map
2. As a user I can save a short note at my current location
3. As a user I can see notes that I have saved at the location they were saved on the map
4. As a user I can see the location, text, and user-name of notes other users have saved
5. As a user I have the ability to search for a note based on contained text or user-name

## Functionalities
Based on the requirements mentioned above, I tried to implement below functionalities and these things can be updated/modified in future releases.
I made sure that these functionalities are implemented first (considering all the things are perfectly working), leaving the small things like validations behind.(Added in Future Updates column in this ReadMe.)
1. Google Sign-In Feature
2. Display of Notes on home page (based on the logged-in user)
3. Adding a new note functionality with default(In case of no network/GPS) and location based location displayed automatically
4. Delete functionality
5. Viewing all notes on the map with Info window click functionality to see more about the note (For both all users and logged in User)
6. Search Functionality (Filter criteria: Title and description)

## Technology Stack Requirements
The Android native application should target minimum API level 21 using no third-party (non-Google) libraries except those explicitly called out in these specifications. 
You may not use WebView to implement any aspect of the application. 
Your application should perform well on a Google Nexus 5 or 6 device (or equivalent). 
Source must be developed and runnable in  Android Studio .

## Technologies Used
1. Android (Java)
2. Firebase (For Google login)

## Features implemented (New to me)
1. Recycler View
2. Google Cluster API (to display all pointers on the map)
3. Firebase Database

Please check this [Images Folder](https://github.com/nasreekar/landmark-notes/tree/master/Screenshots) for screenshots of the app.

## Future Updates/Modifications
Many functionalities can either be added or modified to the current application. 
Few of the things that can be either modified or added are:
1. Edit Note functionality
2. Better Google signout functionality to show list of all google accounts added in the phone to signIn everytime
3. Simple validations like checking for Internet availability and displaying an error message.
4. Color coding of notes
5. Color coding for pointers (different color for different users)

## Challenges
Other things which are new to me but can be implemented are:
1. Better Architecture Pattern usage
2. SOLID principles

## Contribution

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -am 'Added some feature')
4. Push to the branch (git push origin my-new-feature)
5. Create new Pull Request

## License
[MIT](https://github.com/nasreekar/license/blob/master/LICENSE)

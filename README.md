# Gaming-Social-Media-App
Description
An app for gamers to post, explore different games or trending games to play. The users can also meet other users with similar interest and connect to game with each other.

App Evaluation

[Evaluation of your app across the following attributes]

- Category:photo & video/ Social/ gaming
- Mobile: uses camera, mobile first experience
- Story: Allow users to share post about a game they are currently playing and what makes it interesting 
- Market: Anyone that is looking for a someone to play a particular game with or looking to discover new games to try could enjoy this app.
- Habit: Users can post clips or pictures about a level they just completed, and other users finding it hard to pass that level can ask how to pass that           level. Users can also explore grid of games that are arranged according to the most trending games.
- Scope: posting pictures and videos. Also, exploring different games and seeing post about that game.
Product Spec

1. User Stories (Required and Optional)

Required Must-have Stories

- User can login
- User can create a new account
- User can view a feed of posts
- User can explore trending games and see posts in the category of the game
- User can post a new photo or video to their feed
- User can view their profile information
- User can like,and comment on a post
- User can search for other users

…
Optional Nice-to-have Stories

- User can see notification when someone likes or comment on their post
- User can see their profile page with their profile picture
- User can customize the app to their liking in settings
- User can share posts to friends
- User can tag others in a post or comment section of a post
- User can give feedback about the app
…
2. Screen Archetypes

[list first screen here]
- Login Screen
  * User can login
- Registration Screen
  * User can create a new account
- Home page / Timeline
  * User can view a feed of posts
  * Like, share, and comment on posts
  * View profile info about the owner of the posts
- Explore screen
 * User can explore trending games (grid or card view)
 * User see posts in the category of a game
 * Games are ranked based on the number of posts about the game
- Creation screen
 * User can post a new photo or video to their feed
- Search screen
 * User can search for other users
- Profile screen
 * User can view their profile information
 * See their posts
 * delete their posts
 * Edit their profile information
- Notification screen
 * User can see notification when someone likes or comment on their post
 * User can see when they are tagged in posts
- Feedback Screen
 * User can give feedback about the app and problems they encountered while using the app
…
3. Navigation

Tab Navigation (Tab to Screen)
- Home Tab
- Explore
- Creation
- Menu Item nav (top right corner)
Flow Navigation (Screen to Screen)
- Login Screen
 => Home
- Registration Screen
 => Home
- Home Screen 
 => Profile Screen from a user's post 
- Explore Screen
 => Another screen to show posts related to the game selected
- Creation Screen
 => Home (after you finish posting the photo)
- Search Screen
 => Profile Screen of the user searched
- Menu Item nav
 => user's profile
 => Notification screen
 => settings screen
 => Feedback Screen
 
<img src="https://github.com/ayo0620/Gaming-Social-Media-App/commit/0830a91d117257fa42c08a11f50e2b8404ed24c5" width=250><br>
<img src="https://github.com/ayo0620/Gaming-Social-Media-App/commit/b7f8b0651f43556d5bbac540f187d9a56a59982c" width=250><br>

## Schema 
### Models
#### user

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | username        | String | User's username |
   | profileImage         | File     | image for user's profile picture |
   | userDescription       | String   | Biolgraphy of the user |
   | userFriendList | Array   | The user's friend list|
   | createdAt     | DateTime | date when the user created account |
   | updatedAt     | DateTime | date when the user last updated the account |
   | status    | Boolean   | if the user is online or not |
   | password     | String | User's Password |
   | email     | String | User's email |
   | FirstName    | String   | User's first name |
   | LastName    | String | User's last name |
   | userGames    | Array | A string array of the games the user plays |
   
#### Notifcation

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the notification(default field) |
   | notifiedFrom         | Pointer to user     | refers to the user for which a notification comes from |
   | createdAt     | DateTime | date when notification was sent |
   
#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User| image author |
   | image         | File     | image that user posts |
   | Description       | String   | image caption by user |
   | comments | Array   | Comments posted under a post|
   | likesCount    | Number   | number of likes for the post |
   | Favorited    | Boolean  | if a user favorited a post or not |
   | createdAt     | DateTime | date when post is created (default field) |
   | LikedBy   | Array   | An array of all the users that liked a post |
   | user     | pointer to User class | Refers to the user that owns a post |
   | updatedAt     | DateTime | date when post is last updated (default field) |
   
#### UserFeedBack

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the feedback (default field)|
   | Description         | String   | feedback from user about their experience with the app |
   | createdAt     | DateTime | date when feedback was created (default field) |
   | UserID    | pointer to user | Id of the user that sent the feedback |
   
   #### cardView (in explore page)

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | GameDescription | String   | A desription about the game|
   | createdAt     | DateTime | date when post was created (default field) |
   | UserID    | pointer to user | Id of the user that sent the feedback |
   | cardImage | file   | A picture related to the game|
   | GameName | String   | Name of the game|
   | Category| String   | Category of the game|
   | posts | Pointer to Post class   | gets post related to that game|
   | updatedAt     | DateTime | date when post was last updated (default field) |
 

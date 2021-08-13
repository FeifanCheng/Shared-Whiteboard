# Shared-Whiteboard
Shared white boards allowmultipleuserstodrawsimultaneouslyona canvas. There are multiple examples found on the Internet that support a range of features such as freehand drawing with the mouse, drawing lines and shapes such as circles and squares that can be moved and resized, and inserting text.
## Whiteboard – Multiuser system
 Multiple users can draw on a shared interactive canvas.

 Your system will support a single whiteboard that is shared between all of the clients.

 KeyElementswithGUI

 Shapes: at least your white board should support for line, circle, oval and rectangle.

 Text inputting– allow user to type text anywhere inside the white board.

 User should be able choose their favourite colour to draw the above features. At least 16 colors should be available.

 Of course, you are most welcome to be creative/innovative.

## Advanced feature
1. Chat Window (text based): To allow users to communicate with each other by typing a text.
2. A “File” menu with new, open, save, saveAs and close should be provided (only the manager can control this)
3. Allow the manager to kick out a certain peer/user

Use Server.jar to launch the Server

Use CreateWhiteBoard.jar to launch the manager client

Use JoinWhiteBoard.jar to launch the other clients

## Guidelines on Usage/Operation
Users must provide a username when joining the whiteboard. There should be a way of uniquely identifying users, either by enforcing unique usernames or automatically generating a unique identifier and associating it with each username.

 All the users should see the same image of the whiteboard and should have the privilege of doing all the drawing operations.

 When displaying a whiteboard, the client user interface should show the usernames of other users who are currently editing the same whiteboard.

 Clients may connect and disconnect at any time. When a new client joins the system the client should obtain the current state of the whiteboard so that the same objects are always displayed to every active client.

 Only the manager of the whiteboard should be allowed to create a new whiteboard, open a previously saved one, save the current one, and close the application.

 Users should be able to work on a drawing together in real time, without appreciable delays between making and observing edits.

## Proposed Startup/Operational Model

 Thefirstusercreatesawhiteboardandbecomesthewhiteboard’s manager
 ```
javaCreateWhiteBoard<serverIPAddress><serverPort>username
```
 Other users can ask to join the whiteboard application any time by
inputting server’s IP address and port number
```
java JoinWhiteBoard <serverIPAddress> <serverPort> username
```
 A notification will be delivered to the manager if any peer wants to join. The peer can join in only after the manager approves

 A dialog showing “someone wants to share your whiteboard”.

 An online peer list should be maintained and displayed

 Allthepeerswillseetheidenticalimageofthewhiteboard,aswellas have the privilege of doing all the operations.

 Onlinepeerscanchoosetoleavewhenevertheywant.Themanager can kick someone out at any time.

 When the manager quits, the application will be terminated. All the peers will get a message notifying them.

# cite from "Unimelb distributed system assignment 2"

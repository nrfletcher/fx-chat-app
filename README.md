# JavaFX chat app using socket programming
---
## Program layout
* ### Applications
* ### Controllers
* ### Handlers
---
## Applications
### We have two applications which are executable and run the GUI
* The client application which creates our main window for users
* A GUI based server creator that lets us choose a port dynamically
### And two controllers for each one
* Client controller creates our client and deals with the client logic
* Server controller chooses a port and creates ServerSocket
### Additionally, a client handler
* Since our server will be many-to-one in terms of server
* We use a client handler to collect all our users and execute operations on all at once

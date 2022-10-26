# Overview
* This is an application based program using the JavaFX GUI library which creates two programs:
*1. A server application for hosting the chat application using a ServerSocket implementation
*2. A client application for a user to interact with the server via a GUI-based client Socket implementation

## Program layout
* ### Applications
* ### Controllers
* ### Handlers
---
## Applications

### We have two applications which are executable and run the GUI
* The client application which creates our main window for users
* A GUI based server creator that lets us choose a port dynamically

## Controllers
### And two controllers for each one
* Client controller creates our client and deals with the client logic
* Server controller chooses a port and creates ServerSocket

## Handlers
### Additionally, a client handler
* Since our server will be many-to-one in terms of server
* We use a client handler to collect all our users and execute operations on all at once

---
# Video demonstration of program
[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/GLn3UDUhidQ/0.jpg)](https://www.youtube.com/watch?v=GLn3UDUhidQ)

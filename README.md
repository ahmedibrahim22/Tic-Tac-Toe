# Tic Tac Toe Game
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/uses-css.svg)](https://forthebadge.com)

Network based TicTacToe game, using Java. This project implementation made by students from ITI-Intake 41-open source Cloud platform development track.

# Contents

- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Features](#features)
- [How To Play](#How-To-Play)
- [Authors](#Authors)

## Getting Started

There are two ways to run the project.

-First way by using the avaiable jar files as following
  open the terminal and cd to the Server jar file directory and run:
```
java -jar ./ServerGui.jar
```
then you will find the server scene and you will be asked to open the server. After opening the server it will 
listen all the requests on the localhost port 5005 

After that you need to run the jar file of the player like what you did in the server
open another terminal and cd to the player jar file directory and run:
```
java -jar ./TicTacToe_Player.jar
```
then you will find the first scene in the player package and you will be asked to login with your credentials if you have an account or sign up if you don't. You must take in your consideration these jar files made to handle play game in the same machine.
If you want to run the game in different machines change should be made and it's to change the connection ip in the Client project in a class called TicTacToe_Player.java from 127.0.0.1 to the ip of the machine which the server is runing on.

-Second through the projects
   - open projects in netbeans or intellij idea
   - clean and build for both of them then run them


### Prerequisites

- java 8 or higher is recommended
- firewall configured for socket communications.


## Features

Client Side Features:</br>
- login</br>
- SignUp</br>
- selection mode to select mode of playing (Online or Offline)</br>
- play with pc with 3 difficulty levels</br>
- see who is online to play with</br>
- check if he already playing a game or not</br>
- play with online friends</br>
- chat while playing</br>
- see who has the highest score in the game</br>
- popUp to let the player know who has recently joined the game
</br>

Server side Features:</br>
- see a list of all users</br> 
- see players status and score</br>
- open & close the server by only button press</br>
</br>


## How to play
After you open the server and run the game you will be asked to login with your credentials if you have an account or sign up if you don't have an account then you will be directed to selection mode stage to either select play with computer or play online with another online player, if you selecte play with computer you will get three options easy, medium and hard. But in case you select to play online with another player you will be directed to select the player who you want to play with and send him an invitation if he accept the invitation the game will start. 

## Built With
* [fontawesomefx-8.9](https://jar-download.com/artifacts/de.jensd/fontawesomefx/8.9/source-code) -Icon packs for Java applications
* [gson-2.8.6](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/) -A simple Java toolkit for JSON
* [JFoenix](http://www.jfoenix.com/) -JavaFX Material Design Library
* [MySQL](https://dev.mysql.com/downloads/connector/j/) - JDBC Type 4 driver for MySQL



## Authors

- Ahmed Ibrahim
- Ahmed Maher
- Ibrahim Magdy
- Nagy Adel
<<<<<<< HEAD
- Noura Houssien
=======
- Noura Houssien
>>>>>>> c5379c09c26d6fb2594f486fb08a98c11473b6f6

# ESP32-Cam-Live-Feed
A project that uses an ESP32 camera to send image data to a web server to be viewed by a client application.

This is my final project from CS04515: Embedded Systems Programming at Rowan University. I completed this course in Spring 2025 towards an MS degree in Computer Science. There was quite a bit more that I wanted to accomplish with this project, so it has now become a hobby project. 

When I completed this project, it consisted of 3 primary components:
1. C++ code programmed in Arduino IDE which was embedded in the ESP32 microcontroller.
2. A Java HttpServer for handling HTTP requests between the clients (ESP32 camera and desktop application).
3. A Java application using Swing and HttpClient to get the images from the HttpServer.

This implementation was largely a proof of concept for a final project in a course. There are several directions where I want to build this out. The main areas are:
1. Building a new front end using HTML, CSS, and JavaScript to view the images and give the user control.
2. Utilizing a full-featured web server to handle requests.
3. Improving HTTP handling on the ESP32 camera.
4. Incorporate more ESP32 cameras into the project.

# ESP32-Cam-Live-Feed
A project that uses an ESP32 camera to send image data to a web server to be viewed by a client application.

This is my final project from CS04515: Embedded Systems Programming at Rowan University. I completed this course in Spring 2025 towards an MS degree in Computer Science. There was quite a bit more that I wanted to accomplish with this project, so it has now become a hobby project. 

If you would like to replicate this project, I want you to know that cybersecurity was NOT a factor, and to be very careful working with the electronic components. The components are inexpensive, but problems can occur if proper safety measures are not taken. I am not responsible for any issues that you may experience. Be careful, learn, and have fun! 

When I completed this project, it consisted of 3 primary components:
1. C++ code programmed in Arduino IDE which was embedded in the ESP32 microcontroller.
2. A Java HttpServer for handling HTTP requests between the clients (ESP32 camera and desktop application).
3. A Java application using Swing and HttpClient to get the images from the HttpServer.

This implementation was largely a proof of concept for a final project in a course. There are several directions where I want to build this out. The main areas are:
1. Building a new front end using HTML, CSS, and JavaScript to view the images and give the user control.
2. Utilizing a full-featured web server to handle requests.
3. Improving HTTP handling on the ESP32 camera.
4. Incorporate more ESP32 cameras into the project.
5. Add more sensors to the project.
6. Cybersecurity. This project does NOT currently include any cybersecurity safeguards or best practices, so be sure to disconnect the camera when not using! This project was a proof of concept.

The hardware peripherals used in this project are:
1. ESP32-CAM (using 5V power mode). 
   </br><img width="97" alt="image" src="https://github.com/user-attachments/assets/60aec82e-064d-4d1f-85a7-a0ff394865c5" />
2. 3.3V 5V FT232RL FTDI USB to TTL Serial Converter (using 5V setting).
   </br><img width="97" alt="image" src="https://github.com/user-attachments/assets/df644bf8-d357-4f26-a464-10ad8b7e7dae" />
3. DHT11 temperature and humidity sensor.
   </br><img width="88" alt="image" src="https://github.com/user-attachments/assets/dc4b34be-b2a2-4443-8ca3-a686089259a4" />
4. Breadboard.
5. Jumper cables of each variety.
6. USB B mini to USB A or C for connecting USB to TTL adapter to ESP32-CAM and personal computer.

The software dependencies are:
1. Java JDK (I used OpenJDK 22.0.2). Every Java import is included in the Java API.
2. Arduino IDE.
3. [Arduino-esp32 library by Espressif.](https://github.com/user-attachments/assets/2af4ab20-0a45-49c1-a341-a6d07f5920bd)
4. [esp32-camera by Espressif](https://github.com/user-attachments/assets/a7b57372-72a9-410f-9765-1d8641869893)
5. [esp32cam library by yoursunny.](https://github.com/user-attachments/assets/c38fdd05-79a5-48d4-8137-dbfa6c2cffd5)
6. [DHT_nonblocking by olewolf.](https://github.com/user-attachments/assets/497fb40b-8604-46de-968a-5c2ef3bcd736)

Here are some configuration notes.
1. Arduino IDE looks in ~/Documents/Arduino/libraries for library imports. It made configuration easier for me to move the libraries here when programming.
2. The correct board needs to be installed in Arduino IDE. [This tutorial](https://docs.espressif.com/projects/arduino-esp32/en/latest/installing.html) by Espressif (the board's manufacturer) describes how to do this.
3. Here is my wiring diagram (I made this using Excel).
   </br><img width="387" alt="image" src="https://github.com/user-attachments/assets/5ed5d4a5-8b8b-41db-b541-211d87f08565" />


Please use this project as a a fun project to learn and experiment with various libraries, as I did. I would like to state again that cybersecurity was NOT a factor in this implementation. Do NOT leave these cameras unattended or connected when not in use. I even end the web server process when I leave, and restart when I come back to work. Also, be VERY careful when working with the electronic components. There are many tutorials online for working with these components. A YouTube search for ESP32-CAM will return several results with tutorials. They were very helpful in my study.

Since this project covers so many bases (e.g., microcontrollers, embedded systems, web programming, application programming, etc.), it has become my hobby project to expand my knowledge in each area. It is quite open ended. I will share updates with you as I progress.



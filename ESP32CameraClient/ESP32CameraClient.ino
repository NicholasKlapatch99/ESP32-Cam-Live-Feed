/**
  * Nicholas Klapatch
  * May 4, 2025
  * This program implements an HTTP Client for use on an ESP32 Camera microcontroller that sends images
  * and DHT-11 sensor temperature readings to an HTTP Server. To create this, I followed Espressif's examples
  * from their arduino-esp32 and esp32-camera libraries. 
  * BasicHttpClient.ino: https://github.com/espressif/arduino-esp32/blob/master/libraries/HTTPClient/examples/BasicHttpClient/BasicHttpClient.ino
  * CameraWebServer.ino: https://github.com/espressif/arduino-esp32/tree/master/libraries/ESP32/examples/Camera/CameraWebServer
  * I also used the dht_nonblocking library that was used in the course's Assignment 8. DHT_nonblocking by olewolf.
  * https://github.com/olewolf/DHT_nonblocking
*/

#include "esp_camera.h"
#include <WiFi.h>
#include <HTTPClient.h>
#include <dht_nonblocking.h>
#include "camera_pins.h"

#define DHT_SENSOR_TYPE DHT_TYPE_11
#define CAMERA_MODEL_AI_THINKER

// WiFi connection information.
const char *ssid = ""; // Put your Wi-Fi ssid in the value in between the quotes.
const char *password = ""; // Put your Wi-Fi ssid's password in the value in between the quotes.

// Web server information.
const char *serverIP = "#.#.#.#"; // Replace with your web server's IP address.
const int serverPort = 8000; // Replace with your web server's port.

// Camera and HTTP client information.
WiFiClient client;
HTTPClient http;
camera_fb_t* fb;
camera_config_t config;
esp_err_t err;
sensor_t* s;
int cameraHttpReturnCode;

// Define DHT sensor using one of the ESP32's GPIO pins. Note: ESP32 camera GPIO pins are shared with other functionalities, such as WiFi.
// and this can cause issues if peripherals are competing for resources.
DHT_nonblocking dht_sensor(14, DHT_SENSOR_TYPE);
float temperature = 0.0;
float humidity = 0.0;
String temperatureString;
float wifiResetTime = millis() + 1000000;

void setup() {
  
  // Next step: Clear HTTP client to allow the ESP-32 camera to reset and reconnect to the web server without issue.
  // Note: I plan on testing this more and finding best practices.
  if(http.connected()){
    http.end();
  }

  // Set ESP32 configurations. These are from CameraWebServer.ino with modifications I made to work with my application.
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sccb_sda = SIOD_GPIO_NUM;
  config.pin_sccb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 20000000;
  config.frame_size = FRAMESIZE_VGA;
  config.pixel_format = PIXFORMAT_JPEG;
  config.grab_mode = CAMERA_GRAB_WHEN_EMPTY;
  config.fb_location = CAMERA_FB_IN_PSRAM;
  config.jpeg_quality = 10;
  config.fb_count = 1;

  Serial.begin(9600);

  // Initialize camera. Also from CameraWebServer.ino.
  err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    delay(1000);
    ESP.restart();
  }

  // Set sensor values (images have a yellow tint that I want to correct).
  s = esp_camera_sensor_get();
  s->set_whitebal(s, 1);
  s->set_awb_gain(s, 1);

  // Capture initial temperature reading before connecting to Wi-Fi (pins are multi purpose).
  // This is also something I want to work on so that the temperature can be read periodically instead of just once.
  for(int i = 0; i < 50; i++){
    dht_sensor.measure(&temperature, &humidity);
    delay(200);
  }
  temperatureString = String(temperature, 1);

  //Connect to Wi-Fi. Also from CameraWebServer.ino. 
  WiFi.begin(ssid, password);
  WiFi.setSleep(false);

  Serial.print("WiFi connecting");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  if(client.connect(serverIP, serverPort)) {
    Serial.println("Successful connection");
  }
  else{
    Serial.println("Connection unsuccessful");
  }

  // Begin connection to HTTP server.
  http.begin("http://#.#.#.#:8000/"); // Replace with your server's IP address and port.

  // Post initial camera and temperature data.
  fb = esp_camera_fb_get();  
  http.addHeader("Content-Type", "image/jpeg");
  cameraHttpReturnCode = http.POST(fb->buf, fb->len);
  http.end();

  http.begin("http://#.#.#.#:8000/"); // Replace with your server's IP address and port.
  http.addHeader("Content-Type", "text/plain; charset=UTF-8");
  http.POST(temperatureString);
  http.end();
}

void loop() {
  // Reset the frame buffer so it doesn't keep writing new images to it and overflow.
  esp_camera_fb_return(fb);

  // Check if connection is available, and send newly captured image.
  if(client.connect(serverIP, serverPort)) {      
    http.begin("http://#.#.#.#:8000/"); // Replace with your server's IP address and port.
    http.addHeader("Content-Type", "image/jpeg");
    fb = esp_camera_fb_get();
    cameraHttpReturnCode = http.POST(fb->buf, fb->len);
    http.end();
    Serial.println("New image posted.");
  }
  delay(1000); // Delay to prevent overloading the HTTP Server.

  // This section is for future work.
  // if(wifiResetTime < millis()){
  //   Serial.println("Reset time");
  //   WiFi.disconnect();
  //   // Capture temperature while disconnected from Wi-Fi (pins are multi purpose).
  //   dht_sensor.measure(&temperature, &humidity);
  //   temperatureString = String(temperature, 1);
  //     //Connect to Wi-Fi
  //   WiFi.begin(ssid, password);
  //   WiFi.setSleep(false);

  //   Serial.print("WiFi reconnecting");
  //   while (WiFi.status() != WL_CONNECTED) {
  //     delay(500);
  //     Serial.print(".");
  //   }
  //   Serial.println("");
  //   Serial.println("WiFi reconnected");
  //   Serial.print("IP address: ");
  //   Serial.println(WiFi.localIP());
  //   http.begin("http://#.#.#.#:8000/");
  //   http.addHeader("Content-Type", "text/plain; charset=UTF-8");
  //   http.POST(temperatureString);
  //   http.end();
  //   wifiResetTime += 5000000;
  // }
}
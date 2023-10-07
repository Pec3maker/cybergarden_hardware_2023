#include <PubSubClient.h>
#include <WiFi.h>
#include <LiquidCrystal.h>

#include "DHT.h"
#define DHT11PIN 22
#define KNOCK13PIN 13

//dht sensor
DHT dht(DHT11PIN, DHT11);

//lcd display
LiquidCrystal lcd(19, 23, 18, 17, 16, 15);

//wifi settings
const char* ssid = "nimodo";
const char* password = "nimodo228";
//end region

//mqtt settings
char *mqttServer = "192.168.100.199";
int mqttPort = 1883;

const char *mqtt_client_name = "WEATHER_STATION";
const char *mqtt_sub_topic = "/weather/subtopic";

const char *mqtt_base_topic = "/weather/";
const char *mqtt_pub_temperature = "/temperature";
const char *mqtt_pub_humidity = "/humidity";
const char *mqtt_pub_knock = "/quake";
// end region

WiFiClient client;
PubSubClient mqttClient(client);

void setup() {
  Serial.begin(115200);
  wifiConnect();
  delay(2000);
  initMqtt();
  initDHT();
  initKnock();
}

void loop() {
  connectMqttClient();

  String id = String(ESP.getEfuseMac());

  String idTopic = String(mqtt_base_topic) + "id";
  publishMsg(idTopic, id);

  String currentTemperature = getTemperature();
  String currentHumidity = getHumidity();
  String currentKnock = getKnockState();

  String temperatureTopic = createTopicWithId(String(mqtt_pub_temperature), id);
  publishMsg(temperatureTopic, currentTemperature);

  String humidityTopic = createTopicWithId(String(mqtt_pub_humidity), id);
  publishMsg(humidityTopic, currentHumidity);

  String knockTopic = createTopicWithId(String(mqtt_pub_knock), id);
  publishMsg(knockTopic, currentKnock);

  renderTelemetry(currentTemperature, currentHumidity, currentKnock);

  delay(3000);
}

// wifi connection
void wifiConnect() {
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");  Serial.print("WiFi connected to: "); Serial.println(ssid);  Serial.println("IP address: ");     Serial.println(WiFi.localIP());
}

// mqtt initialization
void initMqtt() {
  mqttClient.setServer(mqttServer, mqttPort);
  mqttClient.setCallback(recievedMessageCallback);
}

// dht initialization
void initDHT() {
  dht.begin();
}

void initKnock() {
  pinMode(KNOCK13PIN, INPUT);
}

// callback that runs when client resieve message
// print payload when recieve message
void recievedMessageCallback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message received from: "); Serial.println(topic);
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  Serial.println();
}

// mqtt connect
void connectMqttClient() {
  if (!mqttClient.connected()){
    while (!mqttClient.connected()){
      if(mqttClient.connect(mqtt_client_name)){
        Serial.println("MQTT Connected!");
        mqttClient.subscribe(mqtt_sub_topic);
      } else {
        Serial.print(".");
      }
    }
  }
}

String createTopicWithId(String topic, String id) {
  return String(mqtt_base_topic) + id + topic;
}

String getTemperature() {
  return String(dht.readTemperature());
}

String getHumidity() {
  return String(dht.readHumidity());
}

String getKnockState() {
	return String(digitalRead(KNOCK13PIN));
}

//publish msg
void publishMsg(String topic, String msg) {
  mqttClient.publish(topic.c_str(), msg.c_str());
  Serial.print("Message published ");
  Serial.println(msg);
  mqttClient.loop();
}

//render telemetry
void renderTelemetry(String temperature, String humidity, String knockState) {
  lcd.begin(16, 2);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print(temperature + " C " + "knock: " + knockState);
  lcd.setCursor(0, 1);
  lcd.print("humidity: " + humidity);
}

// WEBSITE: http://forum.arduino.cc/index.php?topic=285522.0

#include <Ethernet.h>           
#include <SPI.h>
#include <Client.h>             
#include<SoftwareSerial.h>

SoftwareSerial RFID(2,3);
int rfid[14],i;


byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };  
byte subnet[] = {255,255,255,0};

IPAddress gateway(192,168,1,1);
//IPAddress DBserver(192, 168, 1, 2);
IPAddress server(192,168,1,4);                  
//char server[] = "localhost/te"; 
IPAddress ip(192,168,1,5);

   EthernetClient client;


void setup() {
Serial.begin(9600);
  RFID.begin(9600);
  
  delay (1000); 
  Serial.print("*********Initializing Ethernet*********\n");   
  
  if (Ethernet.begin(mac) == 0) {
      Serial.println("Failed to configure Ethernet using DHCP");
      Ethernet.begin(mac, ip);
     }
  
  delay(1000);
  Serial.println("connecting...");
  Serial.print("IP Address:");
  Serial.println(Ethernet.localIP());
  Serial.print("Subnet Mask:");
  Serial.println(Ethernet.subnetMask());
  Serial.print("Default Gateway IP:");
  Serial.println(Ethernet.gatewayIP());
  Serial.print("DNS Server IP:");
  Serial.println(Ethernet.dnsServerIP());  
}


}

void loop() {
if (client.connect(server, 80))
    {
          Serial.print("Connected to server:");
          Serial.println(Ethernet.dnsServerIP());
             Serial.println("reading rfid tags....");
while(1)
{
    if (RFID.available()>0)
    { 
      
       for (i=0; i<14; i++)
       {
      rfid[i]=RFID.read();
      Serial.print(rfid[i],DEC);
       }
      Serial.println("Sending to Server...");
     
      client.print( "GET /te/add_data.php?");
    client.print("serial=");
     for (i=0; i<14; i++)
      {
      client.print(rfid[i]);
       }
    
    client.print("&&");
    client.print("temperature=");
    client.print( "0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED" );
    client.println( " HTTP/1.1");
    client.print( "Host: " );
    client.println(server);
    client.println( "Connection: close" );
    client.println();
    client.println();
    client.stop(); 
    
      Serial.println("data sent to server");
      RFID.flush();
    
     }}
   
}
}




  

}

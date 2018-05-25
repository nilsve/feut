// Libraries
#include <SPI.h>
#include <MFRC522.h>
#include <Ethernet.h>

#define SS_PIN 9
#define RST_PIN 8
MFRC522 mfrc522(SS_PIN, RST_PIN);        // MFRC522 instance.

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

IPAddress server(192,168,0,104);  // numeric IP for server (no DNS) (reduce sketch size)
IPAddress ip(192,168,0,103);      //numeric IP of the ETHERNET shield (STATIC)

EthernetClient client; //Initialize the Ethernet client library(port 80 is HTTP default):

int first=0;
int counter=0;
String uID;

void setup() {
  Serial.begin(9600);   // Initialize serial communications with the PC

       // disable SD SPI
        pinMode(4, OUTPUT);
        digitalWrite(4, HIGH);
        
        // disable w5100 SPI
        pinMode(10, OUTPUT);
        digitalWrite(10, HIGH);
        
        SPI.begin();                // Init SPI bus
        mfrc522.PCD_Init();        // Init MFRC522 card
        //Serial.println("Scan a MIFARE Classic PICC to demonstrate Value Blocks.");
        
        Ethernet.begin(mac, ip); //we used a STATIC address to start ETHERNET
    
        // print your local IP address:
        Serial.print("My IP address: ");
        for (byte thisByte = 0; thisByte < 4; thisByte++) {
        // print the value of each byte of the IP address:
        Serial.print(Ethernet.localIP()[thisByte], DEC);
        Serial.print(".");
        }
        // give the Ethernet shield a second to initialize:
        delay(1000);
        Serial.println("connecting...");
}

void loop() {

   // Prepare key - all keys are set to FFFFFFFFFFFFh at chip delivery from the factory.
        MFRC522::MIFARE_Key key;
        for (byte i = 0; i < 6; i++) {
                key.keyByte[i] = 0xFF;
        }
        // Look for new cards
        if ( ! mfrc522.PICC_IsNewCardPresent()) {
                return;
        }

        // Select one of the cards
        if ( ! mfrc522.PICC_ReadCardSerial()) {
                return;
        }
        // Now a card is selected. The UID and SAK is in mfrc522.uid.
        
        // Dump UID
        //Serial.print("Card UID:");
        
              
                
    int val1=(mfrc522.uid.uidByte[0]);
    int val2=(mfrc522.uid.uidByte[1]);
    int val3=(mfrc522.uid.uidByte[2]);
    int val4=(mfrc522.uid.uidByte[3]);
    
    String valA=String(val1);
    String valB=String(val2);
    String valC=String(val3);
    String valD=String(val4);
    uID=valA+valB+valC+valD;
    Serial.print(uID);
    Serial.println();
    counter=counter+1;    
   Serial.print(counter); 
           
         
      
          
        //} 
        Serial.println();

   
        // Halt PICC
        mfrc522.PICC_HaltA();

        // Stop encryption on PCD
        mfrc522.PCD_StopCrypto1();
        
        
        
        
                       
    if (counter>first)
    {//delay ifs
    // enable w5100 SPI
   
       
    if (client.connect(server, 80)) { //start of IF
    Serial.println("connected");
    // Make a HTTP request:
    client.print("GET /add_data.php?rfid="); //dont make these println
    client.print(uID);   //dont make these println
    client.println(" HTTP/1.1");
    client.print("Host: ");
    client.println(server);
    client.println("Connection: close");
    client.println();
    Serial.print("SENT");
    Serial.println();
    first++;
    Serial.print(first);
    Serial.println();

    while (client.connected()) {
      while (client.available()) {
        char ch = client.read();
        Serial.write(ch);
      }
    }

    client.stop();
   

}

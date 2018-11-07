## Synopsis

In this project, implemented cryptic messenger application. Users can securely communicate with each other through end-to-end encryption. Messages are encrypted using AES and DES block cipher algorithm. These algorithms use two encryption modes (CBC, OFB). User friendly interface is prepared. The interface shows both the encrypted and decrypted part of the messages.

All messages are encrypted on the client side. Only the encrypted message is sent to the server. The server is responsible for sending encrypted messages to all clients. Encrypted messages to the clients are decrypted and printed on the screen.

##Software Usage

This program is a cryptic messages system that based a graphics user interface based application. The program purpose to encrypted messaging between users.
To run the program, you must first run the Server.java class. Then the Client.java classes can be run more than once. Program interface is implemented in client class. No interface is required on the server side.
When the user clicks the connect button, the client connects to the server. The user must first enter the user name. It obtains the message data from a textbox and displays the encrypted message on the below. There are two different buttons for Encrypt and send message. Encrypt button only encrypts the message and shows it to the user. The Send button sends the encrypted message to the server. It also checks if the message is not encrypted and sends it to the server by encrypting it. Messages written by users are listed both as encrypted and decrypted in the interface. The user can select the algorithm type and encryption method to encrypt the messages. When the user clicks on the disconnect button, the server is disconnected and the program closes.

## Preview

![alt text](https://github.com/sefaozdogan/cryptic-messaging/blob/master/ss1.png)

![alt text](https://github.com/sefaozdogan/cryptic-messaging/blob/master/ss2.gif)
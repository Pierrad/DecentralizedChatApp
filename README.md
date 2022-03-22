# Chat app

## Installation

- Need GSON Jar (https://jar-download.com/artifacts/com.google.code.gson/gson/2.8.2/source-code)

## Rules

- UDP
- Distributed P2P
- Port -> 6665
- JSON {
    "to": "id",
    "test": "..."
}

## Goal

Connect and chat with all computers on the class with UDP protocol and 
distributed P2P connexion.

## Logic in progress

Every node need to notify all others clients with their IP/Port information.

A node represent a server / client side at once. 

The node has multiple jobs :
- Listening to messages.
- Send messages to all others clients.
    - Keep a list of all connected clients.
    - Check the client that has sent the message.
    - Send message to all others.
    - If message has already been received, ignored it
- Listen for incoming connexion request ?
- Listen for disconnection ?

## Links?

https://css.csail.mit.edu/6.824/2014/projects/drevo.pdf


## IP

peers.add(InetAddress.getByName("192.168.43.34"));
peers.add(InetAddress.getByName("192.168.43.36"));
peers.add(InetAddress.getByName("192.168.43.28"));
peers.add(InetAddress.getByName("192.168.43.54"));
peers.add(InetAddress.getByName("192.168.43.56"));
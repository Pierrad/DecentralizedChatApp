package chat;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

import helpers.File;
import main.Middleware;
import serializer.Serializer;
import serializer.SerializerJSON;

public class Node {
    private final DatagramSocket socket;
    private final Set<InetAddress> candidateAddresses = new HashSet<>();
    private final Set<InetAddress> peers = new HashSet<>();
    private static final int PORT = 6665;
    private final Set<Long> alreadyReceivedMessages = new HashSet<>();
    private static final Random random = new Random();
    private final int NUMBER_OF_REQUIRED_PEERS = 4;
    private Middleware middleware;
    private final String prefix = "192.168.102.";
    private final int[] suffixes = new int[] { 56, 36, 28, 54, 129, 167, 34, 79, 164, 75, 4 };
    private static final Serializer serializer = new SerializerJSON();
    // private final String prefix = "192.168.1.";
    // private final int[] suffixes = new int[] { 106 };

    private final String PSEUDO = "PA";

    /**
     * Run in terminal mode
     * @param args arguments
     * @throws SocketException exception
     * @throws UnknownHostException exception
     */
    public static void main(String[] args) throws SocketException, UnknownHostException {
        new Node(PORT).run();
    }

    /**
     * Use Chat app in Terminal mode
     * @param port port used by app
     * @throws SocketException exception
     */
    public Node(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    /**
     * Use Chat app in GUI mode
     * @param port port used by app
     * @param m The middleware that allow connection between Chat logic and GUI
     * @throws SocketException exception
     */
    public Node(int port, Middleware m) throws SocketException {
        this.socket = new DatagramSocket(port);
        this.middleware = m;
    }

    public static class Message implements Serializable {
        public long id = random.nextLong();
        public List<String> route = new ArrayList<>();
    }

    public static class TextMessage extends Message {
        public String content;
    }

    public static class ACKMessage extends Message {
        public long originalMessageID;
        public List<String> actualRecipients = new ArrayList<>();
    }


    /**
     * Run all tasks
     * @throws UnknownHostException exception
     */
    public void run() throws UnknownHostException {
        setCandidateAddresses();
        subscribeToPeers();
        launchUserListener();
        launchPacketListener();
    }

    public void setCandidateAddresses() throws UnknownHostException {
        for (var suffix : suffixes) {
            candidateAddresses.add(InetAddress.getByName(prefix + suffix));
        }
    }

    public void subscribeToPeers() {
        while (peers.size() <= NUMBER_OF_REQUIRED_PEERS) {
            var p = candidateAddresses.toArray(new InetAddress[0])[random.nextInt(candidateAddresses.size())];
            peers.add(p);
        }
        System.out.println("peers: " + peers);
    }

    /**
     * Launch only if no GUI connected
     */
    public void launchUserListener() {
        if (middleware == null) {
            new Thread(() -> {
                final Scanner userInput = new Scanner(System.in);

                try {
                    while (true) {
                        System.out.println("Please type a message: ");
                        var msg = new TextMessage();
                        msg.content = userInput.nextLine();
                        sendToAllPeers(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void launchPacketListener() {
        new Thread(() -> {
            final byte[] buf = new byte[1000000];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                try {
                    socket.receive(packet);
                    var data = Arrays.copyOf(packet.getData(), packet.getLength());
                    var msg = serializer.deserialize(data);

                    // if the message was never received
                    if (!alreadyReceivedMessages.contains(msg.id)) {
                        alreadyReceivedMessages.add(msg.id);

                        if (msg instanceof TextMessage) {
                            var ack = new ACKMessage();
                            ack.originalMessageID = msg.id;
                            ack.actualRecipients = msg.route;
                            sendToAllPeers(ack);
                        } else {
                            System.err.println("Unknown message type: " + msg.getClass().getName());
                        }

                        System.out.println(msg);
                        sendToAllPeers(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendToAllPeers(Message msg) throws IOException {
        alreadyReceivedMessages.add(msg.id);
        if (msg instanceof TextMessage) {
            String completeMessage = buildMessage((TextMessage) msg);
            showMessage(completeMessage);
            File.write("history.txt", completeMessage);
        }

        msg.route.add(PSEUDO);

        var buf = serializer.serialize(msg);
        for (var peer : peers) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length, peer, PORT);
            socket.send(packet);
        }
    }

    public String buildMessage(TextMessage msg) {
        String sender = (msg.route == null || msg.route.isEmpty()) ? PSEUDO : msg.route.get(msg.route.size() - 1);
        return "> " + sender + " \n" + msg.content + "\nRoute : " +  (msg.route == null ? "[]" :  msg.route.toString()) + "\n";
    }

    public void showMessage(String message) {
        if (middleware == null) {
            System.out.println(message);
        } else {
            middleware.showMessage(message);
        }
    }
}
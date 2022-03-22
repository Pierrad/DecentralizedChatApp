package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helpers.File;
import main.Middleware;

public class Node {
    private final DatagramSocket socket;
    private final Set<InetAddress> candidateAddresses = new HashSet<>();
    private final Set<InetAddress> peers = new HashSet<>();
    private final byte[] buf = new byte[1000000];
    private static final int PORT = 6665;
    private final Set<Long> alreadyReceivedMessages = new HashSet<>();
    private static final Random random = new Random();
    private static Gson gson;
    private final int NUMBER_OF_REQUIRED_PEERS = 0;
    private Middleware middleware;
    // private final String prefix = "192.168.43.";
    // private final int[] suffixes = new int[] { 56, 137, 36, 28, 54, 129, 167, 34, 79, 164, 75, 4 };
    private final String prefix = "192.168.1.";
    private final int[] suffixes = new int[] { 106 };

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

    /**
     * Run all tasks
     * @throws UnknownHostException exception
     */
    public void run() throws UnknownHostException {
        initGsonBuilder();
        setCandidateAddresses();
        subscribeToPeers();
        launchUserListener();
        launchPacketListener();
    }

    public void initGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
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
            System.out.println("peers: " + peers);
        }
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
                        var msg = new Message(random.nextLong());
                        msg.setContent(userInput.nextLine());
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
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                try {
                    socket.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    Message msg = gson.fromJson(received, Message.class);

                    if (!alreadyReceivedMessages.contains(msg.getId())) {
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
        String completeMessage = buildMessage(msg);
        showMessage(completeMessage);
        File.write("history.txt", completeMessage);
        msg.getRoute().add(PSEUDO);

        var json = gson.toJson(msg);
        System.arraycopy(json.getBytes(), 0, buf, 0, json.length());

        for (var peer : peers) {
            DatagramPacket packet = new DatagramPacket(buf, json.length(), peer, PORT);
            socket.send(packet);
        }
    }

    public String buildMessage(Message msg) {
        String sender = msg.getRoute().isEmpty() ? PSEUDO : msg.getRoute().get(msg.getRoute().size() - 1);
        return "> " + sender + " \n" + msg.getContent() + "\nRoute : " +  msg.getRoute().toString() + "\n";
    }

    public void showMessage(String message) {
        if (middleware == null) {
            System.out.println(message);
        } else {
            middleware.showMessage(message);
        }
    }
}
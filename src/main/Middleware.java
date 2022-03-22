package main;

import chat.Message;
import chat.Node;
import ui.GUI;

import java.io.IOException;
import java.util.Random;

public class Middleware implements Sender, Displayer {
    private final Random random = new Random();
    private Node n;
    private GUI ui;

    public void setN(Node n) {
        this.n = n;
    }

    public void setUi(GUI ui) {
        this.ui = ui;
    }

    @Override
    public void showMessage(String message) {
        ui.appendToTextArea(message);
    }

    @Override
    public void sendMessage(String message) throws IOException {
        n.sendToAllPeers(new Message(random.nextLong(), message));
    }
}

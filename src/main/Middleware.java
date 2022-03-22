package main;

import chat.Message;
import chat.Node;
import helpers.File;
import ui.GUI;

import java.io.IOException;
import java.util.Random;

public class Middleware implements Sender, Displayer {
    private final Random random = new Random();
    private Node node;
    private GUI ui;

    public void setNode(Node n) {
        this.node = n;
    }

    public void setUI(GUI ui) {
        this.ui = ui;
    }

    @Override
    public void showMessage(String message) {
        ui.appendToTextArea(message);
    }

    @Override
    public void sendMessage(String message) throws IOException {
        node.sendToAllPeers(new Message(random.nextLong(), message));
    }

    @Override
    public void reloadMessages(String fileName) throws IOException {
        ui.appendToTextArea(File.read(fileName));
    }
}

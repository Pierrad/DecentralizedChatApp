package main;

import chat.Node;
import helpers.File;
import ui.GUI;

import java.io.IOException;

public class Middleware implements Sender, Displayer {
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
        Node.TextMessage msg = new Node.TextMessage();
        msg.content = message;
        node.sendToAllPeers(msg);
    }

    @Override
    public void reloadMessages(String fileName) throws IOException {
        ui.appendToTextArea(File.read(fileName));
    }
}

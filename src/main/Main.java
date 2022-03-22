package main;

import chat.Node;
import ui.GUI;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Run in GUI mode
 */
public class Main {
    private static final Middleware mediator = new Middleware();

    public static void main(String[] args) throws SocketException {
        var ui = new GUI(mediator);
        var node = new Node(6665, mediator);
        mediator.setUi(ui);
        mediator.setN(node);

        new Thread(ui::run).start();
        new Thread(() -> {
            try {
                node.run();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }).start();
    }
}






















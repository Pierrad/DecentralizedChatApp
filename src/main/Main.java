package main;

import chat.Node;
import ui.GUI;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Run in GUI mode
 */
public class Main {
    private static final Middleware middleware = new Middleware();

    public static void main(String[] args) throws IOException {
        var ui = new GUI(middleware);
        var node = new Node(6665, middleware);
        middleware.setUI(ui);
        middleware.setNode(node);

        new Thread(() -> {
            ui.run();
            try {
                middleware.reloadMessages("history.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                node.run();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }).start();
    }
}






















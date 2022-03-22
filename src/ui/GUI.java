package ui;

import main.Middleware;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI {
    private Middleware middleware;
    private static JLabel label;
    private static JTextArea textArea;
    private static JTextField textField;
    private static JButton sendButton;

    public GUI(Middleware m) {
        this.middleware = m;
    }

    public void run() {
        JFrame frame = new JFrame("DUT Decentralized messaging app");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        textArea = new JTextArea("Welcome to the Chat room\n", 80, 80);
        JPanel centerPanel = new JPanel(new GridLayout(1,1));
        centerPanel.add(new JScrollPane(textArea));
        textArea.setEditable(false);

        JPanel southPanel = new JPanel(new GridLayout(3,1));
        label = new JLabel("Enter your message...", SwingConstants.CENTER);
        southPanel.add(label);
        textField = new JTextField("");
        textField.setBackground(Color.WHITE);
        southPanel.add(textField);
        sendButton = new JButton("Send");
        southPanel.add(sendButton);
        sendButton.addActionListener(ae -> {
            try {
                middleware.sendMessage(textField.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
            textField.setText("");
        });

        frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
        frame.setVisible(true);
    }

    public void appendToTextArea(String str) {
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length() - 1);
    }
}

package main;

import java.io.IOException;

public interface Sender {
    void sendMessage(String message) throws IOException;
}

package main;

import java.io.IOException;

public interface Displayer {
    void showMessage(String message);
    void reloadMessages(String fileName) throws IOException;
}

package serializer;

import chat.Node;

import java.io.IOException;

public interface Serializer {
    byte[] serialize(Node.Message msg) throws IOException;
    Node.Message deserialize(byte[] bytes) throws IOException;
}

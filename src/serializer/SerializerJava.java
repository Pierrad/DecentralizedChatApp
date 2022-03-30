package serializer;

import chat.Node;

import java.io.*;

public class SerializerJava implements Serializer {
    private final byte[] buf = new byte[1000000];

    @Override
    public byte[] serialize(Node.Message msg) throws IOException {
        var bos = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        return bos.toByteArray();
    }

    @Override
    public Node.Message deserialize(byte[] bytes) throws IOException {
        var bis = new ByteArrayInputStream(bytes);
        var ois = new ObjectInputStream(bis);

        try {
            var m = (Node.Message) ois.readObject();
            ois.close();
            return m;
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}

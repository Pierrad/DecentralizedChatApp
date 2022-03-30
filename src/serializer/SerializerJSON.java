package serializer;

import chat.Node;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerializerJSON implements Serializer {
    static final Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    @Override
    public byte[] serialize(Node.Message msg) {
        return gson.toJson(msg).getBytes();
    }

    @Override
    public Node.Message deserialize(byte[] bytes) throws IOException {
        String received = new String(bytes);

        // deserialize the UDP packet to a Message object
        return gson.fromJson(received, Node.Message.class);
    }
}

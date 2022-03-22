package chat;

import java.util.ArrayList;
import java.util.List;

public class Message {
    public long id;
    public String content;
    public List<String> route = new ArrayList<>();

    public Message(long id) {
        this.id = id;
    }

    public Message(long id, String msg) {
        this.id = id;
        this.content = msg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }
}

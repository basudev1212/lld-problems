package model;

import java.util.UUID;
import enums.PublisherType;

public class Publisher {

    private final String id;
    private final String name;
    private final PublisherType type;

    public Publisher(String name, PublisherType type) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PublisherType getType() {
        return type;
    }
}

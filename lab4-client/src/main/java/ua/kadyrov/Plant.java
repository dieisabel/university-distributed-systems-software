package ua.kadyrov;

import java.io.Serializable;

public class Plant implements Serializable {
    private String name;
    private String family;

    public Plant() {}

    public Plant(String name, String family) {
        this.name = name;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}

package me.tuong.chodinh.main.home.model;

import java.util.List;

public class Province {
    private String Id;
    private String Name;
    private List<Districts> Districts;

    public Province(String id, String name, List<Districts> districts) {
        Id = id;
        Name = name;
        Districts = districts;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Districts> getDistricts() {
        return Districts;
    }

    public void setDistricts(List<Districts> districts) {
        Districts = districts;
    }
}

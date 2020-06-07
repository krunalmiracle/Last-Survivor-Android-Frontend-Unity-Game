package edu.upc.eetac.dsa.lastsurvivorfrontend.models;

public class Map {
    private String id;
    private String name;
    private int level; //Indicates the level and the precedence of use in Unity
    private String type1Map;
    private String type2Objects;//All of the Object and Enemy Position
    public Map() {}
    //Complete Constructor

    public Map(String id, String name, int level, String type1Map,String type2Objects) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.type1Map = type1Map;
        this.type2Objects = type2Objects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getType1Map() {
        return type1Map;
    }

    public void setType1Map(String type1Map) {
        this.type1Map = type1Map;
    }

    public String getType2Objects() {
        return type2Objects;
    }

    public void setType2Objects(String type2Objects) {
        this.type2Objects = type2Objects;
    }
}

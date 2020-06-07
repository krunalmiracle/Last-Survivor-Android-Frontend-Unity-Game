package edu.upc.eetac.dsa.lastsurvivorfrontend.models;

import java.util.LinkedList;
import java.util.List;

public class Item {

    private String id;
    private String parentId ="";
    private String name;
    private String type;
    private String rarity;
    private int credit;
    private String description;
    private int offense;
    private int defense;
    //Empty Constructor
    public Item() {
    }
    public Item(String parentId, String name, String type, String rarity, String description, int credit, int offense, int defense) {
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.description = description;
        this.credit = credit;
        this.offense = offense;
        this.defense = defense;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() { return parentId;}

    public void setParentId(String parentId) {this.parentId = parentId;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRarity() {
        return rarity;
    }
    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {this.credit = credit;}

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getOffense() {
        return offense;
    }

    public void setOffense(int offense) {
        this.offense = offense;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}

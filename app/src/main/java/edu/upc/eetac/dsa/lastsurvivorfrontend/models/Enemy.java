package edu.upc.eetac.dsa.lastsurvivorfrontend.models;

public class Enemy {
    private String name;
    private String avatar;
    private String id;
    private String description;
    private int experience;
    private int damage;
    private int health;

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public void setAvatar(String avatar){
        this.avatar=avatar;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public int getExperience(){
        return this.experience;
    }
    public void setExperience(int exp){
        this.experience=exp;
    }
    public int getDamage(){
        return this.damage;
    }
    public void setDamage(int damage){
        this.damage=damage;
    }
    public int getHealth(){
        return this.health;
    }
    public void setHealth(int health){
        this.health=health;
    }


}

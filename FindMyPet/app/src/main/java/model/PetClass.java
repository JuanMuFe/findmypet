package model;

public class PetClass {

    private int id;
    private int idOwner;
    private String name;
    private String race;
    private String image;
    private String description;

    public PetClass() {
    }

    public PetClass(int idOwner, String name, String race, String image, String description) {
        this.idOwner = idOwner;
        this.name = name;
        this.race = race;
        this.image = image;
        this.description = description;
    }

    public PetClass(int id, int idOwner, String name, String race, String image, String description) {
        this.id = id;
        this.idOwner = idOwner;
        this.name = name;
        this.race = race;
        this.image = image;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}

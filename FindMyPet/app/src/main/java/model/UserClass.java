package model;

public class UserClass {
    private int id;
    private int idProfile;
    private String userName;
    private String password;
    private String email;
    private int active;
    private String image;

    public UserClass() {
    }

    public UserClass(int id, int idProfile, String userName, String password, String email, int active, String image) {
        this.id = id;
        this.idProfile = idProfile;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.active = active;
        this.image = image;
    }

    public UserClass(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public UserClass(int idProfile, String userName, String password, String email, int active, String image) {
        this.idProfile = idProfile;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.active = active;
        this.image = image;
    }

    public UserClass(int idProfile, String userName, String password, String email,int active) {
        this.idProfile = idProfile;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.active = active;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    
    
}

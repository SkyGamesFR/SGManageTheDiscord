package fr.skygames.managethediscord.data;

public class Roles {

    private String id;
    private String name;
    private Boolean isStaff;

    public Roles(String id, String name, Boolean isStaff) {
        this.id = id;
        this.name = name;
        this.isStaff = isStaff;
    }

    public Roles(String id, String name) {this(id, name, false);}

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

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }

}

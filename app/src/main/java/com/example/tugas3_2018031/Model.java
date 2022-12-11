package com.example.tugas3_2018031;

public class Model {
    private int id;
    private byte[]proavatar;
    private String usertitle;
    private String userdescription;

    public Model(int id, byte[] proavatar, String usertitle, String userdescription) {
        this.id = id;
        this.proavatar = proavatar;
        this.usertitle = usertitle;
        this.userdescription = userdescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getProavatar() {
        return proavatar;
    }

    public void setProavatar(byte[] proavatar) {
        this.proavatar = proavatar;
    }

    public String getUsertitle() {
        return usertitle;
    }

    public void setUsertitle(String usertitle) {
        this.usertitle = usertitle;
    }

    public String getUserdescription() {
        return userdescription;
    }

    public void setUserdescription(String userdescription) {
        this.userdescription = userdescription;
    }

}

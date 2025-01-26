package org.spring.newidea.Model;

public class CREDENTIALS {
    private String text;
    private String password;
    private String newpassword;

    public CREDENTIALS() {}
    public CREDENTIALS(String text, String password,String newpassword) {
        this.text = text;
        this.password = password;
        this.newpassword = newpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getText() {return text;}
    public String getPassword() {return password;}
    public void setText(String text) {this.text = text;}
    public void setPassword(String password) {this.password = password;}
}

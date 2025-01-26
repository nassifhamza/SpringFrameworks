package org.spring.newidea.Model;

public class userDto {
  private User user;
private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public userDto() {
    }

    public userDto(User user,String token) {

this.user=user;
this.token=token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

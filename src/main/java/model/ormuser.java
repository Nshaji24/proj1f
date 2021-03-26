package model;

import annotations.Column;
import annotations.PrimaryKey;
import org.junit.jupiter.api.Test;

//create user class

public class ormuser {
//custom primary key annotation
    @PrimaryKey
    private int id;
//custom column annotation
    @Column
    private String username;

    @Column
    private String password;

    public ormuser(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public ormuser(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

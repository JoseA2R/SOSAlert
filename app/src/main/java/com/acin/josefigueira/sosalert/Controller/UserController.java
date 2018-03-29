package com.acin.josefigueira.sosalert.Controller;

import com.acin.josefigueira.sosalert.POJO.User;

/**
 * Created by jose.figueira on 28-03-2018.
 */

public class UserController {

    private User user;

    public UserController(){

        this.user = new User();

    }

    public void SaveData(User user){
        this.user = user;
    }

    public void ClearUser(){

        this.user.setFirstName("");
        this.user.setLastname("");
        this.user.setCountry("");
        this.user.setDescription("");
        this.user.setPhone("");

    }

    public String viewData(){

        String userdata = "";
        userdata += "First Name = " + this.user.getFirstName() + "\n" +
                "Last Name = " + this.user.getLastName() + "\n" +
                "Country = " + this.user.getCountry() + "\n" +
                "Description = " + this.user.getDescription() + "\n" +
                "Phone = " + this.user.getPhone() + "\n";

        return userdata;

    }

}

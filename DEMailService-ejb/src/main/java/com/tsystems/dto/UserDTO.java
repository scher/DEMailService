/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.dto;

import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

/**
 *
 * @author apronin
 */
public class UserDTO {
    @NotNull(message = "This field is required")
    @Pattern(regexp="[A-Z][a-z]+",
            message = "User name must starts with capital letter followed by small letters")
    private String name;
    @NotNull(message = "This field is required")
    @Pattern(regexp="[A-Z][a-z]+",
            message = "User surname must starts with capital letter followed by small letters")
    private String surname;
    
    @NotNull(message = "This field is required")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "This address is invalid mail address")
    private String mailAddress;

    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "This address is invalid mail address")
    private String secondEmail;

    @NotNull(message = "This field is required")
    private String password;
    
    @Past
    @NotNull(message = "Suppose you want to fill in this field")
    private Date birthday;
    @Min(value = 10, message = "Phone number is too short")
    private Integer phoneNumber;



    public UserDTO() {
    }

    public UserDTO(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getSecondEmail() {
        return secondEmail;
    }

    public void setSecondEmail(String secondEmail) {
        this.secondEmail = secondEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.jsf;

import com.tsystems.dto.UserDTO;
import com.tsystems.ejb.MailAddressBean;
import com.tsystems.ejb.UserBean;
import com.tsystems.exception.DEMailException;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author apronin
 */
@Named(value = "registrationBean")
@SessionScoped
public class RegistrationBean implements Serializable {

    @EJB
    UserBean userBean;

    UserDTO newUserDTO;

    public RegistrationBean() {
        newUserDTO = new UserDTO();
    }

    public String register() {
        try {
            userBean.register(newUserDTO);
        } catch (DEMailException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Error: " + e.message));
            return "rerender";
        }
        newUserDTO = new UserDTO();
        return "ok";
    }

    public String back() {
        return "back";
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public UserDTO getNewUserDTO() {
        return newUserDTO;
    }

    public void setNewUserDTO(UserDTO newUserDTO) {
        this.newUserDTO = newUserDTO;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.jsf;

import com.tsystems.dto.UserDTO;
import com.tsystems.ejb.MailAddressBean;
import com.tsystems.ejb.UserBean;
import com.tsystems.exception.DEMailException;
import com.tsystems.exception.FatalDEMailException;


import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;


/**
 * @author apronin
 */
@SessionScoped
@Named(value = "loginBean")
public class LoginBean implements Serializable {

    @EJB
    private UserBean userBean;
    @EJB
    private MailAddressBean mailAddressBean;

    private UserDTO userDTO;
    private String secondEmail;

    public LoginBean() {
        userDTO = new UserDTO();
    }

    public String remind() {
        if (secondEmail != null) {
            try {
                mailAddressBean.sendReminder(secondEmail);
            } catch (DEMailException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Error: " + e.message));
                return "rerender";
            }
        }
        return "rerender";
    }


    public String login() {
        FacesContext cxt = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) cxt.getExternalContext().getSession(true);

        try {
            userBean.login(userDTO);
            session.setAttribute("userLogin", userDTO.getMailAddress());
            return "ok";
        } catch (DEMailException e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(e.message));
            return "rerender";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getSecondEmail() {
        return secondEmail;
    }

    public void setSecondEmail(String secondEmail) {
        this.secondEmail = secondEmail;
    }

    public String register() {
        return "register";
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}

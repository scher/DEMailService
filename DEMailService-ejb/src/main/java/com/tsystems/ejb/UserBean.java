/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.ejb;

import com.tsystems.dao.FolderDAO;
import com.tsystems.dao.LetterDAO;
import com.tsystems.dao.MailAddressDAO;
import com.tsystems.dao.UserDAO;
import com.tsystems.dto.UserDTO;
import com.tsystems.entity.Folder;
import com.tsystems.entity.MailAddress;
import com.tsystems.entity.User;
import com.tsystems.exception.DEMailException;


import java.util.logging.Logger;
import javax.ejb.EJB;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;


/**
 * @author scher
 */
@Stateless
public class UserBean {

    @EJB
    private UserDAO userDAO;
    @EJB
    private MailAddressDAO mailAddressDAO;
    @EJB
    private FolderDAO folderDAO;

    private static final Logger logger = Logger.getLogger(
            "com.tsystems.ejb.UserBean");

    public void register(UserDTO newUserDTO) throws DEMailException, IllegalArgumentException {

        if (newUserDTO == null) {
            throw new IllegalArgumentException();
        }

        User newUser = new User(newUserDTO);
        MailAddress mailAddress = new MailAddress(newUserDTO);
        mailAddress.setService_user(newUser);

        Folder inbox = new Folder();
        inbox.setName("inbox");

        Folder outbox = new Folder();
        outbox.setName("outbox");

        if (newUserDTO.getSecondEmail() != null){
            MailAddress secondMailAddress = mailAddressDAO.find(newUserDTO.getSecondEmail());
            if (secondMailAddress == null){
                throw new DEMailException("Can not find such user in DataBase");
            }
            if (secondMailAddress.getForgeter() != null){
                throw new DEMailException("This mail address is already connected to another account");
            }

            newUser.setSecondMailAddress(secondMailAddress);
        }

        userDAO.create(newUser);

        try {
            mailAddressDAO.create(mailAddress);
        } catch (EJBTransactionRolledbackException e) {
            throw new DEMailException("Seem that this mail address already exists");
        }

        inbox.setMailAddress(mailAddress);
        outbox.setMailAddress(mailAddress);
        folderDAO.create(inbox);
        folderDAO.create(outbox);
    }

    public void login(UserDTO userDTO) throws IllegalArgumentException, DEMailException {
        if ((userDTO == null)
                || (userDTO.getMailAddress() == null)
                || (userDTO.getPassword() == null)) {
            String errorMsg = "Invalid UserDTO object. Unable to implement login logic";
            logger.severe(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        logger.info("Login request from user: " + userDTO.getMailAddress());

        MailAddress mailAddress = new MailAddress();
        mailAddress.setMailAddress(userDTO.getMailAddress());
        mailAddress.setPassword(userDTO.getPassword());

        if (!mailAddressDAO.checkPasswd(mailAddress)) {
            logger.info("Invalid login credentials for user: " + userDTO.getMailAddress());
            throw new DEMailException("Login or password is incorrect");
        } else {
            logger.info("User: " + userDTO + " logged in");
        }
    }
}

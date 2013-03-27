/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tsystems.jsf;


import com.tsystems.dao.FolderDAO;
import com.tsystems.dto.FolderDTO;
import com.tsystems.dto.LetterDTO;
import com.tsystems.dto.UserDTO;
import com.tsystems.ejb.FolderBean;
import com.tsystems.ejb.LetterBean;
import com.tsystems.exception.DEMailException;
import com.tsystems.exception.FatalDEMailException;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

/**
 * @author apronin
 */
@SessionScoped
@Named(value = "mailBoxBean")
public class MailBoxBean implements Serializable {

    private static final Logger logger = Logger.getLogger("com.tsystems.jsf.MailBoxBean");

    @EJB
    FolderBean folderBean;
    @EJB
    private FolderDAO folderDAO;
    @EJB
    private LetterBean letterBean;

    private UserDTO userDTO;
    private FolderDTO folderDTO = null;
    private LetterDTO letterDTO = null;

    private String critErrorMsg;
    private boolean displayErrorDlg = false;


//    public void gmail() {
//        logger.info("Sending message via Gmail SMTP");
//
//        Properties props = new Properties();
//
//        props.setProperty("proxySet","true");
//        props.setProperty("socksProxyHost","proxy.t-systems.ru");
//        props.setProperty("socksProxyPort","3128");
//
//
//
//        props.put("mail.smtp.user", "scherdemail@gmail.com");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        props.put("mail.smtp.debug", "true");
//
//        props.put("mail.smtp.socketFactory.port", "587");
//        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.socketFactory.fallback", "false");
//
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("scherdemail@gmail.com", "Qwertyzxcoop");
//                    }
//                });
//
//        try {
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("scherkka@gmail.com"));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress("scherkka@gmail.com"));
//            message.setSubject("Testing Subject");
//            message.setText("Dear Mail Crawler," +
//                    "\n\n No spam to my email, please!");
//
//            Transport transport = session.getTransport("smtp");
//
//            transport.connect("smtp.gmail.com", 587, "scherdemail", "Qwertyzxcoop");
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//
//            System.out.println("Done");
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public boolean isDisplayErrorDlg() {
        return displayErrorDlg;
    }

    public void setDisplayErrorDlg(boolean displayErrorDlg) {
        this.displayErrorDlg = displayErrorDlg;
    }

    public String getCritErrorMsg() {
        return critErrorMsg;
    }

    public MailBoxBean() {
    }

    private void addFacesMsg(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Error: " + msg));
    }

    @PostConstruct
    public void initSessionState() throws DEMailException, FatalDEMailException {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session == null) {
            throw new DEMailException("No session exception");
        }

        String userLogin = (String) session.getAttribute("userLogin");
        if (userLogin == null) {
            logger.severe("Unable to get userLogin session attribute");
            throw new DEMailException("userLogin session attribute was not set for session");
        }

        userDTO = new UserDTO(userLogin);

        folderDTO = new FolderDTO();
        // update user folders and set default folder to "inbox"
        folderBean.updateFolders(folderDTO, userDTO.getMailAddress());

        String defaultFolder = folderDTO.getFolders().get("inbox").toString();
        if (defaultFolder == null) {
            logger.severe("User: " + userDTO.getMailAddress() + " does bot have inbox folder");
            throw new DEMailException("Unable to find default(inbox) folder");
        }
        folderDTO.setSelectedFolder(defaultFolder);

        letterDTO = new LetterDTO();
        letterBean.updateLetters(letterDTO, folderDTO.getSelectedFolder(), userDTO.getMailAddress());
        if (letterDTO.getLetters().isEmpty()) {
            letterDTO.clearCurrentLetter();
        } else {
            letterDTO.setCurrentLetter(letterDTO.getLetters().get(0));
        }
    }

    public void updateCritErrorMsg() {
        critErrorMsg = critErrorMsg;
    }

    //Really bad bad case
    public void onErrorLogout(String errorMessage) {
        critErrorMsg = "Critical error: " + errorMessage;
        displayErrorDlg = true;
    }

    public void updateState() {
        try {
            folderBean.updateFolders(folderDTO, userDTO.getMailAddress());
        } catch (FatalDEMailException e) {
            onErrorLogout(e.message);
            return;
        }
        if (folderDTO.getSelectedFolder() != null) {
            try {
                letterBean.updateLetters(letterDTO, folderDTO.getSelectedFolder(), userDTO.getMailAddress());
            } catch (DEMailException e) {
                addFacesMsg(e.message);
            }
        }
    }

    public String logout() {
        logger.info("Logout user: " + userDTO.getMailAddress());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "logout?faces-redirect=true";
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

//////////////////// Folder logic /////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////

    private boolean preFolderAction() {
        if (folderDTO.getSelectedFolder() == null) {
            addFacesMsg("You must select a folder to perform this action");
            return false;
        }
        return true;
    }

    public String selectFolder() {
        if (preFolderAction()) {
            try {
                letterBean.updateLetters(letterDTO, folderDTO.getSelectedFolder(), userDTO.getMailAddress());
                if (letterDTO.getLetters().isEmpty()) {
                    letterDTO.clearCurrentLetter();
                } else {
                    letterDTO.setCurrentLetter(letterDTO.getLetters().get(0));
                }
            } catch (DEMailException e) {
                addFacesMsg(e.message);
            }

        }
        return "rerender";
    }

    public String deleteFolder() {
        if (preFolderAction()) {
            try {
                folderBean.deleteFolder(folderDTO.getSelectedFolder());
            } catch (DEMailException e) {
                addFacesMsg(e.message);
            }
        }
        try {
            folderBean.updateFolders(folderDTO, userDTO.getMailAddress());
        } catch (FatalDEMailException e) {
            onErrorLogout(e.message);
            return "rerender";
        }
        folderDTO.setSelectedFolder(null);
        letterDTO.clearCurrentLetter();
        letterDTO.clearLetters();
        return "rerender";
    }

    public String createFolder() {
        try {
            if (folderDTO.getNewFolder() == null) {
                throw new DEMailException("You must specify name for new folder");
            }
            folderBean.createFolder(userDTO.getMailAddress(), folderDTO.getNewFolder());
        } catch (DEMailException e) {
            addFacesMsg(e.message);
        }
        try {
            ////////////////  uncomment for critical error case
            /////////////////////////////////////////////////////
            //folderBean.updateFolders(folderDTO, "sdsdsd");
            /////////////////////////////////////////////////////
            folderBean.updateFolders(folderDTO, userDTO.getMailAddress());
        } catch (FatalDEMailException e) {
            onErrorLogout(e.message);
            return "rerender";
        }
        folderDTO.setNewFolder(null);
        return "rerender";
    }

    public String renameFolder() {
        if (preFolderAction()) {
            try {
                if (folderDTO.getNewFolder() == null) {
                    throw new DEMailException("Calm down. You must specify new folder's name");
                }
                folderBean.renameFolder(userDTO.getMailAddress(),
                        folderDTO.getSelectedFolder(), folderDTO.getNewFolder());
            } catch (DEMailException e) {
                folderDTO.setSelectedFolder(null);
                addFacesMsg(e.message);
            }
        }

        try {
            folderBean.updateFolders(folderDTO, userDTO.getMailAddress());
        } catch (FatalDEMailException e) {
            onErrorLogout(e.message);
            return "rerender";
        }
        folderDTO.setNewFolder(null);
        return "rerender";
    }

    public FolderDTO getFolderDTO() {
        return folderDTO;
    }

    ////////////////////  End of Folder logic /////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    ////////////////////  Letters logic ///////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////


    public LetterDTO getLetterDTO() {
        return letterDTO;
    }

    public void setLetterDTO(LetterDTO letterDTO) {
        this.letterDTO = letterDTO;
    }

    public void cleanNewLetter() {
        letterDTO.clearCurrentLetter();
    }

    public String showLetter(LetterDTO.Letter letter) {
        if (letter == null) {
            addFacesMsg("You must select a letter to view it's content");
        } else {
            letter.setFullContents(letter.toString());
            letterDTO.setCurrentLetter(letter);
            try {
            letterBean.setAsRead(letterDTO, userDTO.getMailAddress());
            } catch (DEMailException e){
                addFacesMsg(e.message);
                return "rerender";
            }
        }
        return "rerender";
    }

    public String sendNewLetter() {
        letterDTO.getCurrentLetter().setSender(getUserDTO().getMailAddress());
        try {
            letterBean.sendLetter(letterDTO.getCurrentLetter());
            letterDTO.clearCurrentLetter();
        } catch (DEMailException e) {
            addFacesMsg(e.message);
        }
        return "rerender";
    }

    public String changeFolder(LetterDTO.Letter selectedLetter) {
        try {
            if (selectedLetter == null) {
                throw new DEMailException("Letter was not selected");
            }
            letterBean.changeFolder(folderDTO, selectedLetter.getId());
            folderBean.updateFolders(folderDTO, userDTO.getMailAddress());
            letterBean.updateLetters(letterDTO, folderDTO.getSelectedFolder(), userDTO.getMailAddress());
        } catch (DEMailException e) {
            addFacesMsg(e.message);
        } catch (FatalDEMailException e) {
            onErrorLogout(e.message);
            return "rerender";
        }
        folderDTO.setNewFolder(null);
        return "rerender";
    }

    public String deleteLetter(LetterDTO.Letter selectedLetter) {
        try {
            if (selectedLetter == null) {
                throw new DEMailException("Letter was not selected");
            }
            if (folderDTO.getSelectedFolder() == null){
                throw new DEMailException("Letter was not selected");
            }
            letterBean.deleteLetter(Long.parseLong(folderDTO.getSelectedFolder()),
                    selectedLetter.getId());
        } catch (DEMailException e) {
            addFacesMsg(e.message);
        }
        return "rerender";
    }

    ////////////////////  End of letters logic /////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

}

package com.tsystems.ejb;


import com.tsystems.dao.FolderDAO;
import com.tsystems.dao.LetterDAO;
import com.tsystems.dao.MailAddressDAO;
import com.tsystems.dao.UserDAO;
import com.tsystems.dto.FolderDTO;
import com.tsystems.dto.LetterDTO;

import com.tsystems.entity.Folder;
import com.tsystems.entity.Letter;
import com.tsystems.entity.MailAddress;
import com.tsystems.entity.User;
import com.tsystems.exception.DEMailException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;


/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 16.03.13
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "LetterBean")
public class LetterBean {
    private static final Logger logger = Logger.getLogger("com.tsystems.ejb.LetterBean");

    @EJB
    FolderDAO folderDAO;
    @EJB
    MailAddressDAO mailAddressDAO;
    @EJB
    LetterDAO letterDAO;
    @EJB
    UserDAO userDAO;

    public LetterBean() {
    }

    public void updateLetters(LetterDTO letterDTO, String folderId, String mailAddress) throws DEMailException {
        if (letterDTO == null) {
            throw new IllegalArgumentException();
        }

        Folder folderEntity = folderDAO.find(Long.parseLong(folderId));
        if (folderEntity == null) {
            throw new DEMailException("Selected folder not found. Unable to show letters for this folder");
        }

        MailAddress mailAddressEntity = mailAddressDAO.find(mailAddress);
        if (mailAddressEntity == null) {
            throw new DEMailException("Unable to determine current user.");
        }
        User user = mailAddressEntity.getService_user();


        letterDTO.clearLetters();

        boolean unread;
        for (Letter l : folderEntity.getLetters()) {
            unread = l.getReaders().contains(user);
//            letterDAO.refresh(l);
//            userDAO.refresh(l.getSender());
//            userDAO.refresh(l.getRecipients());
            LetterDTO.Letter letter = new LetterDTO.Letter(
                    l.getId(),
                    l.getRecipientsAsString(),
                    l.getSenderAsString(),
                    l.getDate(),
                    l.getThread(),
                    l.getData(),
                    unread);

            letterDTO.addLetter(letter);
        }
    }

    public void sendLetter(LetterDTO.Letter newLetter) throws DEMailException {
        if (newLetter == null) {
            throw new IllegalArgumentException();
        }

        logger.info("Sending new letter from " + newLetter.getSender() + " to " + newLetter.getRecipients());
        MailAddress sendMailAddress = mailAddressDAO.find(newLetter.getSender());

        // parse recipients field into tokens
        String delims = "[ ,;]+";
        newLetter.setRecipients(newLetter.getRecipients().trim());
        String[] recipientsToCheck = null;
        try {
            recipientsToCheck = newLetter.getRecipients().split(delims);
        } catch (PatternSyntaxException e) {
            throw new DEMailException("Invalid input of Recipient field");
        }

        // prepare list of valid recipients
        MailAddress recMailAddress = null;
        List<User> checkedRecipients = new ArrayList<User>();
        // prepare list of default folders
        List<Folder> folders = new ArrayList<Folder>();

        for (int i = 0; i < recipientsToCheck.length; i++) {

            recMailAddress = mailAddressDAO.find(recipientsToCheck[i]);

            if (recMailAddress == null) {
                logger.info("Invalid recipient mail found(" + newLetter.getRecipients() + ")." +
                        " Notification letter will bew sent to " + newLetter.getSender());
//                throw new DEMailException("We do not know this guy: " + newLetter.getRecipients()
//                        + ". Check email address for correctness");
                sendNotExistsResponce(newLetter, recipientsToCheck[i]);
                continue;
            }

            //user is found. Now add him to recipients list of the letter
            checkedRecipients.add(recMailAddress.getService_user());
            // and add this letter to his inbox folder
            folders.add(folderDAO.find(recMailAddress.getMailAddress(), "inbox"));

        }

        if (!checkedRecipients.isEmpty()) {

            //add this letter to sender's outbox folder
            folders.add(folderDAO.find(sendMailAddress.getMailAddress(), "outbox"));

            Set<User> unread = new HashSet<User>(checkedRecipients);

            Letter letterEntity = new Letter();
            letterEntity.setData(newLetter.getData());
            letterEntity.setDate(new Date());
            letterEntity.setThread(newLetter.getThread());

            letterEntity.setSender(sendMailAddress.getService_user());
            letterEntity.setRecipients(checkedRecipients);

            letterEntity.setReaders(unread);

            letterEntity.setFolders(folders);

            letterDAO.create(letterEntity);
            logger.info("Copy of new letter(id=" + letterEntity.getId() + ") for Recipient: "
                    + newLetter.getRecipients() + " created.");

        }
    }

    private void sendNotExistsResponce(LetterDTO.Letter originLetter, String invalidRecipient) throws DEMailException {
        logger.info("Sending notification response concerning invalid recpient: " + invalidRecipient +
                " to sender " + originLetter.getSender());
        if (originLetter == null) {
            throw new IllegalArgumentException();
        }
        if (invalidRecipient == null) {
            throw new IllegalArgumentException();
        }
        LetterDTO.Letter notificationLetter = new LetterDTO.Letter();
        notificationLetter.setData("You receive this notification letter because your outgoing" +
                " letter could not be delivered to recipient: " + invalidRecipient
                + "<br/><br/>\"<br/>" + originLetter.toString() + "  \"<br/><br/>" +
                "Please do not reply to this message.<br/>" +
                "Best regards. Your DE Mail Service.");
        notificationLetter.setDate(new Date());
        notificationLetter.setSender("noreply@demail.com");
        notificationLetter.setRecipients(originLetter.getSender());
        notificationLetter.setThread("Notification: Unable to deliver letter to " + invalidRecipient);

        sendLetter(notificationLetter);
    }

    public void changeFolder(FolderDTO folderDTO, Long letterId) throws DEMailException {
        if (folderDTO == null) {
            throw new IllegalArgumentException();
        }

        Letter letterEntity = letterDAO.find(letterId);

        if (letterEntity == null) {
            throw new DEMailException("Unable to find selected letter.");
        }
        Folder newFolderEntity = folderDAO.find(Long.parseLong(folderDTO.getNewFolder()));
        Folder oldFolderEntity = folderDAO.find(Long.parseLong(folderDTO.getSelectedFolder()));

        if (newFolderEntity == null) {
            throw new DEMailException("Destination folder not found.");
        }
        if (oldFolderEntity == null) {
            throw new DEMailException("Unable to determine current folder of the letter. Really strange");
        }

        logger.info("Moving letter id=" + letterEntity.getId() + " from folder id=" + folderDTO.getSelectedFolder()
                + " to id=" + newFolderEntity.getId());
        List<Folder> folders = letterEntity.getFolders();
        folders.remove(oldFolderEntity);
        folders.add(newFolderEntity);
        // letterEntity.setFolder(newFolderEntity);
        letterDAO.edit(letterEntity);

    }

    public void deleteLetter(Long folderId, Long letterId) {
        Folder folder = folderDAO.find(folderId);
        Letter letter = letterDAO.find(letterId);

        List<Folder> folders = letter.getFolders();
        for (int i = 0; i < folders.size(); i++) {
            if (folders.get(i).equals(folder)) {
                logger.info("Removing letter id=" + letterId
                        + " from folder with id=" + folder.getId());
                folders.remove(i);
                break;
            }
        }
        if (folders.isEmpty()) {
            letterDAO.remove(letterDAO.find(letterId));
        }
    }

    public void setAsRead(LetterDTO letterDTO, String mailAddress) throws DEMailException {
        Letter letter = null;
        letter = letterDAO.find(letterDTO.getCurrentLetter().getId());
        if (letter == null) {
            throw new DEMailException("Unable to find letter.");
        }

        User user = null;
        try {
            user = mailAddressDAO.find(mailAddress).getService_user();
        } catch (NullPointerException e){
            throw new DEMailException("Invalid email address.");
        }

        letter.getReaders().remove(user);
        letterDTO.getCurrentLetter().setUnread(false);
    }
}

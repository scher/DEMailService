package com.tsystems.ejb;

import com.tsystems.dao.MailAddressDAO;
import com.tsystems.dto.LetterDTO;
import com.tsystems.entity.MailAddress;
import com.tsystems.exception.DEMailException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 22.03.13
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */

@Stateless
public class MailAddressBean {

    @EJB
    private MailAddressDAO mailAdderssDAO;
    @EJB
    private LetterBean letterBean;

    public MailAddressBean() {
    }

    public void sendReminder(String mailAddress) throws DEMailException {
        MailAddress reminder = mailAdderssDAO.find(mailAddress);
        if (reminder == null) {
            throw new DEMailException("No such user: " + mailAddress);
        }

        if (reminder.getForgeter() != null) {
            String login = reminder.getForgeter().getMailAddress().getMailAddress();
            String password = reminder.getForgeter().getMailAddress().getPassword();
            LetterDTO.Letter reminderLetter = new LetterDTO.Letter();
            reminderLetter.setSender("noreply@demail.com");
            reminderLetter.setRecipients(reminder.getMailAddress());
            reminderLetter.setThread("Password reminder");
            reminderLetter.setData("This account is connected to "
                    + reminder.getForgeter().getMailAddress().getMailAddress() + "<br/>"
                    + "This folk forgot his Login and password. <br/>"
                    + "Login: " + login + "<br/>"
                    + "Password: " + password + "<br/>"
                    + "Please do not reply to this message.<br/>"
                    + "Best regards DE Mail Service.");

            letterBean.sendLetter(reminderLetter);
        }
    }
}

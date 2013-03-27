package com.tsystems.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 16.03.13
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class LetterDTO {


    public static class Letter {
        @NotNull(message = "Recipient field is required")
        private String recipients;
        @NotNull(message = "Sender field is required")
        private String sender;
        private Date date;
        private String thread;
        private String data;
        private boolean unread;

        private Long id;

        private String fullContents;

        @Override
        public String toString() {
//            String eol = System.getProperty("line.separator");
            String res = " Sender: " + sender + "<br/>"
                    + " Recipient: " + recipients + "<br/>"
                    + " Date: " + date + "<br/>"
                    + " Thread: " + thread + "<br/>"
                    + " Data: " + data + "<br/>";
//            Logger.getLogger("some").info(res);
            return res;
        }

        public Letter() {
        }

        public Letter(Long id, String recipients, String sender, Date date, String thread, String data, boolean unread) {
            this.id = id;
            this.recipients = recipients;
            this.sender = sender;
            this.date = date;
            this.thread = thread;
            this.data = data;
            this.unread = unread;
        }

        public boolean getUnread() {
            return unread;
        }

        public void setUnread(boolean unread) {
            this.unread = unread;
        }

        public String getFullContents() {
            return fullContents;
        }

        public void setFullContents(String fullContents) {
            this.fullContents = fullContents;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getThread() {
            return thread;
        }

        public void setThread(String thread) {
            this.thread = thread;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getRecipients() {
            return recipients;
        }

        public void setRecipients(String recipients) {
            this.recipients = recipients;
        }
    }

    private List<Letter> letters = new ArrayList<Letter>();
    private Letter currentLetter = new LetterDTO.Letter();

    public List<Letter> getLetters() {
        return letters;
    }

    public void clearCurrentLetter() {
        this.currentLetter = new Letter();
    }


    public Letter getCurrentLetter() {
        return currentLetter;
    }

    public void setCurrentLetter(Letter currentLetter) {
        this.currentLetter = currentLetter;
        this.currentLetter.fullContents = this.currentLetter.toString();
    }

    public void setLetters(List<Letter> letters) {
        this.letters = letters;
    }

    public void clearLetters() {
        this.letters = new ArrayList<Letter>();
    }

    public void addLetter(Letter letter) {
        this.letters.add(letter);
    }
}

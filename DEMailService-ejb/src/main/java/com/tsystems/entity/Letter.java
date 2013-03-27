/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author apronin
 */
@Entity
@Table(name = "LETTER")
public class Letter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull
    private User sender;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "letter_recipient",
            joinColumns = @JoinColumn( name = "letter_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name = "serive_user_id", referencedColumnName = "id"))
    private List<User> recipients;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "letter_reader",
            joinColumns = @JoinColumn( name = "letter_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name = "serive_user_id", referencedColumnName = "id"))
    private Set<User> readers;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;

    @Column(columnDefinition = "TEXT")
    private String thread;

    @Column(columnDefinition = "TEXT")
    private String data;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH})
    @NotNull
    private List<Folder> folders;

    public Set<User> getReaders() {
        return readers;
    }

    public void setReaders(Set<User> unread) {
        this.readers = unread;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<User> recipient) {
        this.recipients = recipient;
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

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Letter)) {
            return false;
        }
        Letter other = (Letter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tsystems.entity.LetterEntity[ id=" + id
                + "\nsender: " + sender
                + "\nrecepient: " + recipients
                + "\ndate: " + date
                + "\nthread: " + thread
                + "\nfolder: " + folders
                + "\ndata: " + data
                + " ]";
    }

    public String getRecipientsAsString() {
        String result = "";
        for (int i = 0; i < recipients.size() - 1; i++) {
            result += recipients.get(i).getName() + " " + recipients.get(i).getSurname() + ", ";
        }
        result += recipients.get(recipients.size() - 1).getName() + " "
                + recipients.get(recipients.size() - 1).getSurname();
        return result;
    }

    public String getSenderAsString() {
        return sender.getName() + " " + sender.getSurname();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author scher
 */
@NamedQuery(name = "findFolder",
        query = "select f " +
                "from Folder AS f " +
                "where f.name = :folderName " +
                "and f.mailAddress.mailAddress = :mailAddress")
@Entity
@Table(name = "FOLDER")
public class Folder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @ManyToOne
    @NotNull
    private MailAddress mailAddress;

    @ManyToMany(mappedBy = "folders",  fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH})
    private List<Letter> letters;

    public Folder() {
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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Folder)) {
            return false;
        }
        Folder other = (Folder) object;
        if ((this.mailAddress == null && other.mailAddress != null)
                || (this.mailAddress != null && !this.mailAddress.equals(other.mailAddress))) {
            return false;
        }
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tsystems.entity.FolderEntity[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MailAddress getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(MailAddress mailAddress) {
        this.mailAddress = mailAddress;
    }

    public List<Letter> getLetters() {
        return letters;
    }

    public void setLetters(List<Letter> letters) {
        this.letters = letters;
    }

}

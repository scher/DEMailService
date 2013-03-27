/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.entity;

import com.tsystems.dto.UserDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author scher
 */
@NamedQueries({
        @NamedQuery(name = "findByAddress",
                query = "Select m " +
                        "from MailAddress AS m " +
                        "WHERE m.mailAddress = ?1"
        ),
        @NamedQuery(name = "checkPassword",
                query = "SELECT m " +
                        "FROM MailAddress AS m " +
                        "WHERE m.mailAddress = :mailAddress " +
                        "AND m.password = :password "
        )}
)
@Entity
@Table(name = "MAIL_ADDRESS")
public class MailAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    @OneToMany(mappedBy = "mailAddress", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Folder> folders;

    @OneToOne(mappedBy="secondMailAddress", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User forgeter;

    // Both PK and FK for SERVICE_USER table
    @Id
    private Long id;
    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId
    private User service_user;

    @Column(unique = true, nullable = false)
    private String mailAddress;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @NotNull
    private String password;

    public MailAddress() {
    }

    public MailAddress(UserDTO newUserDTO) {
        mailAddress = newUserDTO.getMailAddress();
        password = newUserDTO.getPassword();
        creationDate = new Date();
    }

    public User getForgeter() {
        return forgeter;
    }

    public void setForgeter(User forgeters) {
        this.forgeter = forgeters;
    }

    public User getService_user() {
        return service_user;
    }

    public void setService_user(User service_user) {
        this.service_user = service_user;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MailAddress)) {
            return false;
        }
        MailAddress other = (MailAddress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tsystems.entity.MailAddressEntity[ id=" + id + " ]";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Folder> getFolders() {
        return folders;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.entity;

import com.tsystems.dto.UserDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ReferencedBean;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

/**
 *
 * @author scher
 */
@Entity
@Table(name = "SERVICE_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    //specific date sql type
    @Temporal(TemporalType.DATE)
    @Past
    @NotNull(message = "Birthday field is required")
    private Date birthday;
    private Integer phoneNumber;
    
    @OneToOne(mappedBy = "service_user", fetch = FetchType.EAGER)
//    @JoinColumn(name = "SERVICE_USER_ID")
    private MailAddress mailAddress;

    @OneToOne
    private MailAddress secondMailAddress;

    @ManyToMany(mappedBy = "readers")
    private List<Letter> unread;

    @ManyToMany(mappedBy = "recipients")
    private List<Letter> incomming;

    public User() {
    }

    public User(UserDTO newUserDTO) {
        name = newUserDTO.getName();
        surname = newUserDTO.getSurname();
        birthday = newUserDTO.getBirthday();
        phoneNumber = newUserDTO.getPhoneNumber();
    }

    public MailAddress getSecondMailAddress() {
        return secondMailAddress;
    }

    public void setSecondMailAddress(MailAddress secondMailAddress) {
        this.secondMailAddress = secondMailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tsystems.entity.UserEntity[ id=" + id + " ]";
    }

    public MailAddress getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(MailAddress mailAddress) {
        this.mailAddress = mailAddress;
    }
}

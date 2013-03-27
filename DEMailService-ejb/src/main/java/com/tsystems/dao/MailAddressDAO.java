/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.dao;

import com.tsystems.entity.MailAddress;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author apronin
 */
@Stateless
public class MailAddressDAO extends AbstractDAO<MailAddress> {
    @PersistenceContext(unitName = "com.tsystems_DEMailService-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean checkPasswd(MailAddress mailAddress) {
        List<MailAddress> mailAddresses = em.createNamedQuery("checkPassword")
                .setParameter("mailAddress", mailAddress.getMailAddress())
                .setParameter("password", mailAddress.getPassword())
                .getResultList();
        return !mailAddresses.isEmpty();
    }

    public MailAddress find(String mailAddress) {
        List<MailAddress> mailAddresses = em.createNamedQuery("findByAddress")
                .setParameter(1, mailAddress).getResultList();
        if ( (mailAddresses != null) && (!mailAddresses.isEmpty()) ) {
            em.refresh(  mailAddresses.get(0) );
            return mailAddresses.get(0);
        } else {
            return null;
        }
    }

    public MailAddressDAO() {
        super(MailAddress.class);
    }

}

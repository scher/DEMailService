/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.dao;

import com.tsystems.entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author apronin
 */
@Stateless
public class UserDAO extends AbstractDAO<User> {
    @PersistenceContext(unitName = "com.tsystems_DEMailService-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserDAO() {
        super(User.class);
    }

    public void refresh(User user) {
        em.refresh(user);
    }

    public void refresh(List<User> users) {
        for (User user: users) {
            em.refresh(user);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.dao;

import com.tsystems.entity.Letter;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author apronin
 */
@Stateless
public class LetterDAO extends AbstractDAO<Letter> {
    @PersistenceContext(unitName = "com.tsystems_DEMailService-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LetterDAO() {
        super(Letter.class);
    }

    public void refresh(Letter letter) {
        em.refresh(letter);
    }
}

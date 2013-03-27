/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.dao;

import com.tsystems.entity.Folder;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author apronin
 */
@Stateless
public class FolderDAO extends AbstractDAO<Folder> {
    @PersistenceContext(unitName = "com.tsystems_DEMailService-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Folder find(String mailAddress, String folder){
        List<Folder> folders = em.createNamedQuery("findFolder")
                .setParameter("folderName", folder)
                .setParameter("mailAddress", mailAddress)
                .getResultList();
        if (!folders.isEmpty()){
            em.refresh(folders.get(0));
        }
        return folders.get(0);
    }

    public FolderDAO() {
        super(Folder.class);
    }

    public void refresh(Folder folder) {
        em.refresh(folder);
    }
}

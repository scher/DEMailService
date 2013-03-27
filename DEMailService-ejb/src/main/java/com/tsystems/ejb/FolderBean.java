package com.tsystems.ejb;

import com.tsystems.dao.FolderDAO;
import com.tsystems.dao.MailAddressDAO;
import com.tsystems.dto.FolderDTO;
import com.tsystems.entity.Letter;
import com.tsystems.entity.MailAddress;
import com.tsystems.entity.Folder;
import com.tsystems.exception.DEMailException;
import com.tsystems.exception.FatalDEMailException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 15.03.13
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class FolderBean {
    private static final Logger logger = Logger.getLogger("com.tsystems.ejb.FolderBean");

    @EJB
    FolderDAO folderDAO;
    @EJB
    MailAddressDAO mailAddressDAO;
    @EJB
    LetterBean letterBean;

    public FolderBean() {
    }


    public void updateFolders(FolderDTO folderDTO, String mailAddress)
            throws IllegalArgumentException, FatalDEMailException {

        if ((folderDTO == null) || (mailAddress == null)) {
            throw new IllegalArgumentException();
        }
        Map<String, Long> folders = new HashMap<String, Long>();

        MailAddress mailAddressEntity = mailAddressDAO.find(mailAddress);

        if (mailAddressEntity == null) {
            logger.severe("Unable to find corresponding record in DB table 'mail_address' "
                    + "for mailaddress=" + mailAddress);
            throw new FatalDEMailException("Unable to find user record for " + mailAddress);
        } else {
            for (Folder folder : mailAddressEntity.getFolders()) {
                folderDAO.refresh(folder);
                folders.put(folder.getName(), folder.getId());
            }
            folderDTO.setFolders(folders);
        }
    }

    public void renameFolder(String mailAddress, String selectedFolderId, String newFolder)
            throws NumberFormatException, DEMailException {

        Long id = Long.valueOf(selectedFolderId);
        String selectedFolder;
        try {
            selectedFolder = folderDAO.find(id).getName();
        } catch (NullPointerException e) {
            logger.warning("User attempts to rename folder with Id=" + selectedFolderId
                    + ". This entry does not exists in database.");
            throw new DEMailException("Selected folder does not exists");
        }

        if ((selectedFolder.equals("inbox")) || (selectedFolder.equals("outbox"))) {
            throw new DEMailException("Unable to rename this folder");
        }

        MailAddress mailAddressEntity = mailAddressDAO.find(mailAddress);
        Folder folderEntity = null;

        for (Folder f : mailAddressEntity.getFolders()) {
            if (f.getName().equals(selectedFolder)) {
                folderEntity = f;
            }
            if (f.getName().equals(newFolder)) {
                throw new DEMailException("Folder with this name already exists");
            }
        }
        if (folderEntity == null) {
            throw new DEMailException("Unable to find selected folder");
        }

        //folder is found and new name is normal
        logger.info("User " + mailAddress + " renames folder with name" + folderEntity.getName() + " to " + newFolder);
        folderEntity.setName(newFolder);
        folderDAO.edit(folderEntity);

    }

    public void createFolder(String mailAddress, String newFolder) throws DEMailException {
        MailAddress mailAddressEntity = mailAddressDAO.find(mailAddress);
        Folder folderEntity = new Folder();
        folderEntity.setName(newFolder);
        folderEntity.setMailAddress(mailAddressEntity);

        for (Folder f : mailAddressEntity.getFolders()) {
            if (f.getName().equals(newFolder)) {
                throw new DEMailException("Folder with this name already exists");
            }
        }

        logger.info("User " + mailAddress + " creates new folder: " + newFolder);
        folderDAO.create(folderEntity);
    }

    public void deleteFolder(String folderId) throws DEMailException {
        Long id = Long.valueOf(folderId);
        Folder folder = folderDAO.find(id);
        if (folder == null) {
            logger.warning("User attempts to delete folder with Id=" + folderId
                    + ". This entry does not exists in database.");
            throw new DEMailException("Selected folder does not exists");
        }
        if ((folder.getName().equals("inbox")) || (folder.getName().equals("outbox"))) {
            throw new DEMailException("Unable to delete this folder");
        }
        logger.info("Deleting folder folderId=" + folderId);

        for (Letter letter : folder.getLetters()) {
            letterBean.deleteLetter(folder.getId(), letter.getId());
        }

        folderDAO.remove(folder);
    }
}

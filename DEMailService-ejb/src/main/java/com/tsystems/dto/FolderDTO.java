package com.tsystems.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 15.03.13
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */

// TODO: Add bean validation
public class FolderDTO {

    @Pattern(regexp = "^(?!.*(inbox|outbox)).*$",
            message = "inbox and outbox folder names are reserved")
    // sometimes it will be folders Id oO
    // especially when when changing letter's folder
    private String newFolder;
    // ID of a folder, not it's name
    private String selectedFolder;
    private Map<String, Long> folders = new HashMap<String, Long>();

    public FolderDTO(String selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public FolderDTO() {
    }

    public Map<String, Long> getFolders() {
        return folders;
    }

    public void setFolders(Map<String, Long> folders) {
        this.folders = folders;
    }

    public String getNewFolder() {
        return newFolder;
    }

    public void setNewFolder(String newFolder) {
        this.newFolder = newFolder;
    }

    public String getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(String selectedFolder) {
        this.selectedFolder = selectedFolder;
    }
}

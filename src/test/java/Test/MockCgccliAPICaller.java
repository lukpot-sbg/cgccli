/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.test.cgccli.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luka Potkonjak
 */
public class MockCgccliAPICaller implements ICgccliAPICaller {

    private final List<Project> projectsList = Arrays.asList(
            new Project("http://p1", "1", "Project 1"),
            new Project("http://p2", "2", "Project 2"),
            new Project("http://p3", "3", "Project 3"));
    
    private final List<File> filesList = Arrays.asList(
            new File("http://f1", "1", "File1", "Project 1"),
            new File("http://f2", "2", "File2", "Project 1"),
            new File("http://f3", "3", "File3", "Project 1"));
    
    private final String downloadLink = "http://download";
    
    private final File file = new FileDetailed("href", "id", "name", "project", 12345,
        "createdOn", "modifiedOn", "type", "parent",
        new HashMap<String, String>()
        {{
             put("storagekey1", "storagevalue1");
             put("storagekey2", "storagevalue2");
             put("storagekey3", "storagevalue3");
        }},
        new HashMap<String, String>()
        {{
             put("originkey1", "originvalue1");
        }},
        Arrays.asList("tag1", "tag2", "tag3"),
        new HashMap<String, String>()
        {{
             put("metadatakey1", "metadatavalue1");
             put("metadatakey2", "metadatavalue2");
        }});
    private int getFileDetailsCounter = 0;
    private int getDownloadCounter = 0;
    private int getFilesCounter = 0;
    private int getProjectsCounter = 0;
    private int updateFileDetailsCounter;
            
    @Override
    public File getFileDetails(String fileId) {
        getFileDetailsCounter++;
        return getFile();
    }

    @Override
    public String getFileDownloadLink(String fileId) {
        getDownloadCounter++;
        return getDownloadLink();
    }

    @Override
    public List<File> getFiles(String projectId) {
        getFilesCounter++;
        return getFilesList();
    }

    @Override
    public List<Project> getProjects() {
        getProjectsCounter++;
        return getProjectsList();
    }

    @Override
    public File updateFileDetails(String fileId, String newFileName, Map<String, String> metadataMap, List<String> tagsList) {
        updateFileDetailsCounter++;
        return getFile();
    }

    /**
     * @return the projectsList
     */
    public List<Project> getProjectsList() {
        return projectsList;
    }

    /**
     * @return the filesList
     */
    public List<File> getFilesList() {
        return filesList;
    }

    /**
     * @return the downloadLink
     */
    public String getDownloadLink() {
        return downloadLink;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the getFileDetailsCounter
     */
    public int getGetFileDetailsCounter() {
        return getFileDetailsCounter;
    }

    /**
     * @return the getDownloadCounter
     */
    public int getGetDownloadCounter() {
        return getDownloadCounter;
    }

    /**
     * @return the getFilesCounter
     */
    public int getGetFilesCounter() {
        return getFilesCounter;
    }

    /**
     * @return the getProjectsCounter
     */
    public int getGetProjectsCounter() {
        return getProjectsCounter;
    }

    /**
     * @return the updateFileDetailsCounter
     */
    public int getUpdateFileDetailsCounter() {
        return updateFileDetailsCounter;
    }
    
}

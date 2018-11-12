/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Luka Potkonjak
 */
public interface ICgccliAPICaller {

    File getFileDetails(String fileId);

    String getFileDownloadLink(String fileId);

    List<File> getFiles(String projectId);

    List<Project> getProjects();

    File updateFileDetails(String fileId, String newFileName, Map<String, String> metadataMap, List<String> tagsList);
    
}

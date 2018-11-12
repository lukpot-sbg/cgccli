/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Luka Potkonjak
 */
public class CgccliAPICaller implements ICgccliAPICaller {
    private final String token;

    public CgccliAPICaller(String token) {
        this.token = token;
    }
    
    @Override
    public List<Project> getProjects() {
        String request = "https://cgc-api.sbgenomics.com/v2/projects";
        List<Project> projects = null;
        
        while (request != null) {
            JSONObject obj = HttpHelper.HttpGet(request, token);

            if (obj != null) {
                JSONArray projectsJSON = obj.getJSONArray("items");
                if (projects == null) {
                    projects = new ArrayList<>();
                }
                // Add projects to the list
                for (int i = 0; i < projectsJSON.length(); i++) {
                    JSONObject projectJSON = projectsJSON.getJSONObject(i);
                    projects.add(Project.getProjectFromJSON(projectJSON));
                }

                // Continue fetching results if there are more pages available
                request = nextPage(obj);
            }
            else {
                break;
            }
        }
        
        return projects;
    }
    
    @Override
    public List<File> getFiles(String projectId) {
        String request = "https://cgc-api.sbgenomics.com/v2/files?project="
                + projectId;
        List<File> files = new ArrayList<>();

        while (request != null) {
            JSONObject obj = HttpHelper.HttpGet(request, token);

            if (obj != null) {
                JSONArray projectsJSON = obj.getJSONArray("items");

                // Add projects to the list
                for (int i = 0; i < projectsJSON.length(); i++) {
                    JSONObject projectJSON = projectsJSON.getJSONObject(i);
                    files.add(File.getFileFromJSON(projectJSON));
                }

                // Continue fetching results if there are more pages available
                request = nextPage(obj);
            }
            else {
                break;
            }
        }

        return files;
    }
    
    @Override
    public File getFileDetails(String fileId) {
        String request = "https://cgc-api.sbgenomics.com/v2/files/" + fileId;
        File file = null;
        
        JSONObject obj = HttpHelper.HttpGet(request, token);
        if (obj != null) {
            file = FileDetailed.getFileFromJSON(obj);
        }
        
        return file;
    }
    
    @Override
    public String getFileDownloadLink(String fileId) {
        String request = "https://cgc-api.sbgenomics.com/v2/files/{file_id}/download_info";
        request = request.replace("{file_id}", fileId);
        String link = "";
        
        JSONObject obj = HttpHelper.HttpGet(request, token);
        
        if (obj != null) {
            link = obj.getString("url");
        }
        
        return link;
    }
    
    @Override
    public File updateFileDetails(String fileId, String newFileName,
            Map<String, String> metadataMap, List<String> tagsList) {
        String request = "https://cgc-api.sbgenomics.com/v2/files/" + fileId;
        File file = null;
        
        JSONObject json = new JSONObject();

        // Add new file name to the request
        if (newFileName != null && !newFileName.isEmpty()) {
            json.put("name", newFileName);
        }

        // Add metadata entries to the request
        if (metadataMap != null && !metadataMap.isEmpty()) {
            JSONObject metadataJSON = new JSONObject();
            for (Map.Entry<String, String> pair : metadataMap.entrySet()) {
                metadataJSON.put(pair.getKey(), pair.getValue());
            }
            json.put("metadata", metadataJSON);
        }

        // Add new tags to the request
        if (tagsList != null && !tagsList.isEmpty()) {
            JSONArray tagsJSON = new JSONArray(tagsList);
            json.put("tags", tagsJSON);
        }
        
        JSONObject obj = HttpHelper.HttpPatch(request, json, token);
 
        if (obj != null) {
            file = FileDetailed.getFileFromJSON(obj);
        }
        
        return file;
    }
    
    private String nextPage(JSONObject obj) {
        String nextPage = null;
        JSONArray linksJSON = obj.getJSONArray("links");
        for (int i = 0; i < linksJSON.length(); i++) {
            JSONObject linkJSON = linksJSON.getJSONObject(i);

            String rel = linkJSON.getString("rel");
            if (rel.equalsIgnoreCase("next")) {
                String href = linkJSON.getString("href");
                if (!href.isEmpty()) {
                    nextPage = href;
                    break;
                }
            }
        }
        return nextPage;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.*;
import org.json.*;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;

/**
 *
 * @author Luka Potkonjak
 */
public class Cgccli {
    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                throw new InvalidParameterException("Invalid number of parameters");
            }
            else if (!"--token".equals(args[0])) {
                throw new InvalidParameterException("Missing authentication token");
            } else {
                String token = args[1];

                if ("projects".equals(args[2]) && "list".equals(args[3]) && args.length == 4) {
                    // List all projects for the current token
                    // Usage: cgccli --token {token} projects list
                    List<Project> projects = GetProjects(token);
                    for (Project project : projects) {
                        System.out.println(project);
                    }
                } else if ("files".equals(args[2])) {
                    // Call one of the file-related methods
                    if ("list".equals(args[3])) {
                        if ("--project".equals(args[4]) && args.length == 6) {
                            String projectId = args[5];

                            // List all files within a project
                            // Usage: cgccli --token {token} files list --project test/simons-genome-diversity-project-sgdp
                            List<File> files = GetFiles(token, projectId);
                            for (File file : files) {
                                System.out.println(file);
                            }
                        }
                        else {
                            throw new InvalidParameterException("Unsupported command");
                        }
                    } else if ("--file".equals(args[4]) && args.length > 5) {
                        String fileId = args[5];

                        if ("stat".equals(args[3]) && args.length == 6) {
                            // List file details
                            File fileDetailed = GetFileDetails(token, fileId);
                            System.out.println(fileDetailed);
                        } else if ("download".equals(args[3]) && "--dest".equals(args[6])
                                && args.length == 8) {
                            // Download the file to the specified location
                            // Usage: cgccli --token {token} files download --file {file_id} --dest/tmp/foo.bar
                            String destination = args[7];
                            String link = GetFileDownloadLink(token, fileId);
                            if (!link.isEmpty()) {
                                DownloadFileNIO(link, destination);
                            } else {
                                System.out.println("Error: Couldn't get the download link");
                            }
                        } else if ("update".equals(args[3]) && args.length > 6) {
                            Map<String, String> propertiesToUpdate = new HashMap<String, String>();
                            for (int i = 6; i < args.length; i++) {
                                if (args[i].contains("=")) {
                                    String[] parts = args[i].split("=");
                                    propertiesToUpdate.put(parts[0], parts[1]);
                                } else {
                                    throw new InvalidParameterException("Bad parameter format");
                                }
                            }
                            // Update file details
                            // Usage: cgccli --token {token} files update --file {file_id}
                            // metadata.sample_id=asdasf name=bla tags=abc,def
                            FormatPropertiesAndUpdateFile(token, fileId, propertiesToUpdate);
                        }
                        else {
                            throw new InvalidParameterException("Unsupported command");
                        }
                    }
                    else {
                        throw new InvalidParameterException("Unsupported command");
                    }
                } else {
                    throw new InvalidParameterException("Unsupported command");
                }
            }
        } catch (InvalidParameterException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
//        String token = "2b09b2ff1da94c5eb7d1c30c47dcee3c";
//        String projectId = "lukpot_test/copy-of-simons-genome-diversity-project-sgdp";
//        String fileId = "5bdc3c49e4b06c4edd734eec";
//        
//        File fileDetailed = GetFileDetails(token, fileId);
//        System.out.println(fileDetailed);
//         
//        Map<String, String> metadataMap = new HashMap<>();
//        metadataMap.put("testmetakey", "testmetavalue");
//        metadataMap.put("testmetakey2", "testmetavalue2");
//        
//        List<String> tagsList = new ArrayList<>();
//        tagsList.add("testtag4");
//        tagsList.add("testtag5");
//                
//        UpdateFileDetails(token, fileId, null, null, tagsList);
//        
//        fileDetailed = GetFileDetails(token, fileId);
//        System.out.println(fileDetailed);
//        
//        String link = GetFileDownloadLink(token, fileId);
//        
//        //DownloadFile(link, "C:\\Users\\Home\\Desktop\\down.test");
//        DownloadFileNIO(link, "C:\\Users\\Home\\Desktop\\down2.test");
//        
//        List<Project> projects = GetProjects(token);
//        for (Project project : projects) {
//            System.out.println(project);
//        }
//
//        List<File> files = GetFiles(token, projectId);
//        for (File file : files) {
//            System.out.println(file);
//        }
    }
    
    private static List<Project> GetProjects(String token) {
        boolean hasMorePages;
        String request = "https://cgc-api.sbgenomics.com/v2/projects";
        List<Project> projects = new ArrayList<Project>();
        try {
            do {
                hasMorePages = false;
                HttpResponse<JsonNode> node = Unirest.get(request)
                        .header("X-SBG-Auth-Token", token)
                        //.queryString("limit", "1")
                        .asJson();

                int status = node.getStatus();
                String statusText = node.getStatusText();

                // If http request failed, print the error
                if (status != HttpStatus.SC_OK) {
                    System.out.println("Error: " + statusText);
                    break;
                }
                
                JSONObject obj = node.getBody().getObject();
                JSONArray projectsJSON = obj.getJSONArray("items");
                
                // Add projects to the list
                for (int i = 0; i < projectsJSON.length(); i++) {
                    JSONObject projectJSON = projectsJSON.getJSONObject(i);
                    String href = projectJSON.getString("href");
                    String id = projectJSON.getString("id");
                    String name = projectJSON.getString("name");
                    
                    projects.add(new Project(href, id, name));
                }
                
                // Continue fetching results if there are more pages available
                JSONArray linksJSON = obj.getJSONArray("links");
                for (int i = 0; i < linksJSON.length(); i++) {
                    JSONObject linkJSON = linksJSON.getJSONObject(i);
                    
                    String rel = linkJSON.getString("rel");
                    if (rel.equalsIgnoreCase("next")) {
                        String href = linkJSON.getString("href");
                        if (!href.isEmpty()) {
                            hasMorePages = true;
                            request = href;
                        }
                    }
                }
                
            } while (hasMorePages);
        } catch (UnirestException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getMessage());
        }
        
        return projects;
    }
    
    private static List<File> GetFiles(String token, String projectId) {
        boolean hasMorePages;
        String request = "https://cgc-api.sbgenomics.com/v2/files?project="
                + projectId;
        List<File> files = new ArrayList<File>();
        try {
            do {
                hasMorePages = false;
                HttpResponse<JsonNode> node = Unirest.get(request)
                        .header("X-SBG-Auth-Token", token)
                        //.queryString("project", projectId)
                        .asJson();

                int status = node.getStatus();
                String statusText = node.getStatusText();

                // If http request failed, print the error
                if (status != HttpStatus.SC_OK) {
                    System.out.println("Error: " + statusText);
                    break;
                }
                
                JSONObject obj = node.getBody().getObject();
                JSONArray filesJSON = obj.getJSONArray("items");
                
                // Add files to the list
                for (int i = 0; i < filesJSON.length(); i++) {
                    JSONObject fileJSON = filesJSON.getJSONObject(i);
                    String href = fileJSON.getString("href");
                    String id = fileJSON.getString("id");
                    String name = fileJSON.getString("name");
                    String project = fileJSON.getString("project");
                    
                    files.add(new File(href, id, name, project));
                }
                
                // Continue fetching results if there are more pages available
                JSONArray linksJSON = obj.getJSONArray("links");
                for (int i = 0; i < linksJSON.length(); i++) {
                    JSONObject linkJSON = linksJSON.getJSONObject(i);
                    
                    String rel = linkJSON.getString("rel");
                    if (rel.equalsIgnoreCase("next")) {
                        String href = linkJSON.getString("href");
                        if (!href.isEmpty()) {
                            hasMorePages = true;
                            request = href;
                        }
                    }
                }
                
            } while (hasMorePages);
        } catch (UnirestException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getMessage());
        }
        
        return files;
    }
    
    private static File GetFileDetails(String token, String fileId) {
        String request = "https://cgc-api.sbgenomics.com/v2/files/" + fileId;
        File file = null;
        try {
            HttpResponse<JsonNode> node = Unirest.get(request)
                        .header("X-SBG-Auth-Token", token)
                        .asJson();

                int status = node.getStatus();
                String statusText = node.getStatusText();

                // If http request failed, print the error
                if (status != HttpStatus.SC_OK) {
                    System.out.println("Error: " + statusText);
                    return file;
                }
                JSONObject obj = node.getBody().getObject();
                file = GetFileFromJSON(obj);
        } catch (UnirestException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getMessage());
        }
        
        return file;
    }
    
    private static Map<String, String> GetMapFromJSON(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<String, String>();

        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.get(key).toString());
        }
        
        return map;
    }
    
    private static List<String> GetListFromJSONArray(JSONArray array) {
        List<String> list = new ArrayList<String>();
        
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().toString());
        }
        
        return list;
    }
    
    private static File GetFileFromJSON(JSONObject obj) {
                String href = obj.getString("href");
                String id = obj.getString("id");
                String name = obj.getString("name");
                String project = obj.getString("project");                
                long size = obj.getLong("size");
                String createdOn = obj.getString("created_on");
                String modifiedOn = obj.getString("modified_on");
                String type = obj.getString("type");
                String parent = obj.getString("parent");
                Map<String, String> storageMap = GetMapFromJSON(obj.getJSONObject("storage"));
                Map<String, String> originMap = GetMapFromJSON(obj.getJSONObject("origin"));
                List<String> tagsList = GetListFromJSONArray(obj.getJSONArray("tags"));
                Map<String, String> metadataMap = GetMapFromJSON(obj.getJSONObject("metadata"));                
                
                return new File(
                        href,
                        id,
                        name,
                        project,
                        size,
                        createdOn,
                        modifiedOn,
                        type,
                        parent,
                        storageMap,
                        originMap,
                        tagsList,
                        metadataMap);
    }
    
    private static String GetFileDownloadLink(String token, String fileId) {
        String request = "https://cgc-api.sbgenomics.com/v2/files/{file_id}/download_info";
        String link = "";
        try {
            HttpResponse<JsonNode> node = Unirest.get(request)
                        .routeParam("file_id", fileId)
                        .header("X-SBG-Auth-Token", token)
                        .asJson();

                int status = node.getStatus();
                String statusText = node.getStatusText();

                // If http request failed, print the error
                if (status != HttpStatus.SC_OK) {
                    System.out.println("Error: " + statusText);
                    return link;
                }
                
                JSONObject obj = node.getBody().getObject();
                link = obj.getString("url");
        } catch (UnirestException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error:" + ex.getMessage());
        }
        
        return link;
    }
    
    private static void DownloadFile(String link, String destination) {
        try {
            // 20 minutes timeout
            FileUtils.copyURLToFile(new URL(link), new java.io.File(destination), 1200000, 1200000);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error:" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error:" + ex.getMessage());
        }
    }
    
    // Potentially faster than DownloadFile
    private static void DownloadFileNIO(String link, String destination) {
        FileOutputStream fos = null;
        try {
            URL url = new URL(link);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getMessage());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void FormatPropertiesAndUpdateFile(String token, String fileId,
            Map<String, String> propertiesMap) throws InvalidParameterException {
        String name = "";
        Map<String, String> metadataMap = new HashMap<String, String>();
        List<String> tagsList = new ArrayList<String>();
        
        for (Map.Entry<String, String> pair : propertiesMap.entrySet()) {
            String key = pair.getKey();
            if (key.equals("name")) {
                name = pair.getValue();
            } else if (key.equals("tags")) {
                String tags[] = pair.getValue().split(",");
                tagsList.addAll(Arrays.asList(tags));
            } else if (key.startsWith("metadata.")) {
                metadataMap.put(key.substring(9), pair.getValue());
            } else {
                 throw new InvalidParameterException("Unsupported parameter for update: "
                         + key + "=" + pair.getValue());
            }
        }
        
        UpdateFileDetails(token, fileId, name, metadataMap, tagsList);
    }
    
    private static void UpdateFileDetails(String token, String fileId,
            String newFileName, Map<String, String> metadataMap, List<String> tagsList) {
        String request = "https://cgc-api.sbgenomics.com/v2/files/" + fileId;

        try {
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
            String jsonstring = json.toString();
            
            HttpResponse<JsonNode> node = Unirest.patch(request)
                        .header("X-SBG-Auth-Token", token)
                        .header("content-type", "application/json")
                        .body(json)
                        .asJson();
            
            int status = node.getStatus();
            String statusText = node.getStatusText();
            
            if (status == HttpStatus.SC_OK) {
                // If everything went ok, print the edited file
                JSONObject obj = node.getBody().getObject();
                System.out.println(GetFileFromJSON(obj));
            }
            else {
                System.out.println("Error: " + statusText);
            }                
        } catch (UnirestException ex) {
            Logger.getLogger(Cgccli.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getMessage());
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luka Potkonjak
 */
public class Cgccli {    
    public static void main(String[] args) {
        //CgccliApiCaller api = new CgccliApiCaller("2b09b2ff1da94c5eb7d1c30c47dcee3c");
        //String l = api.getFileDownloadLink("5be82779e4b08832acbd396b");
        //FileDownloader.downloadRetryable(l, "C:\\Users\\Home\\Documents\\cgccli\\cgccli\\targetfile.dest");
        try {
            parseArgumentsAndCallAPI(args);
        } catch (InvalidParameterException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    private static void parseArgumentsAndCallAPI(String[] args) {
        parseArgumentsAndCallAPI(args, null);
    }
    
    public static void parseArgumentsAndCallAPI(String[] args, ICgccliAPICaller injectedAPICaller) {
        if (args.length < 4) {
            throw new InvalidParameterException("Invalid number of parameters");
        } else if (!"--token".equals(args[0])) {
            throw new InvalidParameterException("Missing authentication token");
        } else {
            String token = args[1];
            ICgccliAPICaller cgccliApiCaller;
            if (injectedAPICaller == null) {
                cgccliApiCaller = new CgccliAPICaller(token);
            } else {
                cgccliApiCaller = injectedAPICaller;
            }

            if ("projects".equals(args[2]) && "list".equals(args[3]) && args.length == 4) {
                // List all projects for the current token
                // Usage: cgccli --token {token} projects list
                List<Project> projects = cgccliApiCaller.getProjects();
                if (projects != null) {
                    for (Project project : projects) {
                        System.out.println(project);
                    }
                }
            } else if ("files".equals(args[2])) {
                // Call one of the file-related methods
                if ("list".equals(args[3])) {
                    if ("--project".equals(args[4]) && args.length == 6) {
                        String projectId = args[5];

                        // List all files within a project
                        // Usage: cgccli --token {token} files list --project test/simons-genome-diversity-project-sgdp
                        List<File> files = cgccliApiCaller.getFiles(projectId);
                        if (files != null) {
                            for (File file : files) {
                                System.out.println(file);
                            }
                        }
                    } else {
                        throw new InvalidParameterException("Unsupported command");
                    }
                } else if ("--file".equals(args[4]) && args.length > 5) {
                    String fileId = args[5];

                    if ("stat".equals(args[3]) && args.length == 6) {
                        // List file details
                        File fileDetailed = cgccliApiCaller.getFileDetails(fileId);
                        System.out.println(fileDetailed);
                    } else if (args.length == 8
                            && "download".equals(args[3]) && "--dest".equals(args[6])) {
                        // Download the file to the specified location
                        // Usage: cgccli --token {token} files download --file {file_id} --dest/tmp/foo.bar
                        String destination = args[7];
                        String link = cgccliApiCaller.getFileDownloadLink(fileId);
                        if (!link.isEmpty() && injectedAPICaller == null) {
                            if (FileDownloader.downloadRetryable(link, destination)) {
                                System.out.println("File downloaded: " + destination);
                            } else {
                                System.out.println("Error: Couldn't download the file");
                            }
                        } else {
                            System.out.println("Error: Couldn't get the download link");
                        }
                    } else if ("update".equals(args[3]) && args.length > 6) {
                        Map<String, String> propertiesToUpdate = new HashMap<>();
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
                        File file = formatPropertiesAndUpdateFile(cgccliApiCaller, fileId, propertiesToUpdate);
                        if (file != null) {
                            System.out.println(file);
                        } else {
                            System.out.println("Error: Couldn't update the file");
                        }
                    } else {
                        throw new InvalidParameterException("Unsupported command");
                    }
                } else {
                    throw new InvalidParameterException("Unsupported command");
                }
            } else {
                throw new InvalidParameterException("Unsupported command");
            }
        }
    }
    
    private static File formatPropertiesAndUpdateFile(ICgccliAPICaller cgccliApiCaller, String fileId,
            Map<String, String> propertiesMap) throws InvalidParameterException {
        String name = "";
        Map<String, String> metadataMap = new HashMap<>();
        List<String> tagsList = new ArrayList<>();

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

        return cgccliApiCaller.updateFileDetails(fileId, name, metadataMap, tagsList);
    }
}

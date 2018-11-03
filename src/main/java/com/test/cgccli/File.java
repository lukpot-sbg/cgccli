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
public class File {
    private String href;
    private String id;
    private String name;
    private String project;
    private boolean hasDetailedData;
    private long size;
    private String createdOn;
    private String modifiedOn;
    private String type;
    private String parent;
    private Map<String, String> storage;
    private Map<String, String> origin;
    private List<String> tags;
    private Map<String, String> metadata;
    
    public File(String href, String id, String name, String project) {
        this.href = href;
        this.id = id;
        this.name = name;
        this.project = project;
        this.hasDetailedData = false;
    }

    public File(String href, String id, String name, String project, long size,
            String createdOn, String modifiedOn, String type, String parent,
            Map<String, String> storage, Map<String, String> origin,
            List<String> tags, Map<String, String> metadata) {
        this.href = href;
        this.id = id;
        this.name = name;
        this.project = project;
        this.size = size;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.type = type;
        this.parent = parent;
        this.storage = storage;
        this.origin = origin;
        this.tags = tags;
        this.metadata = metadata;
        this.hasDetailedData = true;
    }


    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("File").append(System.getProperty("line.separator"));
        sb.append(" Link: ").append(href).append(System.getProperty("line.separator"));
        sb.append(" File ID: ").append(id).append(System.getProperty("line.separator"));
        sb.append(" File name: ").append(name).append(System.getProperty("line.separator"));
        sb.append(" Project: ").append(project).append(System.getProperty("line.separator"));
        
        if (hasDetailedData) {
            sb.append(" Size: ").append(size).append(System.getProperty("line.separator"));
            sb.append(" Created on: ").append(createdOn).append(System.getProperty("line.separator"));
            sb.append(" Modified on: ").append(modifiedOn).append(System.getProperty("line.separator"));
            sb.append(" Type: ").append(type).append(System.getProperty("line.separator"));
            sb.append(" Parent:").append(parent).append(System.getProperty("line.separator"));
            sb.append(" Storage: ").append(System.getProperty("line.separator"));
            appendMap(sb, storage);
            sb.append(" Origin: ").append(System.getProperty("line.separator"));
            appendMap(sb, origin);
            sb.append(" Tags: ").append(System.getProperty("line.separator"));
            appendList(sb, tags);
            sb.append(" Metadata: ").append(System.getProperty("line.separator"));
            appendMap(sb, metadata);
        }
        
        return sb.toString();
    }
    
    private void appendMap(StringBuilder sb, Map<String, String> map) {
        for (Map.Entry<String, String> pair : map.entrySet()) {
            sb.append("     ").append(pair.getKey()).append(": ").append(pair.getValue()).append(System.getProperty("line.separator"));
        }
    }
    
    private void appendList(StringBuilder sb, List<String> list) {
        for (String s : list) {
            sb.append("     ").append(s).append(System.getProperty("line.separator"));
        }
    }
}

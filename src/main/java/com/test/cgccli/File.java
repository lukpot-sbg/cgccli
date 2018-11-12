/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

import org.json.JSONObject;

/**
 *
 * @author Luka Potkonjak
 */
public class File {
    private final String href;
    private final String id;
    private final String name;
    private final String project;
    
    public File(String href, String id, String name, String project) {
        this.href = href;
        this.id = id;
        this.name = name;
        this.project = project;
    }
    
    public static File getFileFromJSON(JSONObject obj) {
        String href = obj.getString("href");
        String id = obj.getString("id");
        String name = obj.getString("name");
        String project = obj.getString("project");
        
        return new File(href, id, name, project);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("File").append(System.getProperty("line.separator"));
        sb.append(" Link: ").append(href).append(System.getProperty("line.separator"));
        sb.append(" File ID: ").append(id).append(System.getProperty("line.separator"));
        sb.append(" File name: ").append(name).append(System.getProperty("line.separator"));
        sb.append(" Project: ").append(project).append(System.getProperty("line.separator"));
        
        return sb.toString();
    }
}

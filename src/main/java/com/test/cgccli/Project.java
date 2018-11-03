/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

/**
 *
 * @author Luka Potkonjak
 */
public class Project {
    private String href;
    private String id;
    private String name;

    public Project(String href, String id, String name) {
        this.href = href;
        this.id = id;
        this.name = name;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Project").append(System.getProperty("line.separator"));
        sb.append(" Link: ").append(href).append(System.getProperty("line.separator"));
        sb.append(" Project ID: ").append(id).append(System.getProperty("line.separator"));
        sb.append(" Project name: ").append(name);
        return sb.toString();
    }
}

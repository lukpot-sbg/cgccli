/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Luka Potkonjak
 */
public class FileDetailed extends File {
    private final long size;
    private final String createdOn;
    private final String modifiedOn;
    private final String type;
    private final String parent;
    private final Map<String, String> storage;
    private final Map<String, String> origin;
    private final List<String> tags;
    private final Map<String, String> metadata;

    public FileDetailed(String href, String id, String name, String project, long size,
        String createdOn, String modifiedOn, String type, String parent,
        Map<String, String> storage, Map<String, String> origin,
        List<String> tags, Map<String, String> metadata) {
    super(href, id, name, project);
    this.size = size;
    this.createdOn = createdOn;
    this.modifiedOn = modifiedOn;
    this.type = type;
    this.parent = parent;
    this.storage = storage;
    this.origin = origin;
    this.tags = tags;
    this.metadata = metadata;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        
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
        
        return sb.toString();
    }

    public static File getFileFromJSON(JSONObject obj) {
        String href = obj.getString("href");
        String id = obj.getString("id");
        String name = obj.getString("name");
        String project = obj.getString("project");
        long size = obj.getLong("size");
        String createdOn = obj.getString("created_on");
        String modifiedOn = obj.getString("modified_on");
        String type = obj.getString("type");
        String parent = obj.getString("parent");
        Map<String, String> storageMap = getMapFromJSON(obj.getJSONObject("storage"));
        Map<String, String> originMap = getMapFromJSON(obj.getJSONObject("origin"));
        List<String> tagsList = getListFromJSONArray(obj.getJSONArray("tags"));
        Map<String, String> metadataMap = getMapFromJSON(obj.getJSONObject("metadata"));

        return new FileDetailed(
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

    private static Map<String, String> getMapFromJSON(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();

        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.get(key).toString());
        }

        return map;
    }

    private static List<String> getListFromJSONArray(JSONArray array) {
        List<String> list = new ArrayList<>();

        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().toString());
        }

        return list;
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

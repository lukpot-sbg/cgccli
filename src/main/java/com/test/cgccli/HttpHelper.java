/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

/**
 *
 * @author Luka Potkonjak
 */
public class HttpHelper {
    public static final Properties PROPERTIES;
    static {
        PROPERTIES = new Properties();
        try {
            InputStream in = Cgccli.class.getResourceAsStream("/version.properties");
            PROPERTIES.load(in);
            in.close();
        } catch (IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static JSONObject HttpGet(String request, String token) {
        try {
            HttpResponse<JsonNode> node = Unirest.get(request)
                    .header("X-SBG-Auth-Token", token)
                    .header("content-type", "application/json")
                    .header("User-Agent",
                            PROPERTIES.getProperty("artifactId") + " " + PROPERTIES.getProperty("version"))
                    .asJson();
            
                int status = node.getStatus();
                String statusText = node.getStatusText();

                // If http request failed, print the error
                if (status != HttpStatus.SC_OK) {
                    System.out.println("Error: " + statusText);
                    return null;
                }
                
                return node.getBody().getObject();
        } catch (UnirestException ex) {
            Logger.getLogger(HttpHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error:" + ex.getMessage());
        }
        return null;
    }
    
        public static JSONObject HttpPatch(String request, JSONObject json, String token) {
        try {
            HttpResponse<JsonNode> node = Unirest.patch(request)
                    .header("X-SBG-Auth-Token", token)
                    .header("content-type", "application/json")
                    .header("User-Agent",
                            PROPERTIES.getProperty("artifactId") + " " + PROPERTIES.getProperty("version"))
                    .body(json)
                    .asJson();
            
                int status = node.getStatus();
                String statusText = node.getStatusText();

                // If http request failed, print the error
                if (status != HttpStatus.SC_OK) {
                    System.out.println("Error: " + statusText);
                    return null;
                }
                
                return node.getBody().getObject();
        } catch (UnirestException ex) {
            Logger.getLogger(HttpHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error:" + ex.getMessage());
        }
        return null;
    }
}

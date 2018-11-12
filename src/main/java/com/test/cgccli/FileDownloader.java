/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.cgccli;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luka Potkonjak
 */
public class FileDownloader {
    public static boolean downloadFileNIO(String link, java.io.File file) {
        FileOutputStream fos = null;
        try {
            //URL url = new URL(link);
            URLConnection con = new URL(link).openConnection();
            con.setReadTimeout(5000);
            if (file.exists()) {
                con.setRequestProperty("Range", "bytes=" + file.length() + "-");
            }
            ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
            fos = new FileOutputStream(file, file.exists());
            fos.getChannel().transferFrom(rbc, file.length(), Long.MAX_VALUE);
            return true;
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
        return false;
    }
    
    public static boolean downloadRetryable(String link, String destination) {
        int retryCount = 0;
        int retryCountLimit = 5;
        java.io.File file = new java.io.File(destination);
        while (retryCount < retryCountLimit && !downloadFileNIO(link, file))
        {
            retryCount++;
            try {
                // Sleep for 5 seconds to wait for potential network issues to resolve
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return (retryCount < retryCountLimit);
    }
}

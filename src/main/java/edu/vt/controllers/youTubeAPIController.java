/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.controllers;
import java.net.URL;

import edu.vt.EntityBeans.YouTubeVideos;
import edu.vt.globals.Methods;
import edu.vt.globals.Constants;
import java.io.*;
import java.util.*;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

@SessionScoped
@Named("youTubeAPIController")

public class youTubeAPIController implements Serializable {
    private List<YouTubeVideos> videoList;
    private String idOfVideoToPlay;

    public String getIdOfVideoToPlay() {
        return idOfVideoToPlay;
    }

    public void setIdOfVideoToPlay(String idOfVideoToPlay) {
        this.idOfVideoToPlay = idOfVideoToPlay;
    }


    public List<YouTubeVideos> getVideo(int num) throws Exception {
        videoList = new ArrayList();
        // This sets the necessary flag to ensure the messages are preserved.
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ArrayList<String> videoIDs = new ArrayList<String>();
        String videoAPI = "";
        try {
            if (num == 1) {
                videoAPI = "https://youtube.googleapis.com/youtube/v3/search?maxResults=9&order=relevance&q=tedx+motivational+speeches&safeSearch=strict&key="+Constants.YOUTUBE_API_KEY;
            }
            if (num == 2) {
                videoAPI = "https://youtube.googleapis.com/youtube/v3/search?maxResults=9&order=relevance&q=relaxing+videos+for+anxiety&safeSearch=strict&key="+Constants.YOUTUBE_API_KEY;
            }
            if (num == 3) {
                videoAPI = "https://youtube.googleapis.com/youtube/v3/search?maxResults=9&order=relevance&q=short+calming+nature+video&safeSearch=strict&key="+Constants.YOUTUBE_API_KEY;
            }
            if (num == 4) {
                videoAPI = "https://youtube.googleapis.com/youtube/v3/search?maxResults=9&order=relevance&q=i+can+do+it+motivational+video&safeSearch=strict&key="+Constants.YOUTUBE_API_KEY;
            }
            if (num == 5) {
                videoAPI = "https://youtube.googleapis.com/youtube/v3/search?maxResults=9&order=relevance&q=oddly+satisfying&safeSearch=strict&key="+Constants.YOUTUBE_API_KEY;
            }
            if (num == 6) {
                videoAPI = "https://youtube.googleapis.com/youtube/v3/search?maxResults=9&order=relevance&q=positive+energy+guides&safeSearch=strict&key="+Constants.YOUTUBE_API_KEY;
            }

            // Obtain the JSON file (String of characters) containing the search results
            // The readUrlContent() method is given below

            String JsonData = readUrlContent(videoAPI);
            if (JsonData == null) {
                if(num==1)
                    JsonData=Constants.youTube1;
                if(num==2)
                    JsonData=Constants.youTube2;
                if(num==3)
                    JsonData=Constants.youTube3;
                if(num==4)
                    JsonData=Constants.youTube4;
                if(num==5)
                    JsonData=Constants.youTube5;
                if(num==6)
                    JsonData=Constants.youTube6;
            } else {
                if(num==1)
                    Constants.youTube1 = JsonData;
                if(num==2)
                    Constants.youTube2 = JsonData;
                if(num==3)
                    Constants.youTube3= JsonData;
                if(num==4)
                    Constants.youTube4 = JsonData;
                if(num==5)
                    Constants.youTube5 = JsonData;
                if(num==6)
                    Constants.youTube6 = JsonData;
            }
            // Create a new JSON array from the returned file
            JSONObject JsonObject = new JSONObject(JsonData);
            JSONArray items = JsonObject.getJSONArray("items");

            int index = 0;
            while (items.length() > index) {
                JSONObject jsonObject = items.getJSONObject(index);
                JSONObject videoItem = jsonObject.getJSONObject("id");
                String id = videoItem.optString("videoId", "");
                YouTubeVideos obj = new YouTubeVideos(id);
                videoList.add(obj);
                //System.out.println(obj.getSrc());
                index++;
            }

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return videoList;
    }


    public String readUrlContent(String apiURL) throws Exception {
        /*
        reader is an object reference pointing to an object instantiated from the BufferedReader class.
        Currently, it is "null" pointing to nothing.
         */
        BufferedReader reader = null;

        try {
            // Create a URL object from the webServiceURL given
            URL url = new URL(apiURL);

            /*
            The BufferedReader class reads text from a character-input stream, buffering characters
            so as to provide for the efficient reading of characters, arrays, and lines.
             */
            reader = new BufferedReader(new InputStreamReader(url.openStream()));


            // Create a mutable sequence of characters and store its object reference into buffer
            StringBuilder buffer = new StringBuilder();

            // Create an array of characters of size 10240
            char[] chars = new char[10240];

            int numberOfCharactersRead;
            /*
            The read(chars) method of the reader object instantiated from the BufferedReader class
            reads 10240 characters as defined by "chars" into a portion of a buffered array.

            The read(chars) method attempts to read as many characters as possible by repeatedly
            invoking the read method of the underlying stream. This iterated read continues until
            one of the following conditions becomes true:

                (1) The specified number of characters have been read, thus returning the number of characters read.
                (2) The read method of the underlying stream returns -1, indicating end-of-file, or
                (3) The ready method of the underlying stream returns false, indicating that further input requests would block.

            If the first read on the underlying stream returns -1 to indicate end-of-file then the read(chars) method returns -1.
            Otherwise the read(chars) method returns the number of characters actually read.
             */
            while ((numberOfCharactersRead = reader.read(chars)) != -1) {
                buffer.append(chars, 0, numberOfCharactersRead);
            }

            // Return the String representation of the created buffer
            return buffer.toString();

        } catch (Exception e) {
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
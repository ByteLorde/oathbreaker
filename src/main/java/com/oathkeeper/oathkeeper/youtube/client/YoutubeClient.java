package com.oathkeeper.oathkeeper.youtube.client;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class YoutubeClient {

    private final String API_KEY = "AIzaSyDcnHn_r0wBnbOyqzR039RsIvlzwUOlLXw";

    private YouTube youtube;

    @PostConstruct
    void init() {
        try {
            // Set up the global instance of the HTTP transport and JSON factory
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            // Set up the YouTube object to be used for API requests
            YouTube.Builder youtubeBuilder = new YouTube.Builder(httpTransport, jsonFactory, httpRequest -> {
                // You can set any necessary HTTP request initialization parameters here
            });

            youtubeBuilder.setApplicationName("Oathkeeper");
            youtube = youtubeBuilder.build();

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<SearchResult> queryVideos(String queryTerm) {
        try {
            long maxResults = 50; // Number of videos to retrieve

            // Define the API request
            YouTube.Search.List searchRequest = null;

            searchRequest = youtube.search().list("snippet");

            searchRequest.setKey(API_KEY);
            searchRequest.setQ(queryTerm);
            searchRequest.setType("video");
            searchRequest.setMaxResults(maxResults);

            // Execute the API request and retrieve the search results
            SearchListResponse searchResponse = searchRequest.execute();
            return searchResponse.getItems();
//
//            List<SearchResult> searchResults = searchResponse.getItems();
//
//            // Process the search results
//            for (SearchResult searchResult : searchResults) {
//                // Access the video details
//                String videoId = searchResult.getId().getVideoId();
//                String title = searchResult.getSnippet().getTitle();
//
//                // Do something with the video details
//                System.out.println("Video ID: " + videoId);
//                System.out.println("Title: " + title);
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.hoarauthomas.go4lunchthp7.ui.workmates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;


@RunWith(MockitoJUnitRunner.class)
public class ViewModelWorkMatesTest {

    //Create a MockWebServer
    MockWebServer server = new MockWebServer();

    //set response for pladeDetail
    MockResponse placeDetail = new MockResponse()
            .addHeader("Content-Type", "application/json ; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody("{}");

    //set autocpomplete repsonse
    MockResponse autocomplete = new MockResponse()
            .addHeader("Content-Type", "application/json ; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody("{}");



    @Before
    public void setup() throws IOException {

        //some response
        server.enqueue(placeDetail);
        server.enqueue(autocomplete);

        //start the server
        server.start();
        HttpUrl baseUrl = server.url("/test/google/");


    }

    @After
    public void after() throws IOException {
            server.shutdown();
    }

    @Test
    public void getMediatorLiveData() throws IOException, InterruptedException {

        //set placeDetail response

      //  RecordedRequest request = server.takeRequest();
//Chat
 //      assertEquals("", request.getBody());
   //     assertNotNull(request.getHeader("Authorization"));


    }
}
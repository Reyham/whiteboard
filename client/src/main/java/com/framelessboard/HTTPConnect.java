package com.framelessboard;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTTPConnect {

    public CloseableHttpClient httpclient ;;
    public String url = "http://api.boyang.website/whiteboard";
    public String token;
    public String Code = "471e61cd2878317b204b878dfc918d2b";
    public String username;
    public String currentMananger;
    public JSONArray onlineUserList;
    public JSONArray activeUserList;
    public boolean isManager = false;

    HTTPConnect(){
        httpclient = HttpClients.createDefault();
    }

    public void setUsername(String username){
        if (username != null){
            this.username = username;
        }else{
            System.out.println("User name illegal");
        }
    }

    public void testConnect(){
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try{
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getToken(){
        try{
            String uri = url + "/accesstoken";
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(uri)
                    .setHeader("User", username)
                    .setHeader("Code", Code)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
                token = response.getFirstHeader("Access-Token").getValue();
                System.out.println("Access-Token: "+ token);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getManager(){
        try{
            String uri = url + "/manager";
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
                currentMananger = content.getString("result");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMananger(){
        if (currentMananger == null){
            System.out.println("Can not get current manager, retry");
        }
        else if (currentMananger.equals("null")){
            System.out.println("You are the first one");
            //Set yourself the mananger
            try{
                String uri = url + "/manager";
                HttpUriRequest request = RequestBuilder.post()
                        .setUri(uri)
                        .setHeader("Access-Token", token)
                        .build();
                CloseableHttpResponse response = httpclient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200){
                    JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                    isManager = true;
                    System.out.println(content);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (username.equals(currentMananger)){
            isManager = true;
            System.out.println("You are the mananger");
        }else{
            System.out.println("There is a mananger:" + currentMananger);
        }
    }

    public void deleteManager(){
        if (username.equals(currentMananger)){
            try{
                String uri = url + "/manager";
                HttpUriRequest request = RequestBuilder.delete()
                        .setUri(uri)
                        .setHeader("Access-Token", token)
                        .build();
                CloseableHttpResponse response = httpclient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200){
                    JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                    System.out.println(content);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getOnlineUsers(){
        try{
            String uri = url + "/onlineusers";
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
                onlineUserList = content.getJSONArray("result");
                System.out.println(onlineUserList);
            }
            else if(response.getStatusLine().getStatusCode() == 404){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerOnline(){
        try{
            String uri = url + "/onlineusers";
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteOnline(){
        try{
            String uri = url + "/onlineusers";
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getActiveUser(){
        try{
            String uri = url + "/activeusers";
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
                activeUserList = content.getJSONArray("result");
                System.out.println(activeUserList);
            }
            else if(response.getStatusLine().getStatusCode() == 404){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerActive(String user){
        try{
            String uri = url + "/activeusers/" + user;
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
            else{
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteActive(String user){
        try{
            String uri = url + "/activeusers/" + user;
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
            else {
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteActiveSelf(){
        try{
            String uri = url + "/activeusers/" + username;
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
            else {
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCanvas(){
        try{
            String uri = url + "/canvas";
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
            else {
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void postCanvas(){
        try{
            String uri = url + "/canvas";
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
            else{
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putCanvas(JSONObject json){
        try{
            String uri = url + "/canvas";
            HttpUriRequest request = RequestBuilder.put()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .setEntity(new StringEntity(json.toString(), "UTF-8"))
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
            else{
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCanvas(){
        try{
            String uri = url + "/canvas";
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(uri)
                    .setHeader("Access-Token", token)
                    .build();
            CloseableHttpResponse response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200){
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
            else{
                JSONObject content = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println(content);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void establishConnect(String username){
        testConnect();
        setUsername(username);
        getToken();
        getManager();
        setMananger();
        registerOnline();
        registerActive(username);
    }

    public void userLogout(HTTPConnect myHTTPConnect){
        myHTTPConnect.deleteOnline();
        myHTTPConnect.deleteActiveSelf();
    }


    public static void main( String[] args ){
        HTTPConnect myHTTPConnect = new HTTPConnect();
        myHTTPConnect.establishConnect("abc");
        myHTTPConnect.getActiveUser();
        myHTTPConnect.postCanvas();
        myHTTPConnect.getCanvas();
        JSONObject testJSON = new JSONObject();
        testJSON.put("test", "hello");
        myHTTPConnect.putCanvas(testJSON);
        myHTTPConnect.getCanvas();
        //myHTTPConnect.deleteCanvas();
    }

}

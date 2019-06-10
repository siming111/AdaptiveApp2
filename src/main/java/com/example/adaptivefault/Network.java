package com.example.adaptivefault;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Network {
    public static Solution[] ask(Error error) {
        URL url = null;
        HttpURLConnection con = null;
        OutputStream os = null;
        InputStream is = null;
        Solution[] solutions = null;
        byte[] buffer = new byte[1024];
        try {
            url = new URL("http://39.108.117.184:8888");//10.0.2.2，39.108.117.184
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setConnectTimeout(5000);
            con.connect();
            os = con.getOutputStream();
            os.write(error.getError().getBytes());
            os.flush();
            is = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                result.append(line);
            }
            solutions = Solution.parseFromJson(new JSONArray(result.toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return solutions;
    }

    public static Solution[] ask2(Error error) {
        URL url = null;
        HttpURLConnection con = null;
        OutputStream os = null;
        InputStream is = null;
        Solution[] solutions = null;
        byte[] buffer = new byte[1024];
        try {
            url = new URL("http://39.108.117.184:8886");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setConnectTimeout(5000);
            con.connect();
            os = con.getOutputStream();
            os.write(error.getError().getBytes());
            os.flush();
            is = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                result.append(line);
            }
            solutions = Solution.parseFromJson(new JSONArray(result.toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return solutions;
    }

    public static void feelGood(String error,String solution){
        URL url = null;
        HttpURLConnection con = null;
        OutputStream os = null;
        InputStream is = null;
        Solution[] solutions = null;
        byte[] buffer = new byte[1024];
        try {
            url = new URL("http://39.108.117.184:8887");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setConnectTimeout(5000);
            con.connect();
            os = con.getOutputStream();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", error);
            jsonObject.put("solution",solution);
            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.flush();
            is = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                result.append(line);
            }
            System.out.println(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(solution+" "+error+"good");
    }
}

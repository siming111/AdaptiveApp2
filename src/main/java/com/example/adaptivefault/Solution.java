package com.example.adaptivefault;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Solution implements Serializable {
    private String solution;

    public Solution(String solution) {
        this.solution = solution;
    }

    public String getSolution() {
        return this.solution;
    }

    public static Solution[] parseFromJson(JSONArray jsonArray) {
        Solution[] solutions = new Solution[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                solutions[i] = new Solution(jsonArray.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return solutions;
    }
}

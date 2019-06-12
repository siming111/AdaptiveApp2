package com.example.adaptivefault;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Error implements Serializable {

    private String error;
    private String solution;

    public Error(String error,String solution) {
        this.error = error;
        this.solution = solution;
    }

    public String getSolution(){
        return this.solution;
    }

    public String getError() {
        return this.error;
    }

    @Override
    public String toString(){
        return this.error;
    }

    public static List<Error> getFromLocal(Context context){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        List<Error> errorList = new ArrayList<>();
        File file = context.getFilesDir();
        //Log.d("file",file.getAbsolutePath());
        for(File file1:file.listFiles()){
            Log.d("file",file1.toString());
            try {
                fis = new FileInputStream(file1);
                ois = new ObjectInputStream(fis);
                Error error = (Error) ois.readObject();
                if(error!=null) {
                    errorList.add(error);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return errorList;
    }

    public void saveInLocal(Context context){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File file = context.getFilesDir();
        int n = file.listFiles().length + 1;
        try{
            String path = file.getAbsolutePath() + "/" + n + ".err";
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

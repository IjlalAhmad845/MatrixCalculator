package com.example.navigation;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Calculations {

    ArrayList<EditText> editTextList = new ArrayList<>();

    Calculations(ArrayList<EditText> editTextList){
        this.editTextList=editTextList;
    }

    public void realtimeCalc(){
        for (int i=0;i<editTextList.size();i++){
            if(editTextList.get(i).isFocused()){
                System.out.println(editTextList.get(i).getText().toString());
            }
        }
    }
}

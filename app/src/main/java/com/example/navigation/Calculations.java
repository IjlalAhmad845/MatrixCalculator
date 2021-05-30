package com.example.navigation;

import android.view.View;

public class Calculations {

    MainActivity instance;
    int outputCardIndex;

    Calculations() {
        this.instance = MainActivity.getInstance();
    }

    public void setOutputCardIndex(int outputCardIndex) {
        this.outputCardIndex = outputCardIndex;
    }

    public void masterControlFunction(){

        expressionCheck(instance.editTextList.get(0).getText().toString());

    }

    public void expressionCheck(String expression){
        if(expression.contains("A")){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);

            for(int i=0;i<instance.matrixCols.get(outputCardIndex);i++){
                for(int j=0;j<instance.matrixRows.get(outputCardIndex);j++){
                    instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.VISIBLE);
                    instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setText(instance.matrixPreviewTextviewList.get(instance.matrixNamesStringList.indexOf("A")).get(i).get(j).getText().toString());
                }
            }
        }
        else if(expression.equals("")){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);
        }
        InfixtoPostfix infixtoPostfix=new InfixtoPostfix();
        System.out.println(infixtoPostfix.infixToPostfix(expression));
    }
}

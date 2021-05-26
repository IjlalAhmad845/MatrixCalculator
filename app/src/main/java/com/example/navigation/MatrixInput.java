package com.example.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MatrixInput extends AppCompatActivity {

    public EditText editText;
    int index;
    int row=2,col=2,focusX=0,focusY=0;
    int p=0;

    MaterialButton rowUp,rowDown,colUp,colDown;

    Button[] btns=new Button[11];
    ImageButton[] imgbtns=new ImageButton[5];


    AutoCompleteTextView act;
    TextInputLayout til;
    ArrayList<String> Name=new ArrayList<>();
    ArrayAdapter<String> adt;
    CardView keyboardcard;




    TextInputEditText[][] matrixFields=new TextInputEditText[5][5];
    TextInputLayout[][] textInputLayouts=new TextInputLayout[5][5];


    TextView rowText,colText;

    ArrayList<ArrayList<String>> matrixList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_input);

        //matrixField=findViewById(R.id.filledTextField);
        initializewidgets();
        keyboardcard=findViewById(R.id.numpadCard);

        til=findViewById(R.id.menu);

        act=findViewById(R.id.textfield);
        Name.add("A");Name.add("B");Name.add("C");Name.add("D");

        adt=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_activated_1,Name);
        act.setAdapter(adt);
        act.setThreshold(1);
        act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                til.setErrorEnabled(false);
                //til.setBoxStrokeColor(Color.GREEN);
            }
        });


        Bundle bundle=getIntent().getExtras();
        ArrayList<String> object = (ArrayList<String>) bundle.getSerializable("com.example.navigation.counter");
        index= (int) bundle.getSerializable("com.example.navigation.index");

        matrixList= (ArrayList<ArrayList<String>>) bundle.getSerializable("com.example.navigation.String_list");
        editText.setText(object.get(index));

        writeMatrix(matrixList);
    }

    public void controlmatrixsize(View v){
        if(rowUp.isPressed() && row<4){
            rowText.setText(String.valueOf(++row+1));

        }


        else if(rowDown.isPressed() && row>0)
        {
            rowText.setText(String.valueOf(--row+1));

        }

        if(colUp.isPressed() && col<4)
        {
            colText.setText(String.valueOf(++col+1));

        }

        else if(colDown.isPressed() && col>0)
        {
            colText.setText(String.valueOf(--col+1));

        }

        matrixFields[focusY][focusX].requestFocus();
        ShowHideMatrix();


    }

    public void ShowHideMatrix(){
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                textInputLayouts[i][j].setVisibility(View.GONE);

        for(int i=0;i<=row;i++)
            for(int j=0;j<=col;j++)
                textInputLayouts[i][j].setVisibility(View.VISIBLE);


    }

    public void showhidekeyboard(View v){

        if(p==1){
            keyboardcard.animate().alpha(1.0f).setDuration(70).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    keyboardcard.setVisibility(View.VISIBLE);
                    super.onAnimationEnd(animation);
                }
            });

        }
        else if(p==0){
            keyboardcard.animate().alpha(0.0f).setDuration(150).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    keyboardcard.setVisibility(View.GONE);
                    super.onAnimationEnd(animation);
                }
            });

        }
        p=1-p;

    }

    public void numpad(View v){
        String str;
        int cursorindex;

                for(int i=0;i<5;i++)
                    for(int j=0;j<5;j++)
                        if(matrixFields[i][j].isFocused()) {
                            str = matrixFields[i][j].getText().toString();
                            cursorindex=matrixFields[i][j].getSelectionStart();

                            for(int k=0;k<10;k++){
                                if(btns[k].isPressed()){
                                    matrixFields[i][j].setText(str.substring(0,cursorindex)+k+str.substring(cursorindex));
                                    matrixFields[i][j].setSelection(cursorindex+1);
                                }
                            }

                            if(btns[10].isPressed() && !matrixFields[i][j].getText().toString().contains(".")){
                                matrixFields[i][j].setText(str.substring(0,cursorindex)+"."+str.substring(cursorindex));
                                matrixFields[i][j].setSelection(cursorindex+1);
                            }

                            else if (imgbtns[0].isPressed() && cursorindex>0){
                                matrixFields[i][j].setText(str.substring(0,cursorindex-1)+str.substring(cursorindex));
                                matrixFields[i][j].setSelection(cursorindex-1);
                            }
                            else if (imgbtns[0].isPressed() && cursorindex==0){
                                movefocus(v);
                            }

                        }
        //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
    }

    public void movefocus(View v){

        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                if(matrixFields[i][j].isFocused()) {
                    focusX=j;focusY=i;
                }
        System.out.println(focusY);

                //UP button
        if(imgbtns[1].isPressed() && focusY>0){
            focusY--;
            matrixFields[focusY][focusX].requestFocus();
        }
         else if(imgbtns[1].isPressed() && focusY==0 && focusX>0){
            focusY=col;
            focusX--;
            matrixFields[focusY][focusX].requestFocus();
        }

        //Right button
         if(imgbtns[2].isPressed() && focusX<row){
            focusX++;
            matrixFields[focusY][focusX].requestFocus();
        }
         else if(imgbtns[2].isPressed() && focusX==row && focusY<col){
             focusY++;
             focusX=0;
             matrixFields[focusY][focusX].requestFocus();
         }

        //Left button
         if((imgbtns[3].isPressed()  || imgbtns[0].isPressed()) && focusX>0){
            focusX--;
            matrixFields[focusY][focusX].requestFocus();
        }
         else if((imgbtns[3].isPressed()  || imgbtns[0].isPressed()) && focusX==0 && focusY>0){
             focusY--;
             focusX=row;
             matrixFields[focusY][focusX].requestFocus();
         }

            //Down button
         if(imgbtns[4].isPressed() && focusY<col){
            focusY++;
            matrixFields[focusY][focusX].requestFocus();
        }
         else if(imgbtns[4].isPressed() && focusY==col && focusX<row){
             focusY=0;
             focusX++;
             matrixFields[focusY][focusX].requestFocus();
         }

    }

    /**==================================== WRITING DATA TO MATRIX ============================================================**/
    public void writeMatrix(ArrayList<ArrayList<String>> matrixList){
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                matrixFields[i][j].setText(matrixList.get(i).get(j));
    }
    public void initializewidgets(){
        editText=findViewById(R.id.text);
        rowUp=findViewById(R.id.rowup);
        rowDown=findViewById(R.id.rowdown);
        colUp=findViewById(R.id.colup);
        colDown=findViewById(R.id.coldown);




        matrixFields[0][0]=findViewById(R.id.et00);
        matrixFields[0][1]=findViewById(R.id.et01);
        matrixFields[0][2]=findViewById(R.id.et02);
        matrixFields[0][3]=findViewById(R.id.et03);
        matrixFields[0][4]=findViewById(R.id.et04);


        matrixFields[0][0].setShowSoftInputOnFocus(false);
        matrixFields[0][1].setShowSoftInputOnFocus(false);
        matrixFields[0][2].setShowSoftInputOnFocus(false);
        matrixFields[0][3].setShowSoftInputOnFocus(false);
        matrixFields[0][4].setShowSoftInputOnFocus(false);

        matrixFields[1][0]=findViewById(R.id.et10);
        matrixFields[1][1]=findViewById(R.id.et11);
        matrixFields[1][2]=findViewById(R.id.et12);
        matrixFields[1][3]=findViewById(R.id.et13);
        matrixFields[1][4]=findViewById(R.id.et14);

        matrixFields[1][0].setShowSoftInputOnFocus(false);
        matrixFields[1][1].setShowSoftInputOnFocus(false);
        matrixFields[1][2].setShowSoftInputOnFocus(false);
        matrixFields[1][3].setShowSoftInputOnFocus(false);
        matrixFields[1][4].setShowSoftInputOnFocus(false);

        matrixFields[2][0]=findViewById(R.id.et20);
        matrixFields[2][1]=findViewById(R.id.et21);
        matrixFields[2][2]=findViewById(R.id.et22);
        matrixFields[2][3]=findViewById(R.id.et23);
        matrixFields[2][4]=findViewById(R.id.et24);

        matrixFields[2][0].setShowSoftInputOnFocus(false);
        matrixFields[2][1].setShowSoftInputOnFocus(false);
        matrixFields[2][2].setShowSoftInputOnFocus(false);
        matrixFields[2][3].setShowSoftInputOnFocus(false);
        matrixFields[2][4].setShowSoftInputOnFocus(false);

        matrixFields[3][0]=findViewById(R.id.et30);
        matrixFields[3][1]=findViewById(R.id.et31);
        matrixFields[3][2]=findViewById(R.id.et32);
        matrixFields[3][3]=findViewById(R.id.et33);
        matrixFields[3][4]=findViewById(R.id.et34);

        matrixFields[3][0].setShowSoftInputOnFocus(false);
        matrixFields[3][1].setShowSoftInputOnFocus(false);
        matrixFields[3][2].setShowSoftInputOnFocus(false);
        matrixFields[3][3].setShowSoftInputOnFocus(false);
        matrixFields[3][4].setShowSoftInputOnFocus(false);

        matrixFields[4][0]=findViewById(R.id.et40);
        matrixFields[4][1]=findViewById(R.id.et41);
        matrixFields[4][2]=findViewById(R.id.et42);
        matrixFields[4][3]=findViewById(R.id.et43);
        matrixFields[4][4]=findViewById(R.id.et44);

        matrixFields[4][0].setShowSoftInputOnFocus(false);
        matrixFields[4][1].setShowSoftInputOnFocus(false);
        matrixFields[4][2].setShowSoftInputOnFocus(false);
        matrixFields[4][3].setShowSoftInputOnFocus(false);
        matrixFields[4][4].setShowSoftInputOnFocus(false);


        //layouts
        textInputLayouts[0][0]=findViewById(R.id.L00);
        textInputLayouts[0][1]=findViewById(R.id.L01);
        textInputLayouts[0][2]=findViewById(R.id.L02);
        textInputLayouts[0][3]=findViewById(R.id.L03);
        textInputLayouts[0][4]=findViewById(R.id.L04);

        textInputLayouts[1][0]=findViewById(R.id.L10);
        textInputLayouts[1][1]=findViewById(R.id.L11);
        textInputLayouts[1][2]=findViewById(R.id.L12);
        textInputLayouts[1][3]=findViewById(R.id.L13);
        textInputLayouts[1][4]=findViewById(R.id.L14);

        textInputLayouts[2][0]=findViewById(R.id.L20);
        textInputLayouts[2][1]=findViewById(R.id.L21);
        textInputLayouts[2][2]=findViewById(R.id.L22);
        textInputLayouts[2][3]=findViewById(R.id.L23);
        textInputLayouts[2][4]=findViewById(R.id.L24);

        textInputLayouts[3][0]=findViewById(R.id.L30);
        textInputLayouts[3][1]=findViewById(R.id.L31);
        textInputLayouts[3][2]=findViewById(R.id.L32);
        textInputLayouts[3][3]=findViewById(R.id.L33);
        textInputLayouts[3][4]=findViewById(R.id.L34);

        textInputLayouts[4][0]=findViewById(R.id.L40);
        textInputLayouts[4][1]=findViewById(R.id.L41);
        textInputLayouts[4][2]=findViewById(R.id.L42);
        textInputLayouts[4][3]=findViewById(R.id.L43);
        textInputLayouts[4][4]=findViewById(R.id.L44);


        btns[0]=findViewById(R.id.btn0);
        btns[1]=findViewById(R.id.btn1);
        btns[2]=findViewById(R.id.btn2);
        btns[3]=findViewById(R.id.btn3);
        btns[4]=findViewById(R.id.btn4);
        btns[5]=findViewById(R.id.btn5);
        btns[6]=findViewById(R.id.btn6);
        btns[7]=findViewById(R.id.btn7);
        btns[8]=findViewById(R.id.btn8);
        btns[9]=findViewById(R.id.btn9);
        btns[10]=findViewById(R.id.btndot);

        imgbtns[0]=findViewById(R.id.backspace);
        imgbtns[1]=findViewById(R.id.up);
        imgbtns[2]=findViewById(R.id.right);
        imgbtns[3]=findViewById(R.id.left);
        imgbtns[4]=findViewById(R.id.down);

        rowText=findViewById(R.id.rowtext);
        colText=findViewById(R.id.coltext);

        ShowHideMatrix();

    }

    /**============================================ SENDING MATRIX DATA BACK TO HOME PAGE==================================**/
    public void sendData(View v){

        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                matrixList.get(i).set(j,matrixFields[i][j].getText().toString());

        MainActivity.getInstance().inittextviews(index,editText.getText().toString(),matrixList);
        if(act.getText().toString().length()==0){
            til.setError("blah blah blah this line is for only showing red colored box");
            Toast.makeText(this, "Matrix name field required", Toast.LENGTH_SHORT).show();
        }

        else
        super.onBackPressed();
    }

    public void back(View v) {
        super.onBackPressed();
    }


}
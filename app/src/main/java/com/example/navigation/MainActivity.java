package com.example.navigation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    Calculations calculations;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    LinearLayout linearLayout,verticalLL;
    HorizontalScrollView scrollView;
    ScrollView scrollView2;
    int counter1=0,counter2=0;

    Button[] buttons =new Button[16];
    ImageButton[] imgbtns=new ImageButton[3];
    CardView keyboardCard;


    //Arraylists Related to Matrix preview cards
    ArrayList<Integer> matrixPreviewIDList =new ArrayList<>();
    ArrayList<ArrayList<ArrayList<TextView>>> matrixPreviewTextviewList= new ArrayList<>();
    ArrayList<ArrayList<String>> matrixPreviewStringList= new ArrayList<>();
    ArrayList<Integer> matrixRows=new ArrayList<>();
    ArrayList<Integer> matrixCols=new ArrayList<>();
    ArrayList<String> matrixNamesStringList =new ArrayList<>();
    ArrayList<TextView> matrixNamesTextviewList=new ArrayList<>();

    String[] alphabets={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    //Arraylists related to result cards
    ArrayList<EditText> editTextList=new ArrayList<>();
    ArrayList<ArrayList<ArrayList<TextView>>> matrixOutputTextviewList= new ArrayList<>();
    ArrayList<Integer> matrixOutputIDList =new ArrayList<>();
    ArrayList<TextView> messageTextviewList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;
        calculations=new Calculations();

        linearLayout=findViewById(R.id.linearLayout);
        scrollView=findViewById(R.id.scrollview);

        verticalLL=findViewById(R.id.VerticalLL);
        scrollView2=findViewById(R.id.scrollView2);

        keyboardCard =findViewById(R.id.KeyboardCard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView2.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(verticalLL.getChildCount()>2)
                        keyboardCard.setVisibility(View.GONE);
                }
            });
        }

        initializeWidgets();

    }
    /**=================================================== REVERTING BACK TO MATRIX PREVIEW CARDS =======================================================**/
    public void inittextviews(int index,ArrayList<ArrayList<String>> s1,String matrixName){


        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                matrixPreviewTextviewList.get(index).get(i).get(j).setVisibility(View.GONE);

        for(int i=0;i<s1.size();i++)
            for(int j=0;j<s1.get(0).size();j++){
                matrixPreviewTextviewList.get(index).get(i).get(j).setText(s1.get(i).get(j));
                matrixPreviewTextviewList.get(index).get(i).get(j).setVisibility(View.VISIBLE);
            }


        matrixRows.set(index,s1.get(0).size());
        matrixCols.set(index,s1.size());

        //Updating matrix names on preview cards given by matrix making page
        matrixNamesStringList.set(index,matrixName);
        matrixNamesTextviewList.get(index).setText(matrixName);

        for(int i=0;i<editTextList.size();i++)
            if(editTextList.get(i).isFocused())
                sendToCalculations(i);

    }

    /**=================================================== For linking Main Activity to Matrix Activity**/
    public static MainActivity getInstance() {
        return instance;
    }

    /**=================================================== DYNAMIC MATRIX PREVIEW CARDS =======================================================**/
    public void addCards(View v){

        ConstraintLayout Cl=new ConstraintLayout(this);
        ImageButton removeButton=new ImageButton(this);
        removeButton.setId(View.generateViewId());
        removeButton.setImageResource(R.drawable.ic_trash);
        removeButton.setBackgroundColor(Color.TRANSPARENT);

        ImageButton editButton = new ImageButton(this);
        editButton.setId(View.generateViewId());
        editButton.setImageResource(R.drawable.ic_edit);
        editButton.setBackgroundColor(Color.TRANSPARENT);


        TextView matrixName=new TextView(this);

        //recycler name mechanism that recycles deleted names to new matrices
        for(int i=0;i<26;i++){
            if(!matrixNamesStringList.contains(alphabets[i])){
                matrixName.setText(alphabets[i]);
                matrixNamesStringList.add(alphabets[i]);
                break;
            }
        }

        matrixName.setId(View.generateViewId());


        //Setting dimensions of each matrix
        matrixRows.add(5);
        matrixCols.add(5);


        //Linear Layout Container of Whole matrix skeleton
        LinearLayout matrixPreviewContainerLL=new LinearLayout(this);
        matrixPreviewContainerLL.setOrientation(LinearLayout.HORIZONTAL);
        matrixPreviewContainerLL.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        matrixPreviewContainerLL.setId(View.generateViewId());

        matrixPreviewTextviewList.add(new ArrayList<>());


        //loop for adding Vertical Linear layouts in above Container
        //Columns
        for(int i=0;i<5;i++){
            LinearLayout matrixPreviewLLArray=new LinearLayout(this);
            matrixPreviewLLArray.setOrientation(LinearLayout.VERTICAL);
            matrixPreviewLLArray.setGravity(Gravity.CENTER);
            matrixPreviewLLArray.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));



            matrixPreviewTextviewList.get(counter1).add(new ArrayList<>());



            //loop for adding TextViews in Vertical Linear layouts in container LL
            //rows
            for(int j=0;j<5;j++){
                TextView matrixPreviewTextviewArray=new TextView(this);
                final float scale = matrixPreviewTextviewArray.getContext().getResources().getDisplayMetrics().density;

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins((int) (4 * scale + 0.5f), (int) (0 * scale + 0.5f), (int) (4 * scale + 0.5f), (int) (0 * scale + 0.5f));

                matrixPreviewTextviewArray.setTextSize(15f);
                matrixPreviewTextviewArray.setLayoutParams(params);
                matrixPreviewTextviewArray.setText("0");
                matrixPreviewLLArray.addView(matrixPreviewTextviewArray);

                matrixPreviewTextviewList.get(counter1).get(i).add(matrixPreviewTextviewArray);

            }

            matrixPreviewContainerLL.addView(matrixPreviewLLArray);
        }



        //ripple effect
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,outValue, true);

        removeButton.setBackgroundResource(outValue.resourceId);
        editButton.setBackgroundResource(outValue.resourceId);



        Cl.addView(removeButton);
        Cl.addView(editButton);

        Cl.addView(matrixName);
        Cl.addView(matrixPreviewContainerLL);

        //adding matrix skeleton Container's Id to ID list
        matrixPreviewIDList.add(matrixPreviewContainerLL.getId());
        matrixNamesTextviewList.add(matrixName);

        //connecting different Constraints programmatically on Constraint layout
        ConstraintSet set=new ConstraintSet();
        set.clone(Cl);
        set.connect(removeButton.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,12);
        set.connect(removeButton.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);

        set.connect(editButton.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,12);
        set.connect(editButton.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0);

        set.connect(matrixName.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,54);
        set.connect(matrixName.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        set.connect(matrixName.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,20);


        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,54);
        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0);
        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
        set.applyTo(Cl);


        CardView cardView = new CardView(this);
        final float scale = cardView.getContext().getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (150 * scale + 0.5f),
                (int) (200 * scale + 0.5f)
        );
        params.setMargins((int) (10 * scale + 0.5f), (int) (20 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f));



        cardView.setElevation(10f);
        cardView.setRadius(10f);
        cardView.addView(Cl);
        cardView.setAlpha(0f);

        //adding upcoming cards to horizontal scroll view
        linearLayout.addView(cardView,linearLayout.getChildCount()-1,params);

        cardView.animate().alpha(1.0f).setDuration(200).setListener(null);
        scrollView.fullScroll(View.FOCUS_RIGHT);

        //Little trash button at the corner of each card functionality
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        //removing by index retrieved by Ids of container LL
                        matrixPreviewTextviewList.remove(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId()));
                        matrixRows.remove(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId()));
                        matrixCols.remove(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId()));

                        //removing by item of current linked card
                        matrixNamesStringList.remove(matrixName.getText().toString());
                        matrixPreviewIDList.remove((Integer) matrixPreviewContainerLL.getId());
                        matrixNamesTextviewList.remove(matrixName);
                        matrixNamesStringList.remove(matrixName.getText().toString());

                        //removing all children from Cl
                        Cl.removeAllViews();
                        //finally removing card
                        linearLayout.removeView(cardView);

                        //rollback counter by 1
                        counter1--;

                        super.onAnimationEnd(animation);
                    }
                });
            }
        });

        //Little EditButton for entering matrix making activity
        editButton.setOnClickListener(v1 -> {
            //ArrayList<TextView> object = new ArrayList<>();
            matrixPreviewStringList.clear();
            for(int i = 0; i<matrixCols.get(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId())); i++){
                matrixPreviewStringList.add(new ArrayList<>());
                for(int j = 0; j<matrixRows.get(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId())); j++)
                    matrixPreviewStringList.get(i).add(matrixPreviewTextviewList.get(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId())).get(i).get(j).getText().toString());
            }

            //sending all the data from home to page matrix making page through a large bundle
            Intent intent = new Intent(getApplicationContext(), MatrixInput.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("com.example.navigation.index", matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId()));
            bundle.putSerializable("com.example.navigation.String_list",matrixPreviewStringList);
            bundle.putSerializable("com.example.navigation.columns",matrixPreviewStringList.get(0).size());
            bundle.putSerializable("com.example.navigation.rows",matrixPreviewStringList.size());
            bundle.putSerializable("com.example.navigation.matrixName", matrixNamesStringList.get(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId())));
            bundle.putSerializable("com.example.navigation.matrixNames", matrixNamesStringList);
            intent.putExtras(bundle);
            startActivity(intent);


        });

        //Entering Matrix names on calculation Text fields
        cardView.setOnClickListener(v1 -> {

                    String str;
                    int cursorIndex;

                    for(int i=0;i<editTextList.size();i++) {
                        if (editTextList.get(i).isFocused()) {
                            str = editTextList.get(i).getText().toString();
                            cursorIndex = editTextList.get(i).getSelectionStart();

                            if(str.length()>0 && (Character.isLetterOrDigit(str.charAt(cursorIndex-1)) || str.charAt(cursorIndex-1)==')')){
                                editTextList.get(i).setText(str.substring(0,cursorIndex)+"·"+matrixNamesStringList.get(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId()))+str.substring(cursorIndex));
                                editTextList.get(i).setSelection(cursorIndex+2);
                            }
                            else {
                                editTextList.get(i).setText(str.substring(0,cursorIndex)+matrixNamesStringList.get(matrixPreviewIDList.indexOf(matrixPreviewContainerLL.getId()))+str.substring(cursorIndex));
                                editTextList.get(i).setSelection(cursorIndex+1);
                            }


                            sendToCalculations(i);
                        }
                    }



        });

        //ONE OF THE MOST IMPORTANT VARIABLE (Controls all the array lists)
        counter1++;

    }
/**=================================================== DYNAMIC TEXT FIELDS =======================================================**/
    public void addtextfield(){

        ConstraintLayout Cl=new ConstraintLayout(this);
        ImageButton removeButton=new ImageButton(this);
        removeButton.setId(View.generateViewId());
        removeButton.setImageResource(R.drawable.ic_cross);
        removeButton.setBackgroundColor(Color.TRANSPARENT);


        EditText editText=new EditText(this);
        editText.setShowSoftInputOnFocus(false);
        editText.setId(View.generateViewId());
        editText.setHint("Tap on Matrix Cards");

        final float scale1 = editText.getContext().getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams editTextparams = new LinearLayout.LayoutParams(
                verticalLL.getWidth()-(int) (160 * scale1 + 0.5f),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        TextView messageTextview=new TextView(this);
        messageTextview.setId(View.generateViewId());
        messageTextview.setTextColor(Color.RED);



        //Linear Layout Container of Whole matrix skeleton
        LinearLayout matrixPreviewContainerLL=new LinearLayout(this);
        matrixPreviewContainerLL.setOrientation(LinearLayout.HORIZONTAL);
        matrixPreviewContainerLL.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        matrixPreviewContainerLL.setId(View.generateViewId());

        matrixOutputTextviewList.add(new ArrayList<>());

        //loop for adding Vertical Linear layouts in above Container
        //Columns
        for(int i=0;i<5;i++){
            LinearLayout matrixPreviewLLArray=new LinearLayout(this);
            matrixPreviewLLArray.setOrientation(LinearLayout.VERTICAL);
            matrixPreviewLLArray.setGravity(Gravity.CENTER);
            matrixPreviewLLArray.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            matrixOutputTextviewList.get(counter2).add(new ArrayList<>());


            //loop for adding TextViews in Vertical Linear layouts in container LL
            //rows
            for(int j=0;j<5;j++){
                TextView matrixPreviewTextviewArray=new TextView(this);
                final float scale = matrixPreviewTextviewArray.getContext().getResources().getDisplayMetrics().density;

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins((int) (4 * scale + 0.5f), (int) (0 * scale + 0.5f), (int) (4 * scale + 0.5f), (int) (0 * scale + 0.5f));

                matrixPreviewTextviewArray.setTextSize(15f);
                matrixPreviewTextviewArray.setLayoutParams(params);
                matrixPreviewTextviewArray.setText("0");
                matrixPreviewLLArray.addView(matrixPreviewTextviewArray);

                matrixOutputTextviewList.get(counter2).get(i).add(matrixPreviewTextviewArray);

            }

            matrixPreviewContainerLL.addView(matrixPreviewLLArray);
        }


        //ripple effect
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,outValue, true);

        removeButton.setBackgroundResource(outValue.resourceId);


        Cl.addView(removeButton);
        Cl.addView(editText,editTextparams);
        Cl.addView(messageTextview);
        Cl.addView(matrixPreviewContainerLL);

        editTextList.add(editText);
        matrixOutputIDList.add(matrixPreviewContainerLL.getId());
        messageTextviewList.add(messageTextview);

        ConstraintSet set=new ConstraintSet();
        set.clone(Cl);
        set.connect(removeButton.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
        set.connect(removeButton.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,24);

        set.connect(editText.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,16);
        set.connect(editText.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,12);

        set.connect(messageTextview.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,16);
        set.connect(messageTextview.getId(),ConstraintSet.TOP,editText.getId(),ConstraintSet.BOTTOM,12);

        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,100);
        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0);

        set.applyTo(Cl);



        CardView cardView = new CardView(this);
        final float scale2= cardView.getContext().getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams cardparams = new LinearLayout.LayoutParams(
               ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (160 * scale2 + 0.5f)
        );
        cardparams.setMargins((int) (10 * scale2 + 0.5f), (int) (20 * scale2 + 0.5f), (int) (10 * scale2 + 0.5f), (int) (0 * scale2 + 0.5f));



        cardView.setElevation(20f);
        cardView.setRadius(10f);


        cardView.addView(Cl);
        cardView.setAlpha(0f);

        verticalLL.addView(cardView,verticalLL.getChildCount()-2,cardparams);
        cardView.animate().alpha(1.0f).setDuration(200).setListener(null);
        scrollView2.fullScroll(View.FOCUS_DOWN);

       editText.setOnClickListener(v -> keyboardCard.setVisibility(View.VISIBLE));


        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(verticalLL.getChildAt(verticalLL.getChildCount()-3)==cardView)
                            scrollView2.smoothScrollBy(0, (int) (-185 * scale2 + 0.5f));
                        if(verticalLL.getChildCount()<=3)
                            keyboardCard.setVisibility(View.VISIBLE);

                        matrixOutputTextviewList.remove(matrixOutputIDList.indexOf(matrixPreviewContainerLL.getId()));
                        matrixOutputIDList.remove((Integer) matrixPreviewContainerLL.getId());
                        messageTextviewList.remove(messageTextview);

                        editTextList.remove(editText);
                        verticalLL.removeView(cardView);

                        Cl.removeAllViews();
                        counter2--;

                        super.onAnimationEnd(animation);
                    }
                });
            }
        });

        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                matrixOutputTextviewList.get(counter2).get(i).get(j).setVisibility(View.GONE);

            counter2++;
    }

    /**================================================ HOMEPAGE'S KEYBOARD FUNCTIONALITY ==============================**/
    public void KeyboardInput(View v){
        String str;
        int cursorIndex;

        for(int i=0;i<editTextList.size();i++) {
            if (editTextList.get(i).isFocused()) {
                str = editTextList.get(i).getText().toString();
                cursorIndex = editTextList.get(i).getSelectionStart();

                for(int j=0;j<10;j++){
                    if(buttons[j].isPressed()){
                        if(str.length()>0 && (Character.isAlphabetic(str.charAt(cursorIndex-1)) || str.charAt(cursorIndex-1)==')')){
                            editTextList.get(i).setText(str.substring(0,cursorIndex)+"·"+j+str.substring(cursorIndex));
                            editTextList.get(i).setSelection(cursorIndex+2);
                        }
                        else{
                            editTextList.get(i).setText(str.substring(0,cursorIndex)+j+str.substring(cursorIndex));
                            editTextList.get(i).setSelection(cursorIndex+1);
                        }

                        buttons[11].setEnabled(true);
                        buttons[12].setEnabled(true);
                        buttons[13].setEnabled(true);
                    }
                }

                if(buttons[10].isPressed() && !editTextList.get(i).getText().toString().contains(".")){
                    editTextList.get(i).setText(str.substring(0,cursorIndex)+"."+str.substring(cursorIndex));
                    editTextList.get(i).setSelection(cursorIndex+1);
                }
                else if(buttons[11].isPressed()){
                    editTextList.get(i).setText(str.substring(0,cursorIndex)+"·"+str.substring(cursorIndex));
                    editTextList.get(i).setSelection(cursorIndex+1);
                    buttons[11].setEnabled(false);
                    buttons[12].setEnabled(false);
                    buttons[13].setEnabled(false);
                }
                else if(buttons[12].isPressed()){
                    editTextList.get(i).setText(str.substring(0,cursorIndex)+"+"+str.substring(cursorIndex));
                    editTextList.get(i).setSelection(cursorIndex+1);
                    buttons[11].setEnabled(false);
                    buttons[12].setEnabled(false);
                    buttons[13].setEnabled(false);
                }
                else if(buttons[13].isPressed()){
                    editTextList.get(i).setText(str.substring(0,cursorIndex)+"-"+str.substring(cursorIndex));
                    editTextList.get(i).setSelection(cursorIndex+1);
                    buttons[11].setEnabled(false);
                    buttons[12].setEnabled(false);
                    buttons[13].setEnabled(false);
                }
                else if(buttons[14].isPressed()){
                    if(str.length()>0 && Character.isLetterOrDigit(str.charAt(cursorIndex-1))){
                        editTextList.get(i).setText(str.substring(0,cursorIndex)+"·("+str.substring(cursorIndex));
                        editTextList.get(i).setSelection(cursorIndex+2);
                    }
                    else {
                        editTextList.get(i).setText(str.substring(0,cursorIndex)+"("+str.substring(cursorIndex));
                        editTextList.get(i).setSelection(cursorIndex+1);
                    }


                }
                else if(buttons[15].isPressed()){
                    editTextList.get(i).setText(str.substring(0,cursorIndex)+")"+str.substring(cursorIndex));
                    editTextList.get(i).setSelection(cursorIndex+1);
                }

                else if (imgbtns[0].isPressed() && cursorIndex>0){
                    editTextList.get(i).setText(str.substring(0,cursorIndex-1)+str.substring(cursorIndex));
                    editTextList.get(i).setSelection(cursorIndex-1);

                    str=editTextList.get(i).getText().toString();

                    if(str.length()-1>0 && (str.charAt(str.length()-1)=='+' || str.charAt(str.length()-1)=='-' || str.charAt(str.length()-1)=='·')){
                        buttons[11].setEnabled(false);
                        buttons[12].setEnabled(false);
                        buttons[13].setEnabled(false);
                    }
                    else{
                        //new way of assigning boolean with condition :)
                        buttons[11].setEnabled(str.length() != 0);
                        buttons[12].setEnabled(true);
                        buttons[13].setEnabled(true);
                    }

                }
                else if (imgbtns[1].isPressed() && cursorIndex>0){
                    editTextList.get(i).setSelection(editTextList.get(i).getSelectionStart()-1);
                }
                else if (imgbtns[2].isPressed() && cursorIndex<str.length()){
                    editTextList.get(i).setSelection(editTextList.get(i).getSelectionStart()+1);
                }

                sendToCalculations(i);
            }
        }



    }

    public void sendToCalculations(int outputCardIndex){
        calculations.setOutputCardIndex(outputCardIndex);
        calculations.masterControlFunction();
    }

    public void initializeWidgets(){

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=findViewById(R.id.nav);
        drawerLayout=findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        CardView cardView = new CardView(this);
        final float scale = cardView.getContext().getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams cardparams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (160 * scale + 0.5f)
        );
        cardparams.setMargins((int) (10 * scale + 0.5f), (int) (20 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (20 * scale + 0.5f));

        cardView.setElevation(20f);
        cardView.setRadius(10f);


       TextView textView=new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText("TAP TO ADD MORE CARDS");
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);


        cardView.addView(textView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtextfield();
            }
        });


       //cardView.animate().alpha(1.0f).setDuration(200).setListener(null);
        //scrollView2.fullScroll(View.FOCUS_DOWN);

                verticalLL.addView(cardView, verticalLL.getChildCount() - 1, cardparams);


        buttons[0]=findViewById(R.id.button0);
        buttons[1]=findViewById(R.id.button1);
        buttons[2]=findViewById(R.id.button2);
        buttons[3]=findViewById(R.id.button3);
        buttons[4]=findViewById(R.id.button4);
        buttons[5]=findViewById(R.id.button5);
        buttons[6]=findViewById(R.id.button6);
        buttons[7]=findViewById(R.id.button7);
        buttons[8]=findViewById(R.id.button8);
        buttons[9]=findViewById(R.id.button9);
        buttons[10]=findViewById(R.id.buttonDot);
        buttons[11]=findViewById(R.id.buttonX);
        buttons[12]=findViewById(R.id.buttonAdd);
        buttons[13]=findViewById(R.id.buttonMinus);
        buttons[14]=findViewById(R.id.buttonBracketStart);
        buttons[15]=findViewById(R.id.buttonBracketEnd);

        buttons[11].setEnabled(false);

        imgbtns[0]=findViewById(R.id.backspace);
        imgbtns[1]=findViewById(R.id.left);
        imgbtns[2]=findViewById(R.id.right);

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if(keyboardCard.getVisibility()==View.VISIBLE && verticalLL.getChildCount()>2)
            keyboardCard.setVisibility(View.GONE);
        else
        super.onBackPressed();
    }
}
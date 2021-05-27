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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    int rows=5,cols=5;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    LinearLayout linearLayout,verticalLL;
    HorizontalScrollView scrollView;
    ScrollView scrollView2;
    int counter=0;

    Button[] buttons =new Button[12];
    ImageButton[] imgbtns=new ImageButton[3];
    CardView keyboardCard;


    ArrayList<TextView> textlist=new ArrayList<>();
    ArrayList<String> stringlist=new ArrayList<>();
    ArrayList<Integer> IDlist=new ArrayList<>();

    ArrayList<ArrayList<ArrayList<TextView>>> matrixPreviewTextviewList= new ArrayList<>();
    ArrayList<ArrayList<String>> matrixPreviewStringList= new ArrayList<>();

    ArrayList<CardView> cardlist=new ArrayList<>();
    ArrayList<ConstraintLayout> ClLlist=new ArrayList<>();
    ArrayList<EditText> editTextList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;

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
    public void inittextviews(int index,String s,ArrayList<ArrayList<String>> s1){

            textlist.get(index).setText(s);

        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                matrixPreviewTextviewList.get(index).get(i).get(j).setVisibility(View.GONE);

        for(int i=0;i<s1.size();i++)
            for(int j=0;j<s1.get(0).size();j++){
                matrixPreviewTextviewList.get(index).get(i).get(j).setText(s1.get(i).get(j));
                matrixPreviewTextviewList.get(index).get(i).get(j).setVisibility(View.VISIBLE);
            }

        cols=s1.size();
            rows=s1.get(0).size();

    }

    public static MainActivity getInstance() {
        return instance;
    }

    /**=================================================== DYNAMIC MATRIX PREVIEW CARDS =======================================================**/
    public void addcard(View v){

        ConstraintLayout Cl=new ConstraintLayout(this);
        ImageButton imageButton=new ImageButton(this);
        imageButton.setId(View.generateViewId());
        imageButton.setImageResource(R.drawable.ic_trash);
        imageButton.setBackgroundColor(Color.TRANSPARENT);

        TextView textView=new TextView(this);
        textView.setText(String.valueOf(counter));
        textView.setId(View.generateViewId());

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
            matrixPreviewLLArray.setId(View.generateViewId());


            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            matrixPreviewTextviewList.get(counter).add(new ArrayList<>());



            //loop for adding TextViews in Vertical Linear layouts in container LL
            //rows
            for(int j=0;j<5;j++){
                TextView matrixPreviewStringArray=new TextView(this);
                final float scale = matrixPreviewStringArray.getContext().getResources().getDisplayMetrics().density;

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params2.setMargins((int) (4 * scale + 0.5f), (int) (0 * scale + 0.5f), (int) (4 * scale + 0.5f), (int) (0 * scale + 0.5f));

                matrixPreviewStringArray.setTextSize(15f);
                matrixPreviewStringArray.setLayoutParams(params2);
                matrixPreviewStringArray.setText("0");
                matrixPreviewStringArray.setId(View.generateViewId());
                matrixPreviewLLArray.addView(matrixPreviewStringArray);

                matrixPreviewTextviewList.get(counter).get(i).add(matrixPreviewStringArray);

            }

            matrixPreviewContainerLL.addView(matrixPreviewLLArray);
        }



        //ripple effect
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,outValue, true);

        imageButton.setBackgroundResource(outValue.resourceId);


        Cl.addView(imageButton);
        Cl.addView(textView);
        Cl.addView(matrixPreviewContainerLL);

        textlist.add(textView);
        stringlist.add(textView.getText().toString());
        IDlist.add(textView.getId());

        ConstraintSet set=new ConstraintSet();
        set.clone(Cl);
        set.connect(imageButton.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,16);
        set.connect(imageButton.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);

        set.connect(textView.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);
        set.connect(textView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        set.connect(textView.getId(),ConstraintSet.BOTTOM,imageButton.getId(),ConstraintSet.BOTTOM,0);

        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);
        set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,80);
      //  set.connect(matrixPreviewContainerLL.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);



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

        cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //ArrayList<TextView> object = new ArrayList<>();
        matrixPreviewStringList.clear();
        for(int i=0;i<cols;i++){
            matrixPreviewStringList.add(new ArrayList<>());
            for(int j=0;j<rows;j++)
                matrixPreviewStringList.get(i).add(matrixPreviewTextviewList.get(IDlist.indexOf(textView.getId())).get(i).get(j).getText().toString());
        }

        Intent intent = new Intent(getApplicationContext(), MatrixInput.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("com.example.navigation.index",IDlist.indexOf(textView.getId()));
        bundle.putSerializable("com.example.navigation.counter", stringlist);
        bundle.putSerializable("com.example.navigation.String_list",matrixPreviewStringList);
        bundle.putSerializable("com.example.navigation.columns",matrixPreviewStringList.get(0).size());
        bundle.putSerializable("com.example.navigation.rows",matrixPreviewStringList.size());
        intent.putExtras(bundle);
        startActivity(intent);


    }
});counter++;
        linearLayout.addView(cardView,linearLayout.getChildCount()-1,params);
        cardView.animate().alpha(1.0f).setDuration(200).setListener(null);
        scrollView.fullScroll(View.FOCUS_RIGHT);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        linearLayout.removeView(cardView);

                        Cl.removeAllViews();

                        super.onAnimationEnd(animation);
                    }
                });
            }
        });

    }
/**=================================================== DYNAMIC TEXT FIELDS =======================================================**/
    public void addtextfield(){

        ConstraintLayout Cl=new ConstraintLayout(this);
        ImageButton imageButton=new ImageButton(this);
        imageButton.setId(View.generateViewId());
        imageButton.setImageResource(R.drawable.ic_trash);
        imageButton.setBackgroundColor(Color.TRANSPARENT);


        EditText editText=new EditText(this);
        editText.setShowSoftInputOnFocus(false);
        editText.setId(View.generateViewId());
        editText.setText(String.valueOf(counter++));

        final float scale1 = editText.getContext().getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams editTextparams = new LinearLayout.LayoutParams(
                verticalLL.getWidth()-(int) (150 * scale1 + 0.5f),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        //editTextparams.setMargins((int) (10 * scale1 + 0.5f), (int) (10 * scale1 + 0.5f), (int) (150 * scale1 + 0.5f), (int) (0 * scale1 + 0.5f));




        //ripple effect
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,outValue, true);

        imageButton.setBackgroundResource(outValue.resourceId);


        Cl.addView(imageButton);
        Cl.addView(editText,editTextparams);
        editTextList.add(editText);

        ConstraintSet set=new ConstraintSet();
        set.clone(Cl);
        set.connect(imageButton.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
        set.connect(imageButton.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,16);

        set.connect(editText.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,16);
        set.connect(editText.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,12);
        //set.connect(editText.getId(),ConstraintSet.RIGHT,imageButton.getId(),ConstraintSet.LEFT,20);

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

       editText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               keyboardCard.setVisibility(View.VISIBLE);
           }
       });

       /*editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               keyboardCard.setVisibility(View.VISIBLE);
           }
       });*/

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(verticalLL.getChildAt(verticalLL.getChildCount()-3)==cardView)
                            scrollView2.smoothScrollBy(0, (int) (-185 * scale2 + 0.5f));
                        if(verticalLL.getChildCount()<=3)
                            keyboardCard.setVisibility(View.VISIBLE);

                        verticalLL.removeView(cardView);

                        Cl.removeAllViews();

                        super.onAnimationEnd(animation);
                    }
                });
            }
        });
    }

    public void KeyboardInput(View v){
        String str;
        int cursorindex;

        for(int i=0;i<editTextList.size();i++) {
            if (editTextList.get(i).isFocused()) {
                str = editTextList.get(i).getText().toString();
                cursorindex = editTextList.get(i).getSelectionStart();

                for(int j=0;j<10;j++){
                    if(buttons[j].isPressed()){
                        editTextList.get(i).setText(str.substring(0,cursorindex)+j+str.substring(cursorindex));
                        editTextList.get(i).setSelection(cursorindex+1);
                    }
                }

                if(buttons[10].isPressed() && !editTextList.get(0).getText().toString().contains(".")){
                    editTextList.get(i).setText(str.substring(0,cursorindex)+"."+str.substring(cursorindex));
                    editTextList.get(i).setSelection(cursorindex+1);
                }

                else if (imgbtns[0].isPressed() && cursorindex>0){
                    editTextList.get(i).setText(str.substring(0,cursorindex-1)+str.substring(cursorindex));
                    editTextList.get(i).setSelection(cursorindex-1);
                }

            }
        }
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

        /*LinearLayout.LayoutParams editTextparams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        editTextparams.setMargins((int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (150 * scale + 0.5f), (int) (0 * scale + 0.5f));*/

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

        imgbtns[0]=findViewById(R.id.backspace);

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
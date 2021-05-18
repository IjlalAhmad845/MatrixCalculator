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

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    LinearLayout linearLayout,verticalLL;
    HorizontalScrollView scrollView;
    ScrollView scrollView2;
    int counter=0;

    Button[] buttons =new Button[12];
    ImageButton[] imgbtns=new ImageButton[3];


    ArrayList<TextView> textlist=new ArrayList<>();
    ArrayList<String> stringlist=new ArrayList<>();
    ArrayList<Integer> IDlist=new ArrayList<>();
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

        initializeWidgets();

    }

    public void inittextviews(int index,String s){

            textlist.get(index).setText(s);
    }
    public void secondactivity(View v){
        Intent intent=new Intent(this,MatrixInput.class);
        startActivity(intent);
    }
    public static MainActivity getInstance() {
        return instance;
    }

    public void addcard(View v){

        ConstraintLayout Cl=new ConstraintLayout(this);
        ImageButton imageButton=new ImageButton(this);
        imageButton.setId(View.generateViewId());
        imageButton.setImageResource(R.drawable.ic_trash);
        imageButton.setBackgroundColor(Color.TRANSPARENT);

        TextView textView=new TextView(this);
        textView.setText(String.valueOf(counter++));
        textView.setId(View.generateViewId());






        //ripple effect
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,outValue, true);

        imageButton.setBackgroundResource(outValue.resourceId);


        Cl.addView(imageButton);
        Cl.addView(textView);

        textlist.add(textView);
        stringlist.add(textView.getText().toString());
        IDlist.add(textView.getId());

        ConstraintSet set=new ConstraintSet();
        set.clone(Cl);
        set.connect(imageButton.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,16);
        set.connect(imageButton.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);

        set.connect(textView.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);
        set.connect(textView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        set.connect(textView.getId(),ConstraintSet.TOP,imageButton.getId(),ConstraintSet.BOTTOM,0);

     


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

        Intent intent = new Intent(getApplicationContext(), MatrixInput.class);
        intent.putExtra("com.example.navigation.index",IDlist.indexOf(textView.getId()));
        intent.putExtra("com.example.navigation.String_list",stringlist);
        startActivity(intent);


    }
});
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

    public void addtextfield(){
        CardView cardView = new CardView(this);
        final float scale = cardView.getContext().getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams cardparams = new LinearLayout.LayoutParams(
               ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (160 * scale + 0.5f)
        );
        cardparams.setMargins((int) (10 * scale + 0.5f), (int) (20 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (0 * scale + 0.5f));

        LinearLayout.LayoutParams editTextparams = new LinearLayout.LayoutParams(
               ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        editTextparams.setMargins((int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (150 * scale + 0.5f), (int) (0 * scale + 0.5f));

        cardView.setElevation(20f);
        cardView.setRadius(10f);


        EditText editText=new EditText(this);
        editText.setShowSoftInputOnFocus(false);
        editTextList.add(editText);
       // editText.setText(String.valueOf(counter++));


        cardView.addView(editText,editTextparams);

        verticalLL.addView(cardView,verticalLL.getChildCount()-2,cardparams);
        cardView.animate().alpha(1.0f).setDuration(200).setListener(null);
       scrollView2.fullScroll(View.FOCUS_DOWN);



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
        else
        super.onBackPressed();
    }
}
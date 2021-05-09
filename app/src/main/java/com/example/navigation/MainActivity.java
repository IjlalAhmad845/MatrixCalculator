package com.example.navigation;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    LinearLayout linearLayout;
    HorizontalScrollView scrollView;
    int counter=0;


    ArrayList<TextView> textlist=new ArrayList<>();
    ArrayList<String> stringlist=new ArrayList<>();
    ArrayList<Integer> IDlist=new ArrayList<>();
    ArrayList<CardView> cardlist=new ArrayList<>();
    ArrayList<ConstraintLayout> ClLlist=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;

        linearLayout=findViewById(R.id.linearLayout);
        scrollView=findViewById(R.id.scrollview);



        initdrawer();


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

    public void initdrawer(){

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=findViewById(R.id.nav);
        drawerLayout=findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
        super.onBackPressed();
    }
}
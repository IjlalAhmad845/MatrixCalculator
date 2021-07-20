package com.example.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    MainActivity instance;
    SeekBar rowSeekbar,colSeekbar;
    TextView rowIndex,colIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();

        Bundle bundle=getIntent().getExtras();

        setdata(bundle);
    }

    public void setdata(Bundle bundle){

        rowSeekbar.setProgress((Integer) bundle.getSerializable("com.example.navigation.matrixRows")-1);
        colSeekbar.setProgress((Integer) bundle.getSerializable("com.example.navigation.matrixCols")-1);

        rowIndex.setText(String.valueOf(rowSeekbar.getProgress()+1));
        colIndex.setText(String.valueOf(colSeekbar.getProgress()+1));
    }


    public void initialize(){
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        rowSeekbar=findViewById(R.id.rowsSeek);
        colSeekbar=findViewById(R.id.colsSeek);
        rowIndex=findViewById(R.id.rowIndex);
        colIndex=findViewById(R.id.colIndex);
        setSeekbars();
    }

    public void setSeekbars(){

        rowSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rowIndex.setText(String.valueOf(progress+1));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        colSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colIndex.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void SendData(View v){
        MainActivity.getInstance().initmatrixdimensions(Integer.parseInt(rowIndex.getText().toString()),Integer.parseInt(colIndex.getText().toString()));
        super.onBackPressed();
    }

    public void back() {
        super.onBackPressed();
    }
}
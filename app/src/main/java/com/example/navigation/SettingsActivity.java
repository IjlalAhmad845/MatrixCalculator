package com.example.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    MainActivity instance;

    SeekBar rowSeekbar,colSeekbar;
    TextView rowIndex,colIndex;
    RadioButton matrixStateRadio1,matrixStateRadio2;
    boolean stateRadio=true;

    boolean settingsStateChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsStateChanged = false;
        Initialize();

        Bundle bundle=getIntent().getExtras();

        setData(bundle);
    }

    /**==================================================== WRITING DATA IN SETTINGS ACTIVITY ========================================**/
    public void setData(Bundle bundle){

        rowSeekbar.setProgress((Integer) bundle.getSerializable("com.example.navigation.matrixRows")-1);
        colSeekbar.setProgress((Integer) bundle.getSerializable("com.example.navigation.matrixCols")-1);

        rowIndex.setText(String.valueOf(rowSeekbar.getProgress()+1));
        colIndex.setText(String.valueOf(colSeekbar.getProgress()+1));

        matrixStateRadio1.setChecked((Boolean)bundle.getSerializable("com.example.navigation.matricesState"));
        matrixStateRadio2.setChecked(!(Boolean)bundle.getSerializable("com.example.navigation.matricesState"));
        stateRadio=(Boolean)bundle.getSerializable("com.example.navigation.matricesState");

    }


    /**========================================== IDENTITY MATRIX RADIO BUTTON STATE CONTROL ==================================================**/
    public void ControlMatrixStateRadio2() {
        if (rowIndex.getText().equals(colIndex.getText()))
            matrixStateRadio2.setEnabled(true);
        else {
            matrixStateRadio2.setEnabled(false);
            matrixStateRadio1.setChecked(true);
        }
    }

    /**========================================================== SEEKBARS CONTROL METHOD ==================================================**/
    public void setSeekbars(){
        rowSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rowIndex.setText(String.valueOf(progress+1));

                ControlMatrixStateRadio2();
                settingsStateChanged =true;

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

                ControlMatrixStateRadio2();
                settingsStateChanged =true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**======================================== INITIALIZING WIDGETS IN SETTINGS ACTIVITY =============================================**/
    public void Initialize(){
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!settingsStateChanged)
                    settingsStateChanged=(stateRadio==matrixStateRadio2.isChecked());

                if(settingsStateChanged)
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("")
                        .setMessage("Discard Changes")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                back();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                else back();

            }
        });

        rowSeekbar=findViewById(R.id.rowsSeek);
        colSeekbar=findViewById(R.id.colsSeek);
        rowIndex=findViewById(R.id.rowIndex);
        colIndex=findViewById(R.id.colIndex);
        setSeekbars();

        matrixStateRadio1=findViewById(R.id.matrixStateRadio1);
        matrixStateRadio2=findViewById(R.id.matrixStateRadio2);

    }

    /**==================================================== SENDING DATA TO HOME ACTIVITY ========================================**/
    public void SendData(View v){

        if(!settingsStateChanged)
           settingsStateChanged=(stateRadio==matrixStateRadio2.isChecked());

        if(settingsStateChanged)
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("")
                    .setMessage("Save Changes")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.getInstance().initMatrixSettings(
                                    Integer.parseInt(rowIndex.getText().toString()),
                                    Integer.parseInt(colIndex.getText().toString()),
                                    matrixStateRadio1.isChecked());
                            back();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        else back();

    }

    public void back() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(!settingsStateChanged)
            settingsStateChanged=(stateRadio==matrixStateRadio2.isChecked());

        if(settingsStateChanged)
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("")
                    .setMessage("Discard Changes")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            back();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        else
        super.onBackPressed();
    }
}
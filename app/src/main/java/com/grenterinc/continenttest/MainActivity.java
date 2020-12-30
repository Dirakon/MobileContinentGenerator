package com.grenterinc.continenttest;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static com.grenterinc.continenttest.Cell.LAND;
import static com.grenterinc.continenttest.Cell.WATER;

public class MainActivity extends AppCompatActivity {
    private final long DELAY = TimeUnit.MILLISECONDS.toMillis(100);
    int defminContinentSeeds = 3, defmaxContinentSeeds = 15; //Whatever.
    float defminContinentProcent = 0.005f, defmaxContinentProcent = 0.1f; //0...1, but obvously even 0.5 is TOO HUGE.
    int defminIslandSeeds = 1, defmaxIslandSeeds = 75; //Whatever.
    float defminIslandProcent = 0.00001f, defmaxIslandProcent = 0.0005f; //0...1, but obvously even 0.5 is TOO HUGE.
    int defminLakeSeeds = 25, defmaxLakeSeeds = 100; //Whatever. But lakes are really important for world to look cool, better have at least some.
    float defminLakeProcent = 0.0001f, defmaxLakeProcent = 0.001f; //0...1, but obvously even 0.5 is TOO HUGE.
    private int sizeY, sizeX;

    static public void Debug(String txt) {
        Log.d("MAN", txt);
    }

    private void startTask() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                DrawManager.Draw(sizeY, sizeX);
                startTask();
            }

        }, DELAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.d("MAN", Integer.toString(size.x) + '-' + size.y);
        int sizeX = size.x <= 0 ? 1000 : size.x;
        int sizeY = size.y <= 0 ? 1000 : size.y;


        setContentView(R.layout.activity_main);
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                ArrayList<Generator.GenerationInniter> sampleData = new ArrayList<Generator.GenerationInniter>();
                sampleData.add(new Generator.GenerationInniter(defminContinentSeeds, defmaxContinentSeeds, defminContinentProcent, defmaxContinentProcent, WATER, LAND)); //WATER -> LAND (continents)
                sampleData.add(new Generator.GenerationInniter(defminLakeSeeds, defmaxLakeSeeds, defminLakeProcent, defmaxLakeProcent, LAND, WATER)); //LAND->WATER (lakes)
                sampleData.add(new Generator.GenerationInniter(defminIslandSeeds, defmaxIslandSeeds, defminIslandProcent, defmaxIslandProcent, WATER, LAND)); //WATER->LAND (islands)
                Generator.Generate(sizeY, sizeX, sampleData);
                startTask();
                // Code here executes on main thread after user presses button
            }
        });

        Log.d("MAN", "man,activated");
    }
}

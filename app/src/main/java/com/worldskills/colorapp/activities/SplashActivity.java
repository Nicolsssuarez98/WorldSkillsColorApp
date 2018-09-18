package com.worldskills.colorapp.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.worldskills.colorapp.R;
import com.worldskills.colorapp.activities.Home;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para quitar la barra de noticacions///
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ////////////////////////////////////
        setContentView(R.layout.activity_splash);

        logo=findViewById(R.id.logo_app);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iniciarHome();
            }
        },2000);
    }

    /*Metodo que se encarga de iniciar la actividad home*/
    public void iniciarHome(){
        Intent intent=new Intent(this,Home.class);

       Pair pair=new Pair(logo,"logo_app_t");

        ActivityOptions op=ActivityOptions.makeSceneTransitionAnimation(this,pair);

        startActivity(intent,op.toBundle());
        finish();
    }
}


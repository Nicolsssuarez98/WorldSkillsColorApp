package com.worldskills.colorapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.worldskills.colorapp.R;

public class Settings extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para quitar la barra de noticacions///
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ////////////////////////////////////

        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.contenedor_prefenencias,new Pref()).commit();

        Animation aparecer= AnimationUtils.loadAnimation(this,R.anim.botones);
        aparecer.setFillAfter(true);

        LinearLayout layout=findViewById(R.id.contenedor_prefenencias);
        layout.startAnimation(aparecer);
    }
    public void startActivity(Intent intent){
        intent.putExtra(Home.MODO_PARTIDA,1);
        super.startActivity(intent);
        finish();
    }
    public void onBackPressed(){
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);
        finish();
    }

}

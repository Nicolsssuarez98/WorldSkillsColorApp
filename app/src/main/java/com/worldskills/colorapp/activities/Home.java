package com.worldskills.colorapp.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldskills.colorapp.R;
import com.worldskills.colorapp.db.DataBase;

public class Home extends AppCompatActivity {

    public static final String MODO_PARTIDA="MODOP";
    private int[] puntajesP=new int[4];
    private Animation apareceIzquierda, apareceDerecha, salirIzquierda, salirDerecha, aparecer, desaparecer;
    private TextView btonJugar, btonPuntajes, btonAjustes;
    private ImageView btonSalir, logo;
    private LinearLayout layoutBotones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para quitar la barra de noticacions///
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ////////////////////////////////////
        setContentView(R.layout.activity_home);

        findViews();
        loadAnimaciones();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutBotones.setVisibility(View.VISIBLE);
                animaEntrada();
            }
        },300);

    }


    /*Metodo para encontrar las vista del layout e instanciarlas*/
    public void findViews(){
        btonJugar=findViewById(R.id.inicio_bton_jugar);
        btonPuntajes=findViewById(R.id.inicio_bton_puntaje);
        btonAjustes=findViewById(R.id.inicio_bton_ajustes);
        btonSalir=findViewById(R.id.inicio_bton_salir);
        logo=findViewById(R.id.logo_app);
        layoutBotones=findViewById(R.id.inicio_layout_botones);

    }


    /*Metodo para cargar las animaciones que seran usades despues*/
    public void loadAnimaciones(){
        apareceIzquierda= AnimationUtils.loadAnimation(this,R.anim.aparecer_desde_izquierda);
        apareceIzquierda.setFillAfter(true);

        salirDerecha= AnimationUtils.loadAnimation(this, R.anim.salir_hacia_derecha);
        salirDerecha.setFillAfter(true);

        apareceDerecha= AnimationUtils.loadAnimation(this, R.anim.aparecer_desde_derecha);
        apareceDerecha.setFillAfter(true);

        salirIzquierda= AnimationUtils.loadAnimation(this, R.anim.salir_hacia_izquierda);
        salirIzquierda.setFillAfter(true);

        aparecer=AnimationUtils.loadAnimation(this,R.anim.botones);
        aparecer.setFillAfter(true);

        desaparecer=AnimationUtils.loadAnimation(this,R.anim.desaparecer);
        desaparecer.setFillAfter(true);

    }


    public void animaEntrada(){
        btonJugar.startAnimation(apareceIzquierda);
        btonSalir.startAnimation(apareceDerecha);
        btonAjustes.startAnimation(apareceIzquierda);
        btonPuntajes.startAnimation(apareceDerecha);
    }
    public void animaSalida(){
        btonJugar.startAnimation(salirIzquierda);
        btonSalir.startAnimation(salirDerecha);
        btonAjustes.startAnimation(salirIzquierda);
        btonPuntajes.startAnimation(salirDerecha);
        logo.startAnimation(desaparecer);

    }
    /*Metodo con el fin de obtener el boton precionado en el inicio y de igual manera realizar su
    * respeciva accion*/
    public void botonesInicio(View v){

        switch (v.getId()){
            case R.id.inicio_bton_jugar:

                verificaAnimacion(0);
                break;
            case R.id.inicio_bton_puntaje:

                verificaAnimacion(1);
                break;
            case R.id.inicio_bton_ajustes:
                verificaAnimacion(2);
                break;
            case R.id.inicio_bton_salir:
                verificaAnimacion(3);

                break;

        }
    }

    /*Metodo que espera a que la animacion termine para seguir con la accion escogida*/
    public void verificaAnimacion(final int accio){
        animaSalida();
        salirDerecha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    switch (accio){
                        case 0:
                            Intent intent=new Intent(getApplicationContext(), Partida.class);
                            intent.putExtra(MODO_PARTIDA,0);
                            startActivity(intent);
                            break;
                        case 1:
                            abrePuntajes();
                            break;
                        case 2:
                            Intent inten=new Intent(getApplicationContext(), Settings.class);
                            startActivity(inten);

                            break;
                        case 3:
                            finish();
                            break;
                    }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    /*Metodo para abrir el dialog de puntajes mostrando extraidad de la bas e de datos*/
    public void abrePuntajes(){
        Dialog dialogPuntajes=new Dialog(this);
        dialogPuntajes.setContentView(R.layout.dialog_puntajes);
        dialogPuntajes.setCanceledOnTouchOutside(false);
        dialogPuntajes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView[] puntajes=new TextView[4];
        puntajes[0]=dialogPuntajes.findViewById(R.id.pun1);
        puntajes[1]=dialogPuntajes.findViewById(R.id.pun2);
        puntajes[2]=dialogPuntajes.findViewById(R.id.pun3);
        puntajes[3]=dialogPuntajes.findViewById(R.id.pun4);


        for (int i=0;i<puntajesP.length; i++)puntajesP[0]=0;
        cargarDatosPuntajes();
        for (int i=0;i<puntajes.length;i++)puntajes[i].setText(puntajesP[i]+"");


        LinearLayout layoutPuntajes=dialogPuntajes.findViewById(R.id.layout_puntajes);
        layoutPuntajes.startAnimation(aparecer);
        dialogPuntajes.show();

        dialogPuntajes.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                animaEntrada();
                logo.startAnimation(aparecer);
            }
        });
    }

    /*Metodo para cargar los puntajes de la base de datos*/
    public void cargarDatosPuntajes(){
        DataBase db=new DataBase(this);
        Cursor cursor=db.load();
        if (cursor==null)return;

        if (cursor.moveToFirst()){
            int i=0;
            do{
                puntajesP[i]=cursor.getInt(0);
                i++;
            }while (cursor.moveToNext());
        }
    }

}

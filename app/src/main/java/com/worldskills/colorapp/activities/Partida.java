package com.worldskills.colorapp.activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.worldskills.colorapp.R;
import com.worldskills.colorapp.db.DataBase;

import java.util.Random;

public class Partida extends AppCompatActivity {

    private static int COLORES_BOTONES[]= {R.drawable.anim_amarillo, R.drawable.anim_azul, R.drawable.anim_rojo, R.drawable.anim_verde};
    private static String COLORES_PALABRAS[]= {"AMARILLO", "AZUL", "ROJO", "VERDE"};
    private int[] numeros;

    private TextView viewPalabras, viewCorrecto, viewModoJuego, viewPalabraMostrar;
    private Button[] botonesJuego;

    private Animation aparecer, animPalabra;

    private int correctas, palabras, intentos, pausa, modo, posicionP, colorTinta;
    private boolean tipoP;
    private long duracionPalabra, tiempoPartida, tiemporeal;

    CountDownTimer timerPalabra, timerPartida;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para quitar la barra de noticacions///
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ////////////////////////////////////
        setContentView(R.layout.activity_partida);



        duracionPalabra=3000;
        intentos=3;
        correctas=0;
        palabras=0;
        tipoP=true;
        pausa=0;

        Bundle datos= getIntent().getExtras();

        if(datos!=null){
            modo=datos.getInt(Home.MODO_PARTIDA);
        }


        findViews();
        loadAnimation();

    }
    /*Metodo para encontrar las vistas del layout*/
    public void findViews(){
        viewPalabras=findViewById(R.id.partida_t1);
        viewModoJuego=findViewById(R.id.partida_t2);
        viewCorrecto=findViewById(R.id.partida_t3);
        viewPalabraMostrar=findViewById(R.id.palabra);


        botonesJuego=new Button[4];
        botonesJuego[0]=findViewById(R.id.b1);
        botonesJuego[1]=findViewById(R.id.b2);
        botonesJuego[2]=findViewById(R.id.b3);
        botonesJuego[3]=findViewById(R.id.b4);

    }


/*Metodo para instanciar las animacion que se usaran en la partida*/
    public void loadAnimation(){
        aparecer=AnimationUtils.loadAnimation(this,R.anim.botones);

        animPalabra=AnimationUtils.loadAnimation(this,R.anim.anim_palabra);
        animPalabra.setFillAfter(true);
    }


    /*Medoto para sacar una posicion al azar*/
    public int azar(){
        return new Random().nextInt(COLORES_BOTONES.length);
    }
    public void azarColorTinta(){
        colorTinta=azar();
        switch (colorTinta){
            case 0:
                viewPalabraMostrar.setTextColor(getResources().getColor(R.color.amarillo,null));

                break;
            case 1:
                viewPalabraMostrar.setTextColor(getResources().getColor(R.color.azul,null));
                break;
            case 2:
                viewPalabraMostrar.setTextColor(getResources().getColor(R.color.rojo,null));
                break;
            case 3:
                viewPalabraMostrar.setTextColor(getResources().getColor(R.color.verde,null));
                break;
        }
    }
    public void botonesAzar(){
        numeros=new int[4];
        for (int i=0; i<numeros.length;i++)numeros[i]=-1;

        int base=0;

        do{
            int posicion=azar();
            if (numeros[posicion]==-1){
                numeros[posicion]=base;
                base++;
            }

        }while (base<4);

        for (int i=0; i<botonesJuego.length; i++){
            botonesJuego[i].setBackgroundResource(COLORES_BOTONES[numeros[i]]);
            botonesJuego[i].startAnimation(aparecer);
        }

        clickBotones();


    }
    /*metodo que pone a la escucha a todos los botones de los colores*/
    public void clickBotones(){
        botonesJuego[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comparar(numeros[0]);
            }
        });

        botonesJuego[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comparar(numeros[1]);
            }
        });

        botonesJuego[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comparar(numeros[2]);
            }
        });

        botonesJuego[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comparar(numeros[3]);
            }
        });
    }


    /*metodo que recibe el boton precionado y compara si la accion es correcta*/
    public void comparar(int botonPresionado){
        if (botonPresionado==colorTinta){
            correctas++;
        }else{
            intentos--;
        }

        try{
            timerPalabra.cancel();
        }catch (Exception e){}

        if (tipoP){
            if (intentos==0)finalPartida();
            else cargaJuego();
        }else{
            cargaJuego();
        }



    }

    /*Metodo que se encarga de cargar el juego */
    public void cargaJuego(){
        azarColorTinta();
        viewPalabraMostrar.setText(COLORES_PALABRAS[azar()]);
        viewPalabraMostrar.startAnimation(animPalabra);


        botonesAzar();
        timerPalabraD();

        palabras++;

        viewPalabras.setText("PALABRAS\n"+palabras);
        viewCorrecto.setText("CORRECTAS\n"+correctas);

        if (tipoP)viewModoJuego.setText("INTENTOS\n"+intentos);
    }

    /*Metodo para terminar la partida luego de verificar*/
    public void finalPartida(){

       cancelarTiempos();
        Toast.makeText(this, "Final partida", Toast.LENGTH_SHORT).show();
        abreDialogFinal();
        if (modo==0)guardarDatos();
        if (!tipoP);


    }

    /*Metodo para cancelar los tiempo*/
    public void cancelarTiempos(){

        try{
            timerPalabra.cancel();
        }catch (Exception e){}

        try{
            timerPartida.cancel();
        }catch (Exception e){}
    }
    /*Metodo para guardar datos cuando la partida ya finalizo, solo cuando es partida por defecto*/
    public void guardarDatos(){
        DataBase db=new DataBase(this);
        db.save(correctas);
    }

    public void timerPalabraD(){
        timerPalabra=new CountDownTimer(duracionPalabra,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                comparar(10);//a
            }
        }.start();
    }



    /*metodo para iniciar la partida con el tiempo*/
    public void timerPartidaD(long timepoPaa){
        timerPartida=new CountDownTimer(timepoPaa,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiemporeal=millisUntilFinished;

                if (tiemporeal/1000<9)viewModoJuego.setText("TIEMPO\n0"+tiemporeal/1000);
                else viewModoJuego.setText("TIEMPO\n"+tiemporeal/1000);

            }

            @Override
            public void onFinish() {
                finalPartida();
            }
        }.start();
    }
    public void onResume(){
        super.onResume();
        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);


        if (modo==1){
            String modd=datos.getString("modoP","INTENTOS");

            if (modd.equalsIgnoreCase("TIEMPO")){
                tipoP=false;
                tiempoPartida=Long.parseLong(datos.getString("tiempoP","10000"));
                timerPartidaD(tiempoPartida);
            }

            duracionPalabra=Long.parseLong(datos.getString("duracionP","3000"));
            intentos=Integer.parseInt(datos.getString("intentos","3"));

        }

        cargaJuego();
    }



    /*Metodo que abre el dialogo final que muestra el resultado de la partida*/
    public void abreDialogFinal(){
        Dialog dialogFinal=new Dialog(this);
        dialogFinal.setContentView(R.layout.dialog_final_partida);
        dialogFinal.setCanceledOnTouchOutside(false);
        dialogFinal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView viewCorrect, viewIncorrec;
        Button btonHome, btonReplay;

        viewPalabraMostrar.setText("");

        viewCorrect=dialogFinal.findViewById(R.id.final_correct);
        viewIncorrec=dialogFinal.findViewById(R.id.final_incorrect);
        btonHome=dialogFinal.findViewById(R.id.final_bton_home);
        btonReplay=dialogFinal.findViewById(R.id.final_bton_replay);

        viewCorrect.setText(correctas+"");
        viewIncorrec.setText(palabras-correctas+"");

        btonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
            }
        });

        btonReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Partida.class);
                intent.putExtra(Home.MODO_PARTIDA,modo);
                startActivity(intent);
            }
        });

        dialogFinal.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent=new Intent(getApplicationContext(),Home.class);
                intent.putExtra(Home.MODO_PARTIDA,modo);
                startActivity(intent);
            }
        });

        LinearLayout layout=dialogFinal.findViewById(R.id.layout_final_partida);
        layout.startAnimation(aparecer);
        dialogFinal.show();
    }

    public void onBackPressed(){
        cancelarTiempos();
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);
        finish();

    }
    /*Metodo encargado de pausar el juego*/
    public void pausarJuego(View v){
        final Dialog dialogPause=new Dialog(this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialogPause.setContentView(R.layout.dialog_pausa);
        dialogPause.setCanceledOnTouchOutside(false);
        dialogPause.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cancelarTiempos();

        if (pausa<2){

        Button btonReanudar=dialogPause.findViewById(R.id.dialog_pausa_boton);
        btonReanudar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPause.dismiss();
                cargaJuego();
                if (!tipoP)timerPartidaD(tiemporeal);


            }
        });
            LinearLayout layout=dialogPause.findViewById(R.id.layout_dialog_pausa);
            layout.startAnimation(aparecer);
            dialogPause.show();

        }else{
            Toast.makeText(this, "Solo puedes pausar dos veces", Toast.LENGTH_SHORT).show();
        }
            pausa++;




    }






}

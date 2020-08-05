package com.example.user.cantinacr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;


public class DatosDeConfirmacion extends AppCompatActivity {

    private static final int SOLICITUD_PERMISO_CALL_PHONE = 1;
    TextView txt_nombre, txt_apellido, txt_personas, txt_telefono, txt_fecha;
    Button bt_call1, bt_call2;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_de_confirmacion);

        txt_nombre = (TextView) findViewById(R.id.txt_name);
        txt_apellido = (TextView) findViewById(R.id.txt_surname);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        txt_personas = (TextView) findViewById(R.id.txt_personas);
        txt_telefono = (TextView) findViewById(R.id.txt_tel);
        bt_call1 = (Button) findViewById(R.id.call1);
        bt_call2 = (Button) findViewById(R.id.Call2);

        String nombre = getIntent().getStringExtra("nombre");
        String apellido = getIntent().getStringExtra("apellido");
        String fecha = getIntent().getStringExtra("fecha");
        String personas = getIntent().getStringExtra("personas");
        String telefono = getIntent().getStringExtra("telefono");

        txt_nombre.setText(nombre);
        txt_apellido.setText(apellido);
        txt_fecha.setText("Fecha " + fecha);
        txt_personas.setText("Mesa para " + personas + " personas");
        txt_telefono.setText("Teléfono " + telefono);

    }

    public void llamada33 (View view){

        intent = new Intent(Intent.ACTION_CALL,  Uri.parse("tel:3364447833"));

        if (ActivityCompat.checkSelfPermission(DatosDeConfirmacion.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
            Toast.makeText(this, "Permiso concedido",Toast.LENGTH_SHORT);
        }else{
            LLAMAR();
        }

    }

    public void llamada87 (View view){

        intent = new Intent(Intent.ACTION_CALL,  Uri.parse("tel:3364294487"));

        if (ActivityCompat.checkSelfPermission(DatosDeConfirmacion.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
            Toast.makeText(this, "Permiso concedido",Toast.LENGTH_SHORT);
        }else{
            LLAMAR();
        }

    }

    public void LLAMAR (){
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, SOLICITUD_PERMISO_CALL_PHONE);
    Toast.makeText(this, "Pedimos el permiso", Toast.LENGTH_SHORT);
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if (requestCode == SOLICITUD_PERMISO_CALL_PHONE){
            startActivity(intent);
            Toast.makeText(this, "Permiso" , Toast.LENGTH_SHORT);

        }else{
            Toast.makeText(this, "no permiso",Toast.LENGTH_SHORT);
        }
    }

    public void Salir (View view){
        finishAffinity();
    }
}

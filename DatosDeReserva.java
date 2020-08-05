package com.example.user.cantinacr;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.io.*;
import java.io.IOException;
import java.lang.String;
import com.google.gson.Gson;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.*;

public class DatosDeReserva extends AppCompatActivity {

    private EditText et_nombre, et_apellido, et_personas, et_tel;
    private int dia, mes, anio;
    private TextView tv_fecha;

    String vDireccionWS = "http://172.16.7.124:60580/api/";
    String vUsuario = "prueba";
    String vPassword = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_de_reserva);

        et_nombre = (EditText) findViewById(R.id.et_name);
        et_apellido = (EditText) findViewById(R.id.et_surname);
        et_personas = (EditText) findViewById(R.id.et_cant);
        et_tel = (EditText) findViewById(R.id.et_phone);
        tv_fecha = (TextView) findViewById(R.id.tv_fecha);
    }

    //METODO DATPICKER PARA SELECCIONAR LA FECHA
    public void fecha(View view) {
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        anio = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_fecha.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            }
        }, anio, mes, dia);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    public void agregarReserva(View view) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Cantina objCantina = new Cantina();

        objCantina.nombre = et_nombre.getText().toString();
        objCantina.apellido = et_apellido.getText().toString();
        objCantina.fecha = tv_fecha.getText().toString();
        objCantina.cantPersonas = et_personas.getText().toString();
        objCantina.telefono = et_tel.getText().toString();

        if (!objCantina.nombre.isEmpty() && !objCantina.apellido.isEmpty() && !objCantina.fecha.isEmpty() && !objCantina.cantPersonas.isEmpty() && !objCantina.telefono.isEmpty()) {

            //Enviar los datos en el siguiente activity
            Intent confir = new Intent(this, DatosDeConfirmacion.class); //pasa al segundo activity
            confir.putExtra("nombre", et_nombre.getText().toString());
            confir.putExtra("apellido", et_apellido.getText().toString());
            confir.putExtra("fecha", tv_fecha.getText().toString());
            confir.putExtra("personas", et_personas.getText().toString());
            confir.putExtra("telefono", et_tel.getText().toString());

            //transforma la reserva en json
            String JsonCantina = new Gson().toJson(objCantina);

            //llama al json pasandole la reserva
            String resultado = WSPost("cantina/enviar", JsonCantina);
            resultado = resultado.replace("\"","");

          //  Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();


            if(resultado.startsWith("ERROR:"))
            {
               Toast.makeText(this,resultado.replace("ERROR:",""), Toast.LENGTH_SHORT).show();
            }
            else
            {
                et_nombre.setText("");
                et_apellido.setText("");
                tv_fecha.setText("");
                et_personas.setText("");
                et_tel.setText("");

               Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show(); //"Registro exitoso"
                startActivity(confir);
            }

        } else {
           Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private String WSPost(String pMetodo, String pJSON) throws IOException {

        String vResultado = "OK";
        try {
            URL url = new URL(vDireccionWS + pMetodo);// "login/authenticate");

            HttpURLConnection vSolicitud = (HttpURLConnection) url.openConnection();

            vSolicitud.setRequestMethod("POST");
            vSolicitud.setRequestProperty("Content-Type", "application/json");
            vSolicitud.setRequestProperty("Accept", "application/json");
            vSolicitud.setDoOutput(true);
            vSolicitud.setConnectTimeout(30000);

            LoginRequest tempVar = new LoginRequest();
            tempVar.Username = vUsuario;
            tempVar.Password = vPassword;
            String vToken = GetToken(tempVar);
            //Toast.makeText(this,vToken,Toast.LENGTH_LONG).show();

            vSolicitud.setRequestProperty("Authorization", "Bearer " + vToken.replace("\"", ""));

            if ((vToken.startsWith("ERROR:"))) {
                return vToken.substring(6);
            }

            OutputStream os = vSolicitud.getOutputStream();
            byte[] input = pJSON.getBytes("utf-8");
            os.write(input, 0, input.length);

            Reader in = new BufferedReader(new InputStreamReader(vSolicitud.getInputStream()));//, "UTF-8")
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0; )
                sb.append((char) c);
            vResultado = sb.toString();
        }catch (Exception e) {
            vResultado = "ERROR:" + e.getMessage();
        }
        return vResultado;
    }

    //metodo para crear la conexion al webservice
    private String GetToken(LoginRequest usuario) throws MalformedURLException {
        String vResultado = "";
        try {
            String usuarioJson = new Gson().toJson(usuario);

            URL url = new URL(vDireccionWS + "login/authenticate");

            //creamos la solicitud con el link del WS apuntando al metodo authenticate

            HttpURLConnection vSolicitud = (HttpURLConnection) url.openConnection();
            vSolicitud.setRequestMethod("POST");
            vSolicitud.setRequestProperty("Content-Type", "application/json");
            vSolicitud.setDoOutput(true);

            OutputStream os = vSolicitud.getOutputStream();
            byte[] input = usuarioJson.getBytes("utf-8");
            os.write(input, 0, input.length);

            try(BufferedReader br = new BufferedReader(new InputStreamReader(vSolicitud.getInputStream(), "utf-8"))){
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                vResultado=  response.toString();
            }
        }
        catch (Exception e) {
           vResultado = "ERROR:" + e.getMessage();
        }

        return vResultado;
    }

    public class Cantina {

        private int idCantina;

        public final int getIdCantina() {
            return idCantina;
        }

        public final void setIdCantina(int value) {
            idCantina = value;
        }

        private String nombre;

        public final String getNombre() {
            return nombre;
        }

        public final void setNombre(String value) {
            nombre = value;
        }

        private String apellido;

        public final String getApellido() {
            return apellido;
        }

        public final void setApellido(String value) {
            apellido = value;
        }

        private String cantPersonas;

        public final String getCantPersonas() {
            return cantPersonas;
        }

        public final void setCantPersonas(String value) {
            cantPersonas = value;
        }

        private String fecha;

        public final String getFecha() {
            return fecha;
        }

        public final void setFecha(String value) {
            fecha = value;
        }

        private String telefono;

        public final String getTelefono() {
            return telefono;
        }

        public final void setTelefono(String value) {
            telefono = value;
        }


    }

    public class LoginRequest {
        String Username;

        public final String getUsername() {
            return Username;
        }

        public final void setUsername(String value) {
            Username = value;
        }

        String Password;

        public final String getPassword() {
            return Password;
        }

        public final void setPassword(String value) {
            Password = value;
        }
    }

    public class Parametro {
        public String Nombre;
        public String Valor;
    }

    public void Cancelar(View view) {
        finishAffinity();
    }
}
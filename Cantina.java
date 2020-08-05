package com.example.user.cantinacr;

public class Cantina {
        String idCantina;
        String nombre;
        String apellido;
        int cantPersonas;
        String fecha;
        String telefono;

    public void setIdCantina( String _id ) {
        idCantina = _id;
    }
    public String getIdCantina( ) {
        return idCantina;
    }

    public void setNombre( String _nombre ) {
        nombre = _nombre;
    }
    public String getNombre( ) {
        return nombre;
    }

    public void setApellido( String _apellido) {
        apellido = _apellido;
    }
    public String getApellido( ) {
        return apellido;
    }

    public void setCantPersonas( int _cantPersonas) {
        cantPersonas = _cantPersonas;
    }
    public int getCantPersonas( ) {
        return cantPersonas;
    }

    public void setFecha( String _fecha ) {
        fecha = _fecha;
    }
    public String getFecha( ) {
        return fecha;
    }

    public void setTelefono( String _telefono) {
        telefono = _telefono;
    }
    public String getTelefono( ) {
        return telefono;
    }
}


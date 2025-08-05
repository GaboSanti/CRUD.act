package org.example.crud;

public class Usuario {
    private static String correo;

    public static void setCorreo(String correo) {
        correo = correo;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void limpiarSesion() {
        correo = null;
    }
}

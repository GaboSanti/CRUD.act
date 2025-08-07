package org.example.crud;


public class Usuarios {
    //atributos que contendra cada usuario
    private String nombre;
    private String correoPersonal;
    private String contrasena;

    // Constructor para crear un nuevo usuario
    public Usuarios(String nombre, String correoPersonal, String contrasena) {
        this.nombre = nombre;
        this.correoPersonal = correoPersonal;
        this.contrasena = contrasena;
    }


    // Getters de cada atributo
    public String getNombre() { return nombre; }
    public String getCorreoPersonal() { return correoPersonal; }
    public String getContrasena() { return contrasena; }

    // Setters de cada atributo
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreoPersonal(String correoPersonal) { this.correoPersonal = correoPersonal; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

}

package org.example.crud;

import org.example.crud.UsuarioBD;
import org.example.crud.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {


    public boolean validarCredenciales(String correo, String contrasena) {
        String query = "SELECT COUNT(*) FROM crud WHERE correo = ? AND contrasena = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar credenciales: " + e.getMessage());//imprime el mensaje de error
        }

        return false;
    }


    public UsuarioBD obtenerUsuarioPorCorreo(String correo) {
        String query = "SELECT correo, nombre, contrasena FROM crud WHERE correo = ?";
        try (Connection conn = ConexionBD.getConnection(); // Usa ConexionBDRegistro
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UsuarioBD(
                        rs.getString("correo"),
                        rs.getString("nombre"),
                        rs.getString("contrasena")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por correo : " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarUsuario(UsuarioBD usuario) {
        // Solo actualiza correo_personal y numero_telefono
        String query = "UPDATE usuarios SET correo = ? WHERE correo= ?";
        // Especifica las columnas a actualizar y sus nuevos valores
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getContrasena());
            stmt.setString(3, usuario.getCorreo());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}
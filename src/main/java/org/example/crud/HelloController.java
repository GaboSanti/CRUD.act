package org.example.crud;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class HelloController {
    @FXML
    private TextField txt1;
    @FXML
    private TextField txt2;


    @FXML
    protected void irConsulta(ActionEvent event) throws IOException{
        String correoIngresado = txt1.getText();
        String passwordIngresado = txt2.getText();

        if (compararDatos(correoIngresado, passwordIngresado)) {
            Usuario.setCorreo(correoIngresado);
            cambioVentana("Horario.fxml", event, "Horario");
        }else {
        }

    }

    @FXML
    protected void irRegistro(ActionEvent event) throws IOException{
        cambioVentana("Registro.fxml", event, "Registro");
    }


    private boolean compararDatos(String correo, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void cambioVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.show();
    }

}
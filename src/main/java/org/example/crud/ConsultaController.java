package org.example.crud;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.crud.UsuarioBD;
import org.example.crud.User;
import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ConsultaController implements Initializable  {

        @FXML private Label lblNombre;
        @FXML private Label lblCorreo;
        @FXML private Label lblContrasena;
        @FXML private TextField contrasenaField;

        private UsuarioBD usuariosActual;
        private User user;
        String correoSesion ="gabo@gmail.com";                //Usuario.getCorreo();

        @Override
        public void initialize(URL url, ResourceBundle rb) {
                user= new User();
                cargarDatosUsuarios();
        }

        @FXML
        public void onModificar() {
                contrasenaField.setText(lblContrasena.getText());
                contrasenaField.setVisible(true);
                lblContrasena.setVisible(false);
                contrasenaField.requestFocus();

                contrasenaField.setLayoutX(lblContrasena.getLayoutX());
                contrasenaField.setLayoutY(lblContrasena.getLayoutY());
        }

        @FXML
        public void onGuardar(){
                if (usuariosActual == null) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay datos de usuario para guardar.");
                        return;
                }

                boolean cambiosRealizados = false;
                if (contrasenaField.isVisible()) {
                        String nuevoCorreo = contrasenaField.getText().trim();
                        if (!nuevoCorreo.equals(usuariosActual.getCorreo())) {
                                usuariosActual.setCorreo(nuevoCorreo);
                                cambiosRealizados = true;
                        }
                }

                if (cambiosRealizados) {
                        if (user.actualizarUsuario(usuariosActual)) {
                                mostrarAlerta(Alert.AlertType.INFORMATION, "Modificación Exitosa", "Datos modificados correctamente.");
                                contrasenaField.setText(usuariosActual.getCorreo()); // lblCorreo_Personal ahora muestra el Correo Personal
                        } else {
                                mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "Hubo un problema al actualizar los datos en la base de datos.");
                        }
                } else {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Sin Cambios", "No se realizaron cambios en los datos de contacto.");
                }
                contrasenaField.setVisible(false);
                lblContrasena.setVisible(true);
}


        private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
                Alert alerta = new Alert(tipo);
                alerta.setTitle(titulo);
                alerta.setHeaderText(null);
                alerta.setContentText(mensaje);
                alerta.showAndWait();
        }

        private void cargarDatosUsuarios() {//Verifica si hay un correo válido
                if (correoSesion != null && !correoSesion.isEmpty()) {//Si lo hay, usa usuarioBD.obtenerUsuarioPorCorreoInstitucional() para recuperar los datos del usuario de la base de datos
                        usuariosActual = user.obtenerUsuarioPorCorreo(correoSesion);
                        if (usuariosActual != null) {
                                // Aquí es donde asignas los valores a los  Labels
                                lblNombre.setText(usuariosActual.getNombre());

                                lblCorreo.setText(usuariosActual.getCorreo());
                                lblContrasena.setText(usuariosActual.getContrasena());

                        } else {
                                mostrarAlerta(Alert.AlertType.ERROR, "Error de Datos", "No se encontraron los datos del usuario logueado. Revise el correo de sesión o la conexión a la base de datos.");
                                try {
                                        cambiarVentana("hello-view.fxml", null, "Iniciar Sesión");
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                } else {
                        mostrarAlerta(Alert.AlertType.ERROR, "Error de Sesión", "No hay usuario logueado. Redirigiendo a inicio de sesión.");
                        try {
                                cambiarVentana("hello-view.fxml", null, "Iniciar Sesión");
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle(titulo);
                stage.show();
        }

}

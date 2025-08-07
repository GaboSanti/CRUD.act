package org.example.crud;

import org.example.crud.UsuarioBD;
import org.example.crud.User;
import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;


public class ConsultaController {

        @FXML private Label lblId;
        @FXML private Label lblNombre;
        @FXML private Label lblCorreo;
        @FXML private Label lblContrasena;
        @FXML private TextField contrasenaField;

        private UsuarioBD usuariosActual;
        private User user;
        String correoSesion = Usuario.getCorreo();

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





}

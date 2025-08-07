package org.example.crud;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Registro implements Initializable {
    @FXML
    private TextField txtNombre;
    @FXML private TextField txtCorreo2;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private Button btnTogglePassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private Label lblPasswordHint;
    @FXML private Label lblConfirmarPasswordHint;
    @FXML private Button btn_registrar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_registrar.setOnAction(event -> validarFormulario());

        btnTogglePassword.setOnAction(e -> togglePasswordVisibility());
        txtPassword.textProperty().bindBidirectional(txtPasswordVisible.textProperty());

        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> validarSeguridadPassword(newVal));
        txtPasswordVisible.textProperty().addListener((obs, oldVal, newVal) -> validarSeguridadPassword(newVal));

        txtConfirmarPassword.textProperty().addListener((obs, oldVal, newVal) -> validarCoincidenciaPassword());
    }

    private void togglePasswordVisibility() {
        boolean visible = txtPasswordVisible.isVisible();
        txtPasswordVisible.setVisible(!visible);
        txtPasswordVisible.setManaged(!visible);
        txtPassword.setVisible(visible);
        txtPassword.setManaged(visible);
    }

    private void validarFormulario() {
        if (txtNombre.getText().isEmpty() || txtCorreo2.getText().isEmpty() ||
                txtPassword.getText().isEmpty() || txtConfirmarPassword.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todos los campos.");
            return;
        }

        if (!esPasswordSegura(txtPassword.getText())) {
            marcarError(txtPassword, true);
            mostrarAlerta(Alert.AlertType.ERROR, "Contraseña insegura", "Debe tener mayúscula, minúscula, número y símbolo.");
            return;
        }

        if (!txtPassword.getText().equals(txtConfirmarPassword.getText())) {
            marcarError(txtConfirmarPassword, true);
            lblConfirmarPasswordHint.setText("Las contraseñas no coinciden.");
            mostrarAlerta(Alert.AlertType.ERROR, "Contraseña", "Las contraseñas no coinciden.");
            return;
        }

        Usuarios nuevoUsuario = new Usuarios(
                txtNombre.getText(),
                txtCorreo2.getText(),
                txtPassword.getText()
        );

        if (registrarUsuario(nuevoUsuario)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "¡Usuario registrado correctamente!");
            limpiarCampos();
        }
    }

    private boolean registrarUsuario(Usuarios crud) {
        String sql = "INSERT INTO crud (nombre, correo_personal, contrasena) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, crud.getNombre());
            pstmt.setString(2, crud.getCorreoPersonal());
            pstmt.setString(3, crud.getContrasena());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                mostrarAlerta(Alert.AlertType.ERROR, "Correo duplicado", "Ese correo personal ya está registrado.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error al registrar.");
            }
            return false;
        }
    }

    private void validarSeguridadPassword(String password) {
        if (esPasswordSegura(password)) {
            lblPasswordHint.setText("");
            marcarError(txtPassword, false);
        } else {
            lblPasswordHint.setText("Mínimo 8 caracteres, mayúscula, minúscula, número y símbolo.");
            marcarError(txtPassword, true);
        }
    }

    private void validarCoincidenciaPassword() {
        if (!txtConfirmarPassword.getText().equals(txtPassword.getText())) {
            lblConfirmarPasswordHint.setText("Las contraseñas no coinciden.");
            marcarError(txtConfirmarPassword, true);
        } else {
            lblConfirmarPasswordHint.setText("");
            marcarError(txtConfirmarPassword, false);
        }
    }

    private boolean esPasswordSegura(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$");
    }

    private void marcarError(Control campo, boolean error) {
        campo.setStyle(error ? "-fx-border-color: red; -fx-border-width: 2;" : "");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtCorreo2.clear();
        txtPassword.clear();
        txtPasswordVisible.clear();
        txtConfirmarPassword.clear();
    }

}

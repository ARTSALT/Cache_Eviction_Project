package com.br.controller;

import java.io.IOException;

import com.br.App;
import com.br.connections.ConnectionProxy;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {
    
    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    public static Text error;
    
    @FXML
    private void login() throws IOException{
        String nome = username.getText();
        String senha = password.getText();

        ConnectionProxy conection = new ConnectionProxy(App.enderecoProxy[0],
                Integer.parseInt(App.enderecoProxy[1]), nome, senha);
    }
}

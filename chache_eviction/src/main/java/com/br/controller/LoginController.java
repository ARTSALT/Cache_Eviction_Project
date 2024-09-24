package com.br.controller;

import java.io.IOException;

import com.br.App;
import com.br.entity.Database;
import com.br.entity.ServiceOrder;

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
    Text error;
    
    @FXML
    private void login() throws IOException{
        String nome = username.getText();
        String senha = password.getText();

        if ((nome.equals("Artur") && senha.equals("12345")) || (nome.equals("Paulo") && senha.equals("56789"))) {

            for (int i = 1; i <= 60; i++) {
                Database.insert(new ServiceOrder(String.valueOf(i), String.valueOf(i)));
            }

            App.telaTabela();
        } else {
            error.setVisible(true);
        }
    }
}

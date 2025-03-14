package com.br.controller;

import java.io.IOException;

import com.br.App;
import com.br.connections.ConnectionProxy;
import com.br.entity.ServiceOrder;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CadastroController {

    @FXML TextField name;
    @FXML TextArea description;
    @FXML Text error;

    @FXML
    private void create() throws IOException, ClassNotFoundException {
        String nome = name.getText();
        String descricao = description.getText();
        if((!nome.isEmpty() && !nome.equals("⠀")) && (!descricao.isEmpty() && !descricao.equals("⠀"))) {
            ConnectionProxy.sendRequest("insert", new ServiceOrder(nome, descricao));
            App.telaTabela();
        } else {
            error.setVisible(true);
        }
    }

    @FXML
    private void delete() throws IOException {
        App.telaTabela();
    }
    
}

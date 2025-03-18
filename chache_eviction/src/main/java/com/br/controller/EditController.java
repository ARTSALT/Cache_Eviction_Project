package com.br.controller;

import java.io.IOException;

import com.br.App;
import com.br.connections.ConnectionProxy;
import com.br.entity.ServiceOrder;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditController {

    @FXML public TextField name;
    @FXML public TextArea description;

    static ServiceOrder original;

    public void initialize(ServiceOrder e) {
        name.setText(e.getName());
        description.setText(e.getDescription());

        original = e;
    }

    @FXML
    private void edit() throws Exception {
        String nome = name.getText();
        String descricao = description.getText();

        if ((!nome.isEmpty() && !nome.equals("⠀")) && (!descricao.isEmpty() && !descricao.equals("⠀"))) {
            ConnectionProxy.sendRequest("update", new ServiceOrder(original.getCode(), nome, descricao));
            App.telaTabela();
        }
    }

    @FXML
    private void delete() throws IOException {
        App.telaTabela();
    }
}

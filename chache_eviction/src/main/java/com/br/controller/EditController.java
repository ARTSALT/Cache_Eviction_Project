package com.br.controller;

import java.io.IOException;

import com.br.App;
import com.br.entity.Database;
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
    private void edit() throws IOException {
        String nome = name.getText();
        String descricao = description.getText();

        if ((!nome.isEmpty() && !nome.equals("⠀")) && (!descricao.isEmpty() && !descricao.equals("⠀"))) {
            ServiceOrder e = new ServiceOrder(original.getCode(), nome, descricao);
            Database.subistitute(e);
        }

        App.telaTabela();
    }

    @FXML
    private void delete() throws IOException {
        Database.subistitute(original);
        App.telaTabela();
    }
}

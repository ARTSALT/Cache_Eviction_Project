package com.br.controller;

import java.io.IOException;

import com.br.App;
import com.br.entity.ServiceOrder;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ViewController {
    
    @FXML TextField name;
    @FXML TextField time;
    @FXML TextArea description;
    int ret;

    public void initialize(ServiceOrder e, int ret) {
        name.setText(e.getName());
        time.setText(e.getSolTime());
        description.setText(e.getDescription());
        this.ret = ret;
    }

    @FXML
    public void goBack() throws IOException {
        if (ret == 1) {
            App.telaTabela();
        } else {
            App.telaCache();
        }
        
    }
}

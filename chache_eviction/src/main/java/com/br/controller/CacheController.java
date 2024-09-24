package com.br.controller;

import java.io.IOException;
import java.util.List;

import com.br.App;
import com.br.entity.Database;
import com.br.entity.ServiceOrder;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CacheController {

    @FXML TableView<ServiceOrder> table = new TableView<>();
    @FXML TableColumn<ServiceOrder, Integer> code = new TableColumn<>("Código");
    @FXML TableColumn<ServiceOrder, String> name = new TableColumn<>("Nome");
    @FXML TableColumn<ServiceOrder, String> description = new TableColumn<>("Descrição");
    @FXML TableColumn<ServiceOrder, String> solTime = new TableColumn<>("Hora da Solicitação");
    @FXML TextField searchBar;

    @FXML
    public void initialize() {
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        solTime.setCellValueFactory(new PropertyValueFactory<>("solTime"));

        loadAll();
    }

    
    private void updateTable(List<ServiceOrder> e) {
        table.getItems().setAll(e);
    }

    private void updateTable(ServiceOrder e) {
        table.getItems().setAll(e);
    }

    private void loadAll() {
        updateTable(Database.cache.returnCache());
    }

    @FXML
    private void search() {
        if (searchBar.getText().isEmpty()) {
            loadAll();
        } else {
            updateTable(Database.searchCache(new ServiceOrder(Integer.parseInt(searchBar.getText()))));
        }
    }

    @FXML
    private void view() throws IOException{
        ServiceOrder e = table.getSelectionModel().getSelectedItem();

        if (e != null) {
            App.telaView(e, 2);
        } 
    }

    @FXML
    private void toTable() throws IOException {
        App.telaTabela();
    }
}

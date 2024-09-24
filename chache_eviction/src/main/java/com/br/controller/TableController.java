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
import javafx.scene.text.Text;

public class TableController {

    @FXML TableView<ServiceOrder> table = new TableView<>();
    @FXML TableColumn<ServiceOrder, Integer> code = new TableColumn<>("Código");
    @FXML TableColumn<ServiceOrder, String> name = new TableColumn<>("Nome");
    @FXML TableColumn<ServiceOrder, String> description = new TableColumn<>("Descrição");
    @FXML TableColumn<ServiceOrder, String> solTime = new TableColumn<>("Hora da Solicitação");

    @FXML TextField searchBar;
    @FXML Text number;
    
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
        number.setText(Database.database.elements() + " Ordens");
    }

    private void updateTable(ServiceOrder e) {
        table.getItems().setAll(e);
    }

    private void loadAll() {
        updateTable(Database.returnAll());
    }

    @FXML
    private void search() {
        if (searchBar.getText().isEmpty()) {
            loadAll();
        } else {
            updateTable(Database.search(new ServiceOrder(Integer.parseInt(searchBar.getText()))));
        }
    }

    @FXML
    private void toCreate() throws IOException {
        App.telaCadastro();
    }

    @FXML
    private void delete() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Database.remove(table.getSelectionModel().getSelectedItem());

            loadAll();
        }
    }

    @FXML
    private void edit() throws IOException {
        ServiceOrder e = table.getSelectionModel().getSelectedItem();

        if (e != null) 
            App.telaEditar(e);
    }

    @FXML
    private void view() throws IOException{
        ServiceOrder e = table.getSelectionModel().getSelectedItem();

        if (e != null) {
            App.telaView(e, 1);
        } 
    }

    @FXML
    private void toCache() throws IOException {
        App.telaCache();
    }
}

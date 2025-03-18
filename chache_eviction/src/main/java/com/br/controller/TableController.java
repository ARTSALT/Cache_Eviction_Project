package com.br.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import com.br.App;
import com.br.connections.ConnectionProxy;
import com.br.entity.Packet;
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
    public void initialize() throws Exception {
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        solTime.setCellValueFactory(new PropertyValueFactory<>("solTime"));

        loadAll();
    }

    private void updateTable(List<ServiceOrder> e) {
        table.getItems().setAll(e);
        number.setText(e.size() + " Ordens");
    }

    private void updateTable(ServiceOrder e) {
        table.getItems().setAll(e);
    }

    private void loadAll() throws Exception {
        try {
            Packet<?> list = ConnectionProxy.sendRequest("getAll", null);
            if (list.getPurpose().equals("1")) {
                updateTable((List<ServiceOrder>) list.getContent());
            }
        } /*catch (SocketException e) {
            System.out.println("Tentando Reconexão");
            App.tryConnection(InetAddress.getLocalHost().getHostAddress(), 12345);
            ConnectionProxy connection = new ConnectionProxy(App.enderecoProxy[0],
                    Integer.parseInt(App.enderecoProxy[1]), "admin", "admin");
        }*/ catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void search() throws Exception {
        if (searchBar.getText().isEmpty()) {
            loadAll();
        } else {
            try {
                Packet<?> result = ConnectionProxy.sendRequest("search",
                        new ServiceOrder(Integer.parseInt(searchBar.getText()), "", ""));
                if (result.getPurpose().equals("1")) {
                    updateTable((ServiceOrder) result.getContent());
                }
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void toCreate() throws IOException {
        App.telaCadastro();
    }

    @FXML
    private void delete() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                Packet<?> result = ConnectionProxy.sendRequest("remove", table.getSelectionModel().getSelectedItem());
                if (result.getPurpose().equals("1")) {
                    loadAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println(e.getMessage());
            }
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
            App.telaView(e);
        } 
    }
}

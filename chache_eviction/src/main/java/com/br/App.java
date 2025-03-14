package com.br;

import java.io.IOException;
import java.net.InetAddress;

import com.br.controller.EditController;
import com.br.controller.ViewController;
import com.br.entity.ServiceOrder;

import com.br.connections.ConnectionLocal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Stage stage;
    public volatile static boolean running = true;
    public static String[] enderecoProxy = new String[2];

    public void setStage(Stage stage)
    {
        App.stage = stage;
    }

    public Stage getStage()
    {return stage;}

    public static void main (String[] args) throws Exception {
        tryConnection(InetAddress.getLocalHost().getHostAddress(), 12345);

        launchApp(args);
        stage.setOnCloseRequest(running -> {
            App.running = false;
        });
    }

    public static void tryConnection(String ip, int porta) throws Exception {
        ConnectionLocal connection = new ConnectionLocal(ip, porta);
        enderecoProxy = connection.connect();
    }

    public static void launchApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        setStage(stage);
        stage.setResizable(false);
        stage.setTitle("SO Manager");
        stage.show();
        telaLogin();
    }

    public static void telaLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("TelaLogin.fxml"));
        Parent root = loader.load(); 

        Scene primary = new Scene(root);
        stage.setScene(primary);
        stage.centerOnScreen();
    }

    public static void telaTabela() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("TelaTabela.fxml"));
        Parent root = loader.load(); 
    
        Scene telaTabela = new Scene(root);
        stage.setScene(telaTabela);
    }

    public static void telaCadastro() throws IOException{
        FXMLLoader loader = new FXMLLoader(App.class.getResource("TelaCadastro.fxml"));
        Parent root = loader.load();

        Scene telaCadastro = new Scene(root);
        stage.setScene(telaCadastro);
    }

    @SuppressWarnings("exports")
    public static void telaEditar(ServiceOrder e) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("TelaEdit.fxml"));
        Parent root = loader.load();

        EditController controller = loader.getController();
        controller.initialize(e);

        Scene telaEditar = new Scene(root);
        stage.setScene(telaEditar);
    }

    @SuppressWarnings("exports")
    public static void telaView(ServiceOrder e) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("TelaView.fxml"));
        Parent root = loader.load();

        ViewController controller = loader.getController();
        controller.initialize(e);

        Scene telaView = new Scene(root);
        stage.setScene(telaView);
    }

    public static void telaCache() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("TelaCache.fxml"));
        Parent root = loader.load();
        
        Scene telaCache = new Scene(root);
        stage.setScene(telaCache);
    }
}
package com.br;

import java.io.IOException;

import com.br.controller.EditController;
import com.br.controller.ViewController;
import com.br.entity.ServiceOrder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage stage;

    public void setStage(Stage stage)
    {
        App.stage = stage;
    }

    public Stage getStage()
    {return stage;}

    public static void main (String[] args)
    {
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
    public static void telaView(ServiceOrder e, int ret) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("TelaView.fxml"));
        Parent root = loader.load();

        ViewController controller = loader.getController();
        controller.initialize(e, ret);

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
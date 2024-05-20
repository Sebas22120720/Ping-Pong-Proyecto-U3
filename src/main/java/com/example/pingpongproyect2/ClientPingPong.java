package com.example.pingpongproyect2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientPingPong extends Application {
    private static final int puerto = 9999;
    private static final String servidor = "localhost";
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Label mensajeEspera;

    @Override
    public void start(Stage primaryStage){
        mensajeEspera = new Label("Esperando jugador 2...");
        StackPane root = new StackPane(mensajeEspera);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Jugadores Ping Pongo");
        primaryStage.setScene(scene);
        primaryStage.show();

        conectarServidor();
        new Thread(this::escucharServidor).start();

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.UP){
                enviarMensaje("Arriba");
            }else if(event.getCode() == KeyCode.DOWN){
                enviarMensaje("Abajo");
            }
        });
    }

    public void conectarServidor(){
        try{
            socket = new Socket(servidor, puerto);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void escucharServidor(){
        try{
            while(true){
                String mensaje = bufferedReader.readLine();
                if(mensaje != null){
                    Platform.runLater(() -> mensajeEspera.setText(mensaje));
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void enviarMensaje(String mensaje){
        printWriter.println(mensaje);
    }

    public static void main(String[] args){
        launch(args);
    }
}

package com.example.pingpongproyect2;

import javafx.scene.canvas.Canvas;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerPingPong{
    private static final int puerto = 9999;
    private static final int width = 800;
    private static final int height = 600;
    private static final int jugadoresMax = 2;
    private static final String mensajeEspera = "Esperando Jugadores...";
    private static final String mensajeComienzo = "Comiencen!!!";
    private static final String mensajeVictoria = "GANASTE";
    private static final String mensajeDerrota = "PERDISTE";
    private static final String movimientoArriba = "Arriba";
    private static final String movimientoAbajo = "Abajo";
    private List<PrintWriter> jugadores = new ArrayList<>();
    private PingPongGame pingPongGame;
    public static void main(String[] args) {
        new ServerPingPong().iniciar();
    }

    private void iniciar(){
        Canvas canvas = new Canvas(width, height);
        pingPongGame = new PingPongGame(canvas.getGraphicsContext2D(), canvas);
        try{
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor abierto, esperando jugadores...");

            while(true){
                if(jugadores.size() < jugadoresMax){
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Jugador " + clientSocket + " conectado");
                    PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    jugadores.add(printWriter);

                    if(jugadores.size() == jugadoresMax){
                        enviarMensajeAll(mensajeComienzo);
                    }else{
                        printWriter.println(mensajeEspera);
                    }

                    ManejadorCliente manejadorCliente = new ManejadorCliente(clientSocket);
                    new Thread(manejadorCliente).start();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void enviarMensajeAll(String mensaje){
        for(PrintWriter jugador : jugadores){
            jugador.println(mensaje);
        }
    }

    private class ManejadorCliente implements Runnable{
        private Socket clienSocket;
        private BufferedReader bufferedReader;

        public ManejadorCliente(Socket socket) throws IOException{
            this.clienSocket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run(){
            try{
                while(true){
                    String movimiento = bufferedReader.readLine();
                    if(movimiento != null){
                        if(movimiento.equals(movimientoArriba) || movimiento.equals(movimientoAbajo)){
                            pingPongGame.receiveMovement(movimiento);
                            enviarMensajeAll(movimiento);
                        }
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally {
                try{
                    clienSocket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}

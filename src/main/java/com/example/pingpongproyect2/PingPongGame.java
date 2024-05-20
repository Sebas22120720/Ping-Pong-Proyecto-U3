package com.example.pingpongproyect2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PingPongGame{
        private static final int width = 800;
        private static final int height = 600;
        private static final int PLAYER_HEIGHT = 100;
        private static final int PLAYER_WIDTH = 15;
        private static final double BALL_R = 15;
        private int ballYSpeed = 1;
        private int ballXSpeed = 1;
        private double playerOneYPos = height / 2;
        private double playerTwoYPos = height / 2;
        private double ballXPos = width / 2;
        private double ballYPos = height / 2;
        private int scoreP1 = 0;
        private int scoreP2 = 0;
        private boolean gameStarted;
        private int playerOneXPos = 0;
        private double playerTwoXPos = width - PLAYER_WIDTH;
        private GraphicsContext gc;
        private Canvas canvas;

        public PingPongGame(GraphicsContext gc, Canvas canvas){
            this.gc = gc;
            this.canvas = canvas;
        }

        private void updateGameState() {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, width, height);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(25));

            if (gameStarted) {
                ballXPos += ballXSpeed;
                ballYPos += ballYSpeed;
                if (ballXPos < width - width / 4) {
                    playerTwoYPos = ballYPos - PLAYER_HEIGHT / 2;
                } else {
                    playerTwoYPos = ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ? playerTwoYPos += 1 : playerTwoYPos - 1;
                }
                gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);

            } else {
                gc.setStroke(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.strokeText("Click", width / 2, height / 2);
                ballXPos = width / 2;
                ballYPos = height / 2;
                ballXSpeed = Math.random() > 0.5 ? 1 : -1;
                ballYSpeed = Math.random() > 0.5 ? 1 : -1;
            }
            if (ballYPos > height || ballYPos < 0) ballYSpeed *= -1;
            if (ballXPos < playerOneXPos - PLAYER_WIDTH) {
                scoreP2++;
                gameStarted = false;
            }
            if (ballXPos > playerTwoXPos + PLAYER_WIDTH) {
                scoreP1++;
                gameStarted = false;
            }

            gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t" + scoreP2, width / 2, 100);
            gc.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
            gc.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        public void receiveMovement(String movimiento) {
            if(movimiento.equals("Arriba")){
                playerOneYPos -= 10;
            }else if(movimiento.equals("Abajo")){
                playerOneYPos += 10;
            }
            playerOneYPos = Math.max(0, Math.min(height - PLAYER_HEIGHT, playerOneYPos));
            gc.clearRect(0, 0, width, height);
            updateGameState();
        }
}

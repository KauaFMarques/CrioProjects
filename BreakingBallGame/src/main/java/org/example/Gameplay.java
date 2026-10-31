package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    // ============================================================
    // 1. VARIÁVEIS DO JOGO (o "estado" do jogo)
    // ============================================================
    private boolean play = false;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballDirX = -1;
    private int ballDirY = -2;

    private Timer timer;
    private int delay = 8;

    // NOVO: mapa de tijolos, pontuação e contador
    private MapGenerator map;
    private int score = 0;
    private int totalBricks = 8 * 12;

    // ============================================================
    // 2. CONSTRUTOR (preparação)
    // ============================================================
    public Gameplay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Cria o mapa ANTES de iniciar o timer
        map = new MapGenerator(8, 12);

        timer = new Timer(delay, this);
        timer.start();
    }

    // ============================================================
    // 3. DESENHO (paintComponent)
    // ============================================================
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 3a. Fundo preto
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        // 3b. Bordas amarelas
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // 3c. NOVO: desenha os tijolos (precisa do cast para Graphics2D)
        map.draw((Graphics2D) g);

        // 3d. NOVO: pontuação na tela
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 550, 30);

        // 3e. Raquete verde
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        // 3f. Bola amarela
        g.setColor(Color.YELLOW);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // 3g. NOVO: mensagem de vitória quando acabar os tijolos
        if (totalBricks == 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Você venceu!", 250, 300);
        }

        g.dispose();
    }

    // ============================================================
    // 4. TECLADO (KeyListener)
    // ============================================================
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
    }

    private void moveRight() {
        play = true;
        playerX += 20;
        repaint();
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    // ============================================================
    // 5. LOOP DO JOGO (ActionListener / Timer)
    // ============================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {

            // 5a. Move a bola
            ballPosX += ballDirX;
            ballPosY += ballDirY;

            // 5b. Colisão com as bordas
            if (ballPosX < 0) {
                ballDirX = -ballDirX;
            }
            if (ballPosY < 0) {
                ballDirY = -ballDirY;
            }
            if (ballPosX > 670) {
                ballDirX = -ballDirX;
            }
            if (ballPosY > 570) {
                ballDirY = -ballDirY;
            }

            // 5c. Colisão com a raquete
            if (new Rectangle(ballPosX, ballPosY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            // 5d. NOVO: colisão com os tijolos
            Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {

                    if (map.map[i][j] > 0) {

                        int brickX = 80 + j * map.brickWidth;
                        int brickY = 50 + i * map.brickHeight;
                        Rectangle brickRect = new Rectangle(brickX, brickY,
                                map.brickWidth, map.brickHeight);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            // descobre de que lado bateu
                            if (ballPosX + 19 <= brickRect.x
                                    || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }

                            break;
                        }
                    }
                }
            }

            // 5e. Redesenha a tela
            repaint();
        }
    }
}
package org.example;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // Cria a janela (game terminal)
        JFrame terminal = new JFrame();
        terminal.setSize(800, 600);
        terminal.setTitle("Breaking Ball Game");
        terminal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Cria o painel do jogo e adiciona à janela
        Gameplay game = new Gameplay();
        terminal.add(game);

        // Torna a janela visível
        terminal.setVisible(true);

        // Garante que o painel receba os eventos de teclado
        game.requestFocusInWindow();
    }
}
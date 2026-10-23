package org.example;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame terminal = new JFrame();
        terminal.setSize(800, 600);
        terminal.setTitle("Breaking Ball Game");
        terminal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        terminal.getContentPane().setBackground(new Color(200, 30, 50));
        terminal.setVisible(true);

    }
}
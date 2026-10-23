package org.example;

import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        //System.out.printf("Hello and welcome!");

        /*for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
            }*/

        JLabel label = new JLabel("Hello word! I am a window Java.", JLabel.CENTER);
        label.setFont(new Font("Calibri",Font.BOLD, 40));
        label.setForeground(Color.white);

        //janela
        JFrame janela = new JFrame();
        janela.setSize(700,700);
        janela.getContentPane().setBackground(new Color(200,30,50));
        janela.add(label);
        janela.setVisible(true);

    }
}
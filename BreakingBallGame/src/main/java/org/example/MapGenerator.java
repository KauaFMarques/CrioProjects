package org.example;

import java.awt.*;

public class MapGenerator {
    public int[][] map;      // 1 = tijolo existe, 0 = destruído
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        map = new int[row][col];

        // preenche tudo com 1 (todos os tijolos existem no início)
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = 1;
            }
        }

        // calcula tamanho de cada tijolo para caber na tela (692 de largura útil)
        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    // Desenha todos os tijolos que ainda existem (valor 1)
    public void draw(Graphics2D g) {
        Color[] cores = { Color.RED, Color.RED, Color.ORANGE, Color.ORANGE,
                Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW };

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // escolhe a cor pela linha (se tiver mais de 8 linhas, usa amarelo)
                    g.setColor(i < cores.length ? cores[i] : Color.YELLOW);

                    // desenha o retângulo do tijolo
                    g.fillRect(80 + j * brickWidth + 1,
                            50 + i * brickHeight + 1,
                            brickWidth - 2,
                            brickHeight - 2);

                    // desenha a borda branca
                    g.setColor(Color.WHITE);
                    g.setStroke(new BasicStroke(3));
                    g.drawRect(80 + j * brickWidth + 1,
                            50 + i * brickHeight + 1,
                            brickWidth - 2,
                            brickHeight - 2);
                }
            }
        }
    }

    // Marca um tijolo como destruído
    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}

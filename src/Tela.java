
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tela extends JPanel implements ActionListener {

    private final int bordalargura = 500;
    private final int bordaAltura = 500;
    private final int todosPontos = 1000; // TODOS OS PONTOS POSSÍVEIS NO VISOR
    private final int posicaoAleatoria = 29;
    private final int velocidadeJogo = 104;
    private final int tamanhoPonto = 10; // Aqui estamos definindo que cada ponto do corpo da cobra e da mação possui o tamanho de 10 pixels
    private final int tamanhoMaca = 10; // Aqui estamos definindo que cada ponto do corpo da cobra e da mação possui o tamanho de 10 pixels

    private final int coordX[] = new int[todosPontos];
    private final int coordY[] = new int[todosPontos];

    private int corpo; // Cada ponto reresenta uma parte do corpo da cobra
    private int macaX;
    private int macaY;

    private boolean direcaoEsquerda = false;
    private boolean direcaDireita = true;
    private boolean direcaoCima = false;
    private boolean direcaoBaixo = false;
    private boolean noJogo = true;

    private Timer tempo;
    private Image ponto;
    private Image maca;  //Maçã
    private Image cabeca;

    
    public Tela() {
        
        apresentarTela();
    }
    
    private void apresentarTela() {
        
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        
        setPreferredSize(new Dimension(bordalargura, bordaAltura));
        carregarImagens();
        iniciarJogo();
    }
    
    private void carregarImagens() {

        ImageIcon iconePonto = new ImageIcon("ponto.png");
        ponto = iconePonto.getImage();

        ImageIcon iconeMaca = new ImageIcon("maca.png");
        maca = iconeMaca.getImage();

        ImageIcon iconeCabeca = new ImageIcon("cabeca.png");
        cabeca = iconeCabeca.getImage();
    }

    private void iniciarJogo() {

        corpo = 5; // Definindo o tamanho da cobra no início do jogo

        for (int z = 0; z < corpo; z++) { //Definindo a posição inicial da cobra;
            coordX[z] = 50 - z * 10;
            coordY[z] = 50;
        }
        
        localizacaoMaca();

        tempo = new Timer(velocidadeJogo, this);
        tempo.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (noJogo) {

            g.drawImage(maca, macaX, macaY, this);

            for (int z = 0; z < corpo; z++) {
                if (z == 0) {
                    g.drawImage(cabeca, coordX[z], coordY[z], this);
                } else {
                    g.drawImage(ponto, coordX[z], coordY[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            fimDoJogo(g);
        }        
    }

    private void fimDoJogo(Graphics g) {
        
        String msg = "Você bateu!";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (bordalargura - metr.stringWidth(msg)) / 2, bordaAltura / 2);
    }

    private void checarMaca() {

        if ((coordX[0] == macaX) && (coordY[0] == macaY)) {

            corpo++;
            localizacaoMaca();
        }
    }

    private void movimentar() {

        for (int z = corpo; z > 0; z--) {
            coordX[z] = coordX[(z - 1)];
            coordY[z] = coordY[(z - 1)];
        }

        if (direcaoEsquerda) {
            coordX[0] -= tamanhoPonto;
        }

        if (direcaDireita) {
            coordX[0] += tamanhoPonto;
        }

        if (direcaoCima) {
            coordY[0] -= tamanhoPonto;
        }

        if (direcaoBaixo) {
            coordY[0] += tamanhoPonto;
        }
    }

    private void checarColisao() {

        for (int z = corpo; z > 0; z--) {

            if ((z > 4) && (coordX[0] == coordX[z]) && (coordY[0] == coordY[z])) {
                noJogo = false;
            }
        }

        if (coordY[0] >= bordaAltura) {
            noJogo = false;
        }

        if (coordY[0] < 0) {
            noJogo = false;
        }

        if (coordX[0] >= bordalargura) {
            noJogo = false;
        }

        if (coordX[0] < 0) {
            noJogo = false;
        }
        
        if (!noJogo) {
            tempo.stop();
        }
    }

    private void localizacaoMaca() {

        int r = (int) (Math.random() * posicaoAleatoria);
        macaX = ((r*tamanhoMaca));

        r = (int) (Math.random() * posicaoAleatoria);
        macaY = ((r*tamanhoMaca));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (noJogo) {

            checarMaca();
            checarColisao();
            movimentar();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!direcaDireita)) {
                direcaoEsquerda = true;
                direcaoCima = false;
                direcaoBaixo = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!direcaoEsquerda)) {
                direcaDireita = true;
                direcaoCima = false;
                direcaoBaixo = false;
            }

            if ((key == KeyEvent.VK_UP) && (!direcaoBaixo)) {
                direcaoCima = true;
                direcaDireita = false;
                direcaoEsquerda = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!direcaoCima)) {
                direcaoBaixo = true;
                direcaDireita = false;
                direcaoEsquerda = false;
            }
        }
    }
}
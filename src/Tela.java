
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

    private final int bordalargura = 400;
    private final int bordaAltura = 400;
    private final int todosPontos = 1200; // TODOS OS PONTOS POSSÍVEIS NO VISOR
    private final int posicaoAleatoria = 39;
    private final int velocidadeJogo = 84;
    private final int tamanhoPonto = 10; // Aqui estamos definindo que cada ponto do corpo da cobra e da mação possui o
                                         // tamanho de 10 pixels
    private final int tamanhoMaca = 10; // Aqui estamos definindo que cada ponto do corpo da cobra e da mação possui o
                                        // tamanho de 10 pixels

    private final int coordX[] = new int[todosPontos];
    private final int coordY[] = new int[todosPontos];
    private final int pX[] = new int[15];
    private final int pY[] = new int[15];

    private int corpo; // Cada ponto reresenta uma parte do corpo da cobra
    private int macaX;
    private int macaY;
    private int pedraX;
    private int pedraY;
    private int vidas;
    private int contador;
    private int contadorMaca;

    private boolean direcaoEsquerda = false;
    private boolean direcaoDireita = true;
    private boolean direcaoCima = false;
    private boolean direcaoBaixo = false;
    private boolean noJogo = true;

    private Timer tempo;
    private Image ponto;
    private Image maca; // Maçã
    private Image cabeca;
    private Image pedra;

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
        posicaoObstaculos();
    }

    private void carregarImagens() {

        ImageIcon iconePonto = new ImageIcon("ponto.png");
        ponto = iconePonto.getImage();

        ImageIcon iconeMaca = new ImageIcon("maca.png");
        maca = iconeMaca.getImage();

        ImageIcon iconeCabeca = new ImageIcon("cabeca.png");
        cabeca = iconeCabeca.getImage();

        ImageIcon iconePedra = new ImageIcon("pedra.png");
        pedra = iconePedra.getImage();
    }

    private void iniciarJogo() {

        corpo = 5; // Definindo o tamanho da cobra no início do jogo
        vidas = 5; // Cobra começa com 5 vidas.
        contador = 0;
        contadorMaca = 0;
        posicaoInicial();
        posicaoObstaculos();
        localizacaoMaca();

        tempo = new Timer(velocidadeJogo, this);
        tempo.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void posicaoInicial() {
        for (int z = 0; z < corpo; z++) { // Definindo a posição inicial da cobra;
            coordX[z] = 50 - (z * 10);
            coordY[z] = 50;
        }
        if (direcaoEsquerda) {
            direcaoEsquerda = false;
            direcaoDireita = true;
        }
        posicaoObstaculos();
    }

    private void doDrawing(Graphics g) {

        if (noJogo && contadorMaca != 15) {

            g.drawImage(maca, macaX, macaY, this);

            for (int i = 0; i < pX.length; i++) {
                g.drawImage(pedra, pX[i], pY[i], this);

            }

            for (int z = 0; z < corpo; z++) {
                if (z == 0) {
                    g.drawImage(cabeca, coordX[z], coordY[z], this);
                } else {
                    g.drawImage(ponto, coordX[z], coordY[z], this);
                }
            }

            if (contador == 3) {
                vidas++;
                contador = 0;
            }

            ganhaVidas(g);
            mostraMaca(g);

            Toolkit.getDefaultToolkit().sync();

        } else if (contadorMaca == 15) {

            ganhaJogo(g);

        } else if (!noJogo || vidas == 0) {

            fimDoJogo(g);
        }
    }

    private void ganhaJogo(Graphics g) {

        String msg = "Parabéns! Você venceu!";
        Font small = new Font("Helvetica", Font.BOLD, 22);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (bordalargura - metr.stringWidth(msg)) / 2, bordaAltura / 2);
    }

    private void ganhaVidas(Graphics g) {

        String msg = "Vidas: " + vidas;
        Font small = new Font("Helvetica", Font.BOLD, 14);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, 15, 385);
    }

    private void mostraMaca(Graphics g) {

        String msg = "Maçãs: " + contadorMaca;
        Font small = new Font("Helvetica", Font.BOLD, 14);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, 100, 385);
    }

    private void fimDoJogo(Graphics g) {

        String msg = "Suas tentativas acabaram!";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (bordalargura - metr.stringWidth(msg)) / 2, bordaAltura / 2);
    }

    private void posicaoObstaculos() {
        for (int i = 0; i < pX.length; i++) {
            pedraX = ((int) (Math.random() *  posicaoAleatoria))*tamanhoPonto;
            pX[i] = pedraX;
        }
        for (int i = 0; i < pY.length; i++) {
            pedraY = ((int) (Math.random() * posicaoAleatoria))* tamanhoPonto;
            pY[i] = pedraY;
        }
    }

    private void checarMaca() {

        if ((coordX[0] == macaX) && (coordY[0] == macaY)) {

            corpo++;
            contador++;
            contadorMaca++;
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

        if (direcaoDireita) {
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
                vidas--;
                posicaoInicial();

            }
            if (vidas == 0) {
                noJogo = false;
            }
        }

      

        if (coordY[0] >= bordaAltura) {
            coordY[0] = 0;
        }

        if (coordY[0] < 0) {
            coordY[0] = bordaAltura;
        }

        if (coordX[0] >= bordalargura) {
            coordX[0] = 0;
        }

        if (coordX[0] < 0) {
            coordX[0] = bordalargura;
        }

        if (!noJogo) {
            tempo.stop();
        }
    }

    private void localizacaoMaca() {

        int r = (int) (Math.random() * posicaoAleatoria);
        macaX = ((r * tamanhoMaca));
        r = (int) (Math.random() * posicaoAleatoria);
        macaY = ((r * tamanhoMaca));
        checarMacaObstaculo();
    }

    private void checarMacaObstaculo() {
        for (int i = 0; i < 58; i++) {
            if ((pX[0] == macaX) && (pX[0] == macaY)) {
                localizacaoMaca();
            }
        }
    }

    private void checarBatida(){
        for (int i = 0; i < pX.length; i++) {
            if ((coordY[0] == pY[i]) && (coordX[0] == pX[i])) {
                vidas--;
                posicaoInicial();

            }
            if (vidas == 0) {
                noJogo = false;
            };
            }
        }

    

    @Override
    public void actionPerformed(ActionEvent e) {

        if (noJogo) {

            checarMaca();
            checarColisao();
            movimentar();
            checarMacaObstaculo();
            checarBatida();

        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!direcaoDireita)) {
                direcaoEsquerda = true;
                direcaoCima = false;
                direcaoBaixo = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!direcaoEsquerda)) {
                direcaoDireita = true;
                direcaoCima = false;
                direcaoBaixo = false;
            }

            if ((key == KeyEvent.VK_UP) && (!direcaoBaixo)) {
                direcaoCima = true;
                direcaoDireita = false;
                direcaoEsquerda = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!direcaoCima)) {
                direcaoBaixo = true;
                direcaoDireita = false;
                direcaoEsquerda = false;
            }
        }
    }
}
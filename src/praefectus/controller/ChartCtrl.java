package praefectus.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import praefectus.view.Chart;

/**
 *
 * @author soares
 */
public class ChartCtrl extends JPanel {

    private int altura;
    private int espaco;
    private int margem;
    private LinkedList<String> rotulos;
    private HashMap<Integer, Integer> valores;
    private String tituloGrafico;
    private String tituloHorizontal;
    private String tituloVertical;
    private Chart tela;
    private PrincipalCtrl controlePrincipal;
    private Graphics2D g2d;
    private double zoom;
    private Stroke linhaPadrao;
    private BasicStroke linhaGrade;

    /**
     *
     * @param controlePrincipal Controller da tela principal
     * @param tituloHorizontal Titulo do eixo X
     * @param tituloVertical Titulo do eixo Y
     * @param valores Valores a serem plotados no gráfico de linha
     * @param rotulos Rótulos do eixo X
     */
    public ChartCtrl(final PrincipalCtrl controlePrincipal, String tituloGrafico, String tituloHorizontal, String tituloVertical, HashMap<Integer, Integer> valores, LinkedList<String> rotulos) {

        this.setTitulos(tituloHorizontal, tituloVertical);
        this.valores = valores;
        this.rotulos = rotulos;

        this.altura = 200;
        this.espaco = 30;
        this.margem = 30;
        this.zoom = 1;

        this.controlePrincipal = controlePrincipal;
        this.tela = new Chart();
        this.controlePrincipal.newInternalFrame(tela);
        this.tela.setTitle(tituloGrafico);
        this.setSize(controlePrincipal.getTelaPincipal().getSize());
        this.tela.getjPanelPlotagem().add(this);

        this.tela.getjSliderZoom().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setScale((double) 2 * tela.getjSliderZoom().getValue() / 100);
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g.create();
        super.paintComponent(g2d);

        // seta tipos de linhas
        linhaPadrao = g2d.getStroke();
        linhaGrade = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, new float[]{6.0f}, 0.0f);

        g2d.scale(zoom, zoom);
        this.setBackground(Color.WHITE);

        desenharEixos();

        desenharTitulos();


        g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));

        double maximo = valores.size() > 0 ? Collections.max(valores.values()) : 10;
        double media = maximo / rotulos.size();

        for (int i = 1; i <= rotulos.size(); i++) {

            desenharRotuloEixoX(i);

            desenharLinhasVerticaisGrade(i);


            // Esforço restante esperado
            g2d.setColor(Color.BLUE);
            double y2 = altura + margem - (maximo - i * media) / maximo * altura;
            double x1 = (i - 1) * espaco + margem;
            double y1 = altura + margem - (maximo - (i - 1) * media) / maximo * altura;
            double x2 = i * espaco + 30;
            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            g2d.fillOval(i * espaco + 27, ((int) y2) - 3, 7, 7);
        }

        // pontos
        double yAnterior = 0;
        double xAnterior = 0;
        int valor;
        for (int i : valores.keySet()) {
            g2d.setColor(Color.DARK_GRAY);
            if (i == 0) {
                valor = valores.get(i);
            } else {
                valor = (int) (maximo - valores.get(i));
            }
            double x = i * espaco + 30;
            double y = altura + margem - valor / maximo * altura;

            // ponto
            g2d.fillOval(i * espaco + 27, ((int) y) - 3, 7, 7);

            // rotulo ponto
            g2d.drawString("" + valor, (int) (i * espaco + margem),
                    (int) y - 5);

            if (i > 0) {
                // linha entre o ponto anterior e o atual
                g2d.drawLine((int) xAnterior, (int) yAnterior, (int) x, (int) y);
            }

            xAnterior = x;
            yAnterior = y;
        }

    }

    public void setTitulos(String tituloHor, String tituloVer) {
        this.tituloGrafico = tituloGrafico;
        tituloHorizontal = tituloHor;
        tituloVertical = tituloVer;
    }

    public void setScale(double fator) {
        zoom = fator;
        repaint();
    }

    private void desenharEixos() {
        int tamanhoLinha = rotulos.size() * espaco + 50;

        // linha ghorizontal
        g2d.setColor(Color.BLACK);
        g2d.drawLine(margem, altura + margem, tamanhoLinha, altura + margem);
        // linha vertical
        g2d.drawLine(margem, margem, margem, altura + margem);
    }

    private void desenharTitulos() {
        // titulo horizontal
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        g2d.setColor(Color.BLACK);
        g2d.drawString(tituloHorizontal, margem, altura + margem + 45);

        // titulo vertical
        g2d.rotate(Math.PI / -2.0);
        g2d.drawString(tituloVertical, -30 - altura, margem - 5);
        g2d.rotate(Math.PI / 2.0);
    }

    private void desenharRotuloEixoX(int i) {
        // rotulos eixo horizontal
        g2d.setColor(Color.BLACK);
        g2d.drawString(rotulos.get(i - 1), (i - 1) * espaco + 50, altura + 55);

        // marcadores eixo horizontal
        g2d.drawLine(i * espaco + margem, altura + margem, i * espaco + margem, altura + 40);
    }

    private void desenharLinhasVerticaisGrade(int i) {
        // linhas verticais grade
        g2d.setStroke(linhaGrade);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(i * espaco + margem, margem, i * espaco + margem, altura + margem);
        g2d.setStroke(linhaPadrao);
    }
}

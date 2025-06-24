import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class PanelArbol extends JPanel {
    private ArbolRB arbol;
    private Map<NodoRB, Point> posiciones;

    public PanelArbol(ArbolRB arbol) {
        this.arbol = arbol;
        setPreferredSize(new Dimension(1000, 500));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        posiciones = new HashMap<>();

        if (arbol != null && arbol.getRaiz() != null) {
            calcularPosiciones(arbol.getRaiz(), getWidth() / 2, 30, getWidth() / 4);

            // Primero dibujar líneas
            for (Map.Entry<NodoRB, Point> entry : posiciones.entrySet()) {
                NodoRB nodo = entry.getKey();
                Point punto = entry.getValue();

                if (nodo.izquierdo != null) {
                    Point hijoIzq = posiciones.get(nodo.izquierdo);
                    g.drawLine(punto.x, punto.y, hijoIzq.x, hijoIzq.y);
                }

                if (nodo.derecho != null) {
                    Point hijoDer = posiciones.get(nodo.derecho);
                    g.drawLine(punto.x, punto.y, hijoDer.x, hijoDer.y);
                }
            }

            // Después dibujar nodos
            for (Map.Entry<NodoRB, Point> entry : posiciones.entrySet()) {
                NodoRB nodo = entry.getKey();
                Point punto = entry.getValue();

                g.setColor(nodo.color == NodoRB.RED ? Color.RED : Color.BLACK);
                g.fillOval(punto.x - 15, punto.y - 15, 30, 30);
                g.setColor(Color.WHITE);
                g.drawString(String.valueOf(nodo.clave), punto.x - 8, punto.y + 5);
            }
        }
    }

    private void calcularPosiciones(NodoRB nodo, int x, int y, int xOffset) {
        if (nodo == null) return;

        posiciones.put(nodo, new Point(x, y));
        calcularPosiciones(nodo.izquierdo, x - xOffset, y + 60, xOffset / 2);
        calcularPosiciones(nodo.derecho, x + xOffset, y + 60, xOffset / 2);
    }
}

public class NodoRB {
    public static final boolean RED = true; //los uso para que sea mas legible el codigo
    public static final boolean BLACK = false;

    int clave;
    String valor;
    boolean color;
    NodoRB izquierdo, derecho, padre;

    public NodoRB(int clave, String valor) {
        this.clave = clave;
        this.valor = valor;
        this.color = RED; // Nuevo nodo se inserta como rojo ;)
        this.izquierdo = null;
        this.derecho = null;
        this.padre = null;
    }

    
}


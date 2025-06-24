public class ArbolRB {
    private NodoRB raiz;

    public ArbolRB() {
        raiz = null;
    }

    // Métodos

        public String insertar(int clave, String valor) {
            if (buscarNodo(raiz, clave) != null) {
                
                return ("El ID " + clave + " ya existe. No se insertó.\n");
            }

            NodoRB nuevo = new NodoRB(clave, valor);
            insertarABB(nuevo);  // Inserción de arbol "normal"
            fixInsert(nuevo);    // Rebalanceo ;)
            return "Usuario agregado: " + clave + " - " + valor + "\n";

        }

        private void insertarABB(NodoRB nodo) {
            NodoRB actual = raiz;
            NodoRB padre = null;

            while (actual != null) {
                padre = actual;
                if (nodo.clave < actual.clave) {
                    actual = actual.izquierdo;
                } else {
                    actual = actual.derecho;
                }
            }

            nodo.padre = padre;

            if (padre == null) {
                raiz = nodo;
            }else if (nodo.clave < padre.clave) {
                padre.izquierdo = nodo;
            }else {
                padre.derecho = nodo;
            }
        }

        private void rotarIzquierda(NodoRB x) {
            NodoRB y = x.derecho;
            x.derecho = y.izquierdo;
            
            if (y.izquierdo != null) {
                y.izquierdo.padre = x;
            }
            
            y.padre = x.padre;
            
            if (x.padre == null) {
                raiz = y;
            }else if (x == x.padre.izquierdo) {
                x.padre.izquierdo = y;
            }else {
                x.padre.derecho = y;
            }
            
            y.izquierdo = x;
            x.padre = y;
        }
        //
        private void rotarDerecha(NodoRB x) {
        NodoRB y = x.izquierdo;
        x.izquierdo = y.derecho;
        
        if (y.derecho != null) {
            y.derecho.padre = x;
        }
        
        y.padre = x.padre;
        
        if (x.padre == null) {
            raiz = y;
        }else if (x == x.padre.derecho) {
            x.padre.derecho = y;
        }else {
            x.padre.izquierdo = y;
        }
        
        y.derecho = x;
        x.padre = y;
        }

    private void fixInsert(NodoRB nodo) {
    while (nodo != raiz && nodo.padre.color == NodoRB.RED) {
            NodoRB abuelo = nodo.padre.padre;

            if (nodo.padre == abuelo.izquierdo) {
                NodoRB tio = abuelo.derecho;

                if (tio != null && tio.color == NodoRB.RED) {
                    // Caso 1: 
                    nodo.padre.color = NodoRB.BLACK;
                    tio.color = NodoRB.BLACK;
                    abuelo.color = NodoRB.RED;
                    nodo = abuelo;
                } else {
                    if (nodo == nodo.padre.derecho) {
                        // Caso 2: 
                        nodo = nodo.padre;
                        rotarIzquierda(nodo);
                    }
                    // Caso 3: 
                    nodo.padre.color = NodoRB.BLACK;
                    abuelo.color = NodoRB.RED;
                    rotarDerecha(abuelo);
                }

            } else {
                // Mismo que arriba pero lado derecho
                NodoRB tio = abuelo.izquierdo;

                if (tio != null && tio.color == NodoRB.RED) {
                    // Caso 1: 
                    nodo.padre.color = NodoRB.BLACK;
                    tio.color = NodoRB.BLACK;
                    abuelo.color = NodoRB.RED;
                    nodo = abuelo;
                } else {
                    if (nodo == nodo.padre.izquierdo) {
                        // Caso 2:
                        nodo = nodo.padre;
                        rotarDerecha(nodo);
                    }
                    // Caso 3:
                    nodo.padre.color = NodoRB.BLACK;
                    abuelo.color = NodoRB.RED;
                    rotarIzquierda(abuelo);
                }
            }
        }
        raiz.color = NodoRB.BLACK;
    }

    public String inOrden() {
        StringBuilder sb = new StringBuilder();
        inOrdenRecursivo(raiz, sb);
        return sb.toString();
    }

    private void inOrdenRecursivo(NodoRB nodo, StringBuilder sb) {
        if (nodo != null) {
            inOrdenRecursivo(nodo.izquierdo, sb);
            String color = (nodo.color == NodoRB.RED) ? "ROJO" : "NEGRO";
            sb.append("ID: ").append(nodo.clave)
            .append(", Nombre: ").append(nodo.valor)
            .append(", Color: ").append(color).append("\n");
            inOrdenRecursivo(nodo.derecho, sb);
        }
    }


    public NodoRB ceiling(int clave) { //para encontrar el menor valor mayor
        NodoRB actual = raiz;
        NodoRB resultado = null;

        while (actual != null) {
            if (clave == actual.clave) {
                return actual;
            } else if (clave < actual.clave) {
                resultado = actual;
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }

        return resultado;
    }

    public String eliminar(int clave) {
        NodoRB nodo = buscarNodo(raiz, clave);
        if (nodo == null) {
            return " ID no encontrado: " + clave + "\n";
        }
        eliminarNodo(nodo);
        return " Usuario con ID " + clave + " eliminado correctamente.\n";
    }


    private NodoRB buscarNodo(NodoRB actual, int clave) {
        if (actual == null || clave == actual.clave) {
            return actual;
        }

        if (clave < actual.clave) {
            return buscarNodo(actual.izquierdo, clave);
        } else {
            return buscarNodo(actual.derecho, clave);
        }
    }

    private void eliminarNodo(NodoRB nodo) {
        NodoRB y = nodo;
        NodoRB x;
        boolean yOriginalColor = y.color;


        if (nodo.izquierdo == null) {
            x = nodo.derecho;
            transplantar(nodo, nodo.derecho);
        } else if (nodo.derecho == null) {
            x = nodo.izquierdo;
            transplantar(nodo, nodo.izquierdo);
        } else {
            y = minimo(nodo.derecho);
            yOriginalColor = y.color;
            x = y.derecho;

            if (y.padre == nodo) {
                if (x != null) x.padre = y;
            } else {
                transplantar(y, y.derecho);
                y.derecho = nodo.derecho;
                if (y.derecho != null) y.derecho.padre = y;
            }

            transplantar(nodo, y);
            y.izquierdo = nodo.izquierdo;
            if (y.izquierdo != null) y.izquierdo.padre = y;
            y.color = nodo.color;
        }

        if (yOriginalColor == NodoRB.BLACK) {
            fixDelete(x);
        }
    }

    private void transplantar(NodoRB u, NodoRB v) {//ayuda a encontrar el nodo que sustituye el eliminado
        if (u.padre == null) {
            raiz = v;
        } else if (u == u.padre.izquierdo) {
            u.padre.izquierdo = v;
        } else {
            u.padre.derecho = v;
        }
        if (v != null) {
            v.padre = u.padre;
        }
    }

    private NodoRB minimo(NodoRB nodo) {//nodo minimo de la rama derecha 
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }

    private void fixDelete(NodoRB x) {
    while (x != raiz && getColor(x) == NodoRB.BLACK) {
        if (x.padre == null) break; // protección extra

        if (x == x.padre.izquierdo) {
            NodoRB w = x.padre.derecho;

            if (getColor(w) == NodoRB.RED) {
                w.color = NodoRB.BLACK;
                x.padre.color = NodoRB.RED;
                rotarIzquierda(x.padre);
                w = x.padre.derecho;
            }

            if (getColor(w.izquierdo) == NodoRB.BLACK && getColor(w.derecho) == NodoRB.BLACK) {
                if (w != null) w.color = NodoRB.RED;
                x = x.padre;
            } else {
                if (getColor(w.derecho) == NodoRB.BLACK) {
                    if (w.izquierdo != null) w.izquierdo.color = NodoRB.BLACK;
                    if (w != null) w.color = NodoRB.RED;
                    rotarDerecha(w);
                    w = x.padre.derecho;
                }

                if (w != null) w.color = x.padre.color;
                x.padre.color = NodoRB.BLACK;
                if (w.derecho != null) w.derecho.color = NodoRB.BLACK;
                rotarIzquierda(x.padre);
                x = raiz;
            }

        } else {
            NodoRB w = x.padre.izquierdo;

            if (getColor(w) == NodoRB.RED) {
                w.color = NodoRB.BLACK;
                x.padre.color = NodoRB.RED;
                rotarDerecha(x.padre);
                w = x.padre.izquierdo;
            }

            if (getColor(w.derecho) == NodoRB.BLACK && getColor(w.izquierdo) == NodoRB.BLACK) {
                if (w != null) w.color = NodoRB.RED;
                x = x.padre;
            } else {
                if (getColor(w.izquierdo) == NodoRB.BLACK) {
                    if (w.derecho != null) w.derecho.color = NodoRB.BLACK;
                    if (w != null) w.color = NodoRB.RED;
                    rotarIzquierda(w);
                    w = x.padre.izquierdo;
                }

                if (w != null) w.color = x.padre.color;
                x.padre.color = NodoRB.BLACK;
                if (w.izquierdo != null) w.izquierdo.color = NodoRB.BLACK;
                rotarDerecha(x.padre);
                x = raiz;
            }
        }
    }

    if (x != null)
        x.color = NodoRB.BLACK;
}


    private boolean getColor(NodoRB nodo) {
        return (nodo == null) ? NodoRB.BLACK : nodo.color;
    }

    public String buscarPorID(int clave) {
        NodoRB nodo = buscarNodo(raiz, clave);
        if (nodo != null) {
            String color = (nodo.color == NodoRB.RED) ? "ROJO" : "NEGRO";
            return " Encontrado: ID: " + nodo.clave + ", Nombre: " + nodo.valor + ", Color: " + color;
        } else {
            return " Usuario con ID " + clave + " no encontrado.";
        }
    }



    public NodoRB getRaiz() {
        return raiz;
    }

}

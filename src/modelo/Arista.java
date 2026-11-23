package modelo;

/**
 * Nodo de la lista de adyacencia de un vertice.
 * destino es la otra persona; tipo distingue AMISTAD de SEGUIR.
 */
public class Arista {
    public Vertice destino;
    public TipoRelacion tipo;
    public Arista sig;

    public Arista(Vertice destino, TipoRelacion tipo) {
        this.destino = destino;
        this.tipo = tipo;
        this.sig = null;
    }
}

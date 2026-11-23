package modelo;

/**
 * Vertice de la multilista: una persona y la cabeza de su lista de aristas.
 * sig enlaza el siguiente usuario de la red.
 */
public class Vertice {
    public Persona persona;
    public Arista aristas;
    public Vertice sig;

    public Vertice(Persona persona) {
        this.persona = persona;
        this.aristas = null;
        this.sig = null;
    }
}

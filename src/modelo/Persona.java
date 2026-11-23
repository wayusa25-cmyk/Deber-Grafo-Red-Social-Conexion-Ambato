package modelo;

/**
 * Datos de una cuenta de Conexion Ambato.
 * El usuario se guarda normalizado (sin espacios extremos y en minusculas).
 */
public class Persona {
    public String usuario;
    public String nombre;

    public Persona(String usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }
}

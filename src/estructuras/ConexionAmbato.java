package estructuras;

import modelo.Arista;
import modelo.Persona;
import modelo.TipoRelacion;
import modelo.Vertice;

/**
 * Red social Conexion Ambato como grafo dinamico (multilista).
 *
 * Cada persona es un vertice enlazado. Cada vertice tiene su propia lista
 * de aristas. AMISTAD se guarda en ambos sentidos y SEGUIR solo desde quien
 * sigue hacia el seguido. No se permiten lazos ni duplicados del mismo tipo.
 *
 * No usa HashMap, HashSet, ArrayList ni LinkedList: solo nodos propios.
 * Los usuarios se insertan al inicio, de modo que al cargar A01..A06 la
 * cabeza queda: Diego, Camila, Andres, Valeria, Mateo, Sofia.
 * Las aristas se agregan al final de la lista del origen para que
 * listarAmigos conserve el orden de insercion (Tabla 3: Valeria, Sofia y Camila).
 */
public class ConexionAmbato {

    private Vertice cabeza;
    private int cantidadPersonas;
    private int cantidadAmistades;

    public ConexionAmbato() {
        cabeza = null;
        cantidadPersonas = 0;
        cantidadAmistades = 0;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public int getCantidadAmistades() {
        return cantidadAmistades;
    }

    /**
     * Registra una persona. Rechaza usuario nulo, vacio, repetido
     * o que contenga espacios o comas. El nombre no puede ir vacio.
     */
    public boolean registrarPersona(String usuario, String nombre) {
        String clave = normalizar(usuario);
        if (clave == null || nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        if (buscarCodigo(clave) != null) {
            return false;
        }
        Vertice nuevo = new Vertice(new Persona(clave, nombre.trim()));
        nuevo.sig = cabeza;
        cabeza = nuevo;
        cantidadPersonas++;
        return true;
    }

    /**
     * Da de baja a la persona y limpia toda arista entrante antes de
     * desenlazar el vertice. Las aristas salientes desaparecen con el nodo.
     */
    public boolean eliminarPersona(String usuario) {
        Vertice objetivo = buscar(usuario);
        if (objetivo == null) {
            return false;
        }
        for (Vertice w = cabeza; w != null; w = w.sig) {
            if (w != objetivo) {
                quitarHacia(w, objetivo);
            }
        }
        Vertice anterior = null;
        Vertice actual = cabeza;
        while (actual != objetivo) {
            anterior = actual;
            actual = actual.sig;
        }
        if (anterior == null) {
            cabeza = objetivo.sig;
        } else {
            anterior.sig = objetivo.sig;
        }
        cantidadPersonas--;
        return true;
    }

    /** Crea las dos aristas AMISTAD. Rechaza inexistentes, lazos y duplicados. */
    public boolean agregarAmistad(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || existe(va, vb, TipoRelacion.AMISTAD)) {
            return false;
        }
        insertar(va, vb, TipoRelacion.AMISTAD);
        insertar(vb, va, TipoRelacion.AMISTAD);
        cantidadAmistades++;
        return true;
    }

    /** Crea una sola arista dirigida SEGUIR. */
    public boolean seguir(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || existe(va, vb, TipoRelacion.SEGUIR)) {
            return false;
        }
        insertar(va, vb, TipoRelacion.SEGUIR);
        return true;
    }

    /** Quita las dos aristas AMISTAD. */
    public boolean eliminarAmistad(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || !existe(va, vb, TipoRelacion.AMISTAD)) {
            return false;
        }
        quitar(va, vb, TipoRelacion.AMISTAD);
        quitar(vb, va, TipoRelacion.AMISTAD);
        cantidadAmistades--;
        return true;
    }

    /** Quita solo la arista SEGUIR de a hacia b. */
    public boolean dejarDeSeguir(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || !existe(va, vb, TipoRelacion.SEGUIR)) {
            return false;
        }
        quitar(va, vb, TipoRelacion.SEGUIR);
        return true;
    }

    /**
     * Amigos, seguidos y seguidores de cada persona, en el orden de registro.
     * Sirve para la opcion 8 del menu cuando no se pide una sola cuenta.
     */
    public String listarRelaciones() {
        if (cabeza == null) {
            return "(red vacia)";
        }
        int n = cantidadPersonas;
        Vertice[] vertices = new Vertice[n];
        int i = n - 1;
        for (Vertice v = cabeza; v != null; v = v.sig) {
            vertices[i] = v;
            i--;
        }
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < n; k++) {
            Vertice v = vertices[k];
            if (k > 0) {
                sb.append("\n");
            }
            sb.append(codigo(v.persona.usuario)).append(" ").append(v.persona.nombre).append("\n");
            sb.append("  Amigos: ").append(listarAmigos(v.persona.usuario)).append("\n");
            sb.append("  Seguidos: ").append(listarSeguidos(v.persona.usuario)).append("\n");
            sb.append("  Seguidores: ").append(listarSeguidores(v.persona.usuario));
        }
        return sb.toString();
    }

    /** Nombres de los amigos, o null si el usuario no existe. */
    public String listarAmigos(String usuario) {
        return listarPropias(usuario, TipoRelacion.AMISTAD);
    }

    /** Nombres de a quienes sigue, o null si el usuario no existe. */
    public String listarSeguidos(String usuario) {
        return listarPropias(usuario, TipoRelacion.SEGUIR);
    }

    /**
     * Nombres de quienes siguen a la persona. Recorre toda la red porque
     * la arista SEGUIR vive en la lista de quien sigue, no en la del seguido.
     * Devuelve null si el usuario no existe.
     */
    public String listarSeguidores(String usuario) {
        Vertice objetivo = buscar(usuario);
        if (objetivo == null) {
            return null;
        }
        int n = 0;
        for (Vertice w = cabeza; w != null; w = w.sig) {
            if (existe(w, objetivo, TipoRelacion.SEGUIR)) {
                n++;
            }
        }
        String[] nombres = new String[n];
        int i = 0;
        for (Vertice w = cabeza; w != null; w = w.sig) {
            if (existe(w, objetivo, TipoRelacion.SEGUIR)) {
                nombres[i++] = w.persona.nombre;
            }
        }
        return formatear(nombres, n);
    }

    /** Cantidad de amigos, o -1 si el usuario no existe. */
    public int contarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return -1;
        }
        return contarTipo(v, TipoRelacion.AMISTAD);
    }

    /** Grados en el orden de registro (el mas antiguo primero). */
    public String resumenGrados() {
        int n = cantidadPersonas;
        String[] partes = new String[n];
        int i = n - 1;
        for (Vertice v = cabeza; v != null; v = v.sig) {
            partes[i] = codigo(v.persona.usuario) + "=" + contarTipo(v, TipoRelacion.AMISTAD);
            i--;
        }
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < n; k++) {
            if (k > 0) {
                sb.append(", ");
            }
            sb.append(partes[k]);
        }
        return sb.toString();
    }

    public String mostrarRed() {
        StringBuilder sb = new StringBuilder();
        sb.append("Personas: ").append(cantidadPersonas);
        sb.append(" | Amistades: ").append(cantidadAmistades);
        sb.append(" | Aristas almacenadas: ").append(contarAristas());
        sb.append("\n");
        if (cabeza == null) {
            sb.append("(red vacia)");
            return sb.toString();
        }
        for (Vertice v = cabeza; v != null; v = v.sig) {
            sb.append(codigo(v.persona.usuario)).append(" ").append(v.persona.nombre);
            sb.append(" | grado ").append(contarTipo(v, TipoRelacion.AMISTAD));
            sb.append("\n");
            if (v.aristas == null) {
                sb.append("   (sin relaciones)\n");
            } else {
                for (Arista a = v.aristas; a != null; a = a.sig) {
                    sb.append("   ").append(a.tipo);
                    sb.append(" -> ").append(codigo(a.destino.persona.usuario));
                    sb.append(" ").append(a.destino.persona.nombre);
                    sb.append("\n");
                }
            }
        }
        return sb.toString().stripTrailing();
    }

    /** true si queda alguna arista cuyo destino sea ese usuario. */
    public boolean hayReferenciasHacia(String usuario) {
        String clave = normalizar(usuario);
        if (clave == null) {
            return false;
        }
        for (Vertice v = cabeza; v != null; v = v.sig) {
            for (Arista a = v.aristas; a != null; a = a.sig) {
                if (a.destino.persona.usuario.equals(clave)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String listarPropias(String usuario, TipoRelacion tipo) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return null;
        }
        int n = contarTipo(v, tipo);
        String[] nombres = new String[n];
        int i = 0;
        for (Arista a = v.aristas; a != null; a = a.sig) {
            if (a.tipo == tipo) {
                nombres[i++] = a.destino.persona.nombre;
            }
        }
        return formatear(nombres, n);
    }

    private int contarTipo(Vertice v, TipoRelacion tipo) {
        int n = 0;
        for (Arista a = v.aristas; a != null; a = a.sig) {
            if (a.tipo == tipo) {
                n++;
            }
        }
        return n;
    }

    private int contarAristas() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.sig) {
            for (Arista a = v.aristas; a != null; a = a.sig) {
                n++;
            }
        }
        return n;
    }

    /** "Ninguno", "Ana", "Ana y Luis" o "Ana, Luis y Mia". */
    private String formatear(String[] nombres, int n) {
        if (n == 0) {
            return "Ninguno";
        }
        if (n == 1) {
            return nombres[0];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n - 1; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(nombres[i]);
        }
        sb.append(" y ").append(nombres[n - 1]);
        return sb.toString();
    }

    private String codigo(String usuario) {
        return usuario.toUpperCase();
    }

    /**
     * null si el identificador es nulo, vacio o contiene espacios o comas.
     * En otro caso devuelve la clave en minusculas. Por eso A01 y a01
     * son la misma cuenta.
     */
    private String normalizar(String usuario) {
        if (usuario == null) {
            return null;
        }
        String clave = usuario.trim().toLowerCase();
        if (clave.isEmpty() || clave.indexOf(' ') >= 0 || clave.indexOf(',') >= 0) {
            return null;
        }
        return clave;
    }

    /**
     * Localiza por codigo (A04 o a04) o por nombre (Andres, Andrés, "A04 Andrés").
     * El codigo tiene prioridad sobre el nombre.
     */
    private Vertice buscar(String usuario) {
        if (usuario == null) {
            return null;
        }
        String clave = normalizar(usuario);
        if (clave != null) {
            Vertice porCodigo = buscarCodigo(clave);
            if (porCodigo != null) {
                return porCodigo;
            }
            return buscarPorNombre(usuario);
        }
        String recortado = usuario.trim();
        int espacio = recortado.indexOf(' ');
        if (espacio > 0) {
            String primera = normalizar(recortado.substring(0, espacio));
            Vertice porCodigo = buscarCodigo(primera);
            if (porCodigo != null) {
                return porCodigo;
            }
        }
        return buscarPorNombre(recortado);
    }

    private Vertice buscarCodigo(String clave) {
        if (clave == null) {
            return null;
        }
        for (Vertice v = cabeza; v != null; v = v.sig) {
            if (v.persona.usuario.equals(clave)) {
                return v;
            }
        }
        return null;
    }

    private Vertice buscarPorNombre(String texto) {
        if (texto == null) {
            return null;
        }
        String pedido = simplificar(texto);
        if (pedido.isEmpty()) {
            return null;
        }
        Vertice hallado = null;
        for (Vertice v = cabeza; v != null; v = v.sig) {
            if (simplificar(v.persona.nombre).equals(pedido)) {
                if (hallado != null) {
                    return null;
                }
                hallado = v;
            }
        }
        return hallado;
    }

    /** Minusculas y sin tildes, para que Andrés y Andres sean el mismo nombre. */
    private String simplificar(String texto) {
        String base = java.text.Normalizer.normalize(texto.trim(), java.text.Normalizer.Form.NFD);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < base.length(); i++) {
            char c = base.charAt(i);
            if (Character.getType(c) != Character.NON_SPACING_MARK) {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    /** Inserta la arista al final para respetar el orden de insercion. */
    private void insertar(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista nueva = new Arista(destino, tipo);
        if (origen.aristas == null) {
            origen.aristas = nueva;
            return;
        }
        Arista ultima = origen.aristas;
        while (ultima.sig != null) {
            ultima = ultima.sig;
        }
        ultima.sig = nueva;
    }

    /** Recorre la lista de aristas propias del origen. */
    private boolean existe(Vertice origen, Vertice destino, TipoRelacion tipo) {
        for (Arista a = origen.aristas; a != null; a = a.sig) {
            if (a.destino == destino && a.tipo == tipo) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retira de "desde" toda arista que apunte al objetivo, sea AMISTAD o SEGUIR.
     * Si la arista retirada es una amistad, descuenta esa relacion.
     */
    private void quitarHacia(Vertice desde, Vertice objetivo) {
        Arista anterior = null;
        Arista actual = desde.aristas;
        while (actual != null) {
            if (actual.destino == objetivo) {
                if (actual.tipo == TipoRelacion.AMISTAD) {
                    cantidadAmistades--;
                }
                if (anterior == null) {
                    desde.aristas = actual.sig;
                } else {
                    anterior.sig = actual.sig;
                }
                actual = (anterior == null) ? desde.aristas : anterior.sig;
            } else {
                anterior = actual;
                actual = actual.sig;
            }
        }
    }

    private boolean quitar(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista anterior = null;
        Arista actual = origen.aristas;
        while (actual != null) {
            if (actual.destino == destino && actual.tipo == tipo) {
                if (anterior == null) {
                    origen.aristas = actual.sig;
                } else {
                    anterior.sig = actual.sig;
                }
                return true;
            }
            anterior = actual;
            actual = actual.sig;
        }
        return false;
    }
}

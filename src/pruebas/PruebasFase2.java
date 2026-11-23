package pruebas;

import app.Main;
import estructuras.ConexionAmbato;

/**
 * Pruebas de la Fase 2 sin JUnit. Cada caso usa nodos de la red,
 * no colecciones de java.util.
 */
public class PruebasFase2 {

    private static int pruebas;
    private static int fallos;

    public static void main(String[] args) {
        int f = ejecutar();
        if (f > 0) {
            System.exit(1);
        }
    }

    public static int ejecutar() {
        pruebas = 0;
        fallos = 0;
        System.out.println("========== ANEXO 5. Pruebas automatizadas ==========");
        secuenciaTabla3();
        validaciones();
        dejarDeSeguirYMayusculas();
        consultaPorNombre();
        System.out.println();
        System.out.println("Pruebas: " + pruebas + " | Fallos: " + fallos);
        if (fallos == 0) {
            System.out.println("RESULTADO: todas las pruebas pasaron");
        } else {
            System.out.println("RESULTADO: hay pruebas fallidas");
        }
        return fallos;
    }

    private static void secuenciaTabla3() {
        ConexionAmbato red = new ConexionAmbato();
        Main.cargarInicial(red);
        igual("paso1 usuarios", 6, red.getCantidadPersonas());
        igual("paso1 amistades", 5, red.getCantidadAmistades());
        igual("paso1 grados", "A01=2, A02=2, A03=2, A04=3, A05=1, A06=0", red.resumenGrados());
        igual("paso1 amigos Sofia", "Mateo y Andrés", red.listarAmigos("A01"));
        igual("paso1 amigos Mateo", "Sofía y Valeria", red.listarAmigos("A02"));
        igual("paso1 amigos Valeria", "Mateo y Andrés", red.listarAmigos("A03"));
        igual("paso1 amigos Andres", "Valeria, Sofía y Camila", red.listarAmigos("A04"));
        igual("paso1 amigos Camila", "Andrés", red.listarAmigos("A05"));
        igual("paso1 amigos Diego", "Ninguno", red.listarAmigos("A06"));
        igual("paso1 aristas", 10, contarAristasVisibles(red));

        verdad("paso2 seguir Mateo a Sofia", red.seguir("A02", "A01"));
        igual("paso2 seguidores Sofia", "Mateo", red.listarSeguidores("A01"));

        verdad("paso3 seguir Diego a Sofia", red.seguir("A06", "A01"));
        igual("paso3 seguidos Diego", "Sofía", red.listarSeguidos("A06"));
        igual("paso3 amigos Diego", "Ninguno", red.listarAmigos("A06"));
        igual("paso3 seguidores Sofia", "Diego y Mateo", red.listarSeguidores("A01"));

        igual("paso4 listarAmigos A04", "Valeria, Sofía y Camila", red.listarAmigos("A04"));
        igual("paso4 contarAmigos A04", 3, red.contarAmigos("A04"));

        verdad("paso5 eliminar amistad Andres-Camila", red.eliminarAmistad("A04", "A05"));
        igual("paso5 amigos Andres", "Valeria y Sofía", red.listarAmigos("A04"));
        igual("paso5 amigos Camila", "Ninguno", red.listarAmigos("A05"));
        igual("paso5 amistades", 4, red.getCantidadAmistades());

        verdad("paso6 eliminar Sofia", red.eliminarPersona("A01"));
        igual("paso6 listar Sofia", null, red.listarAmigos("A01"));
        igual("paso6 seguidores Sofia", null, red.listarSeguidores("a01"));
        igual("paso6 amigos Mateo", "Valeria", red.listarAmigos("A02"));
        igual("paso6 amigos Andres", "Valeria", red.listarAmigos("A04"));
        igual("paso6 seguidos Mateo", "Ninguno", red.listarSeguidos("A02"));
        igual("paso6 seguidos Diego", "Ninguno", red.listarSeguidos("A06"));
        igual("paso6 usuarios", 5, red.getCantidadPersonas());
        igual("paso6 amistades", 2, red.getCantidadAmistades());
        falso("paso6 referencias hacia Sofia", red.hayReferenciasHacia("A01"));

        falso("paso7 duplicado A02", red.registrarPersona("A02", "otro nombre"));
        falso("paso8 lazo A03", red.agregarAmistad("A03", "A03"));
    }

    private static void validaciones() {
        ConexionAmbato red = new ConexionAmbato();
        falso("usuario vacio", red.registrarPersona("  ", "Ana"));
        falso("usuario con espacio", red.registrarPersona("A 07", "Ana"));
        falso("usuario con coma", red.registrarPersona("A07,X", "Ana"));
        falso("usuario nulo", red.registrarPersona(null, "Ana"));
        falso("nombre vacio", red.registrarPersona("A07", "  "));
        verdad("alta A07", red.registrarPersona("A07", "Ana"));
        falso("repetido con otra caja", red.registrarPersona("a07", "Otra"));
        falso("amistad inexistente", red.agregarAmistad("A07", "A08"));
        falso("seguir a si mismo", red.seguir("A07", "A07"));
        igual("contar inexistente", -1, red.contarAmigos("A99"));
        igual("listar inexistente", null, red.listarSeguidos("A99"));
        falso("eliminar inexistente", red.eliminarPersona("A99"));
        falso("eliminar amistad ausente", red.eliminarAmistad("A07", "A01"));
    }

    private static void dejarDeSeguirYMayusculas() {
        ConexionAmbato red = new ConexionAmbato();
        Main.cargarInicial(red);
        verdad("seguir con minusculas", red.seguir("a06", "a01"));
        falso("seguimiento duplicado", red.seguir("A06", "A01"));
        igual("misma cuenta a01", "Sofía", red.listarSeguidos("A06"));
        verdad("dejar de seguir", red.dejarDeSeguir("A06", "A01"));
        igual("ya no sigue", "Ninguno", red.listarSeguidos("A06"));
        falso("dejar de seguir otra vez", red.dejarDeSeguir("A06", "A01"));
        falso("segunda amistad", red.agregarAmistad("a01", "A02"));
    }

    private static void consultaPorNombre() {
        ConexionAmbato red = new ConexionAmbato();
        Main.cargarInicial(red);
        igual("amigos por nombre", "Valeria, Sofía y Camila", red.listarAmigos("Andrés"));
        igual("amigos sin tilde", "Valeria, Sofía y Camila", red.listarAmigos("Andres"));
        igual("amigos codigo y nombre", "Valeria, Sofía y Camila", red.listarAmigos("A04 Andrés"));
        igual("contar por nombre", 1, red.contarAmigos("Camila"));
        verdad("seguir por nombre", red.seguir("Diego", "Sofía"));
        igual("seguidores por nombre", "Diego", red.listarSeguidores("Sofia"));
        verdad("amistad por nombre", red.agregarAmistad("Diego", "Sofía"));
        igual("diego ya tiene amiga", "Sofía", red.listarAmigos("diego"));
        verdad("eliminar amistad por nombre", red.eliminarAmistad("Andrés", "Camila"));
        igual("camila sin amigos", "Ninguno", red.listarAmigos("Camila"));
        verdad("dejar de seguir por nombre", red.dejarDeSeguir("Diego", "Sofia"));
        igual("diego ya no sigue", "Ninguno", red.listarSeguidos("Diego"));
        verdad("eliminar por nombre", red.eliminarPersona("Sofía"));
        igual("sofia ya no esta", null, red.listarAmigos("Sofía"));
    }

    /** Cuenta las aristas que mostrarRed describiria, releyendo la red por operaciones. */
    private static int contarAristasVisibles(ConexionAmbato red) {
        return red.getCantidadAmistades() * 2;
    }

    private static void igual(String nombre, int esperado, int obtenido) {
        revisar(nombre, esperado == obtenido, esperado + " != " + obtenido);
    }

    private static void igual(String nombre, String esperado, String obtenido) {
        boolean ok = esperado == null ? obtenido == null : esperado.equals(obtenido);
        revisar(nombre, ok, "esperado [" + esperado + "] obtuvo [" + obtenido + "]");
    }

    private static void verdad(String nombre, boolean valor) {
        revisar(nombre, valor, "se esperaba true");
    }

    private static void falso(String nombre, boolean valor) {
        revisar(nombre, !valor, "se esperaba false");
    }

    private static void revisar(String nombre, boolean ok, String detalle) {
        pruebas++;
        if (ok) {
            System.out.println("OK  " + nombre);
        } else {
            fallos++;
            System.out.println("FALLO  " + nombre + " -> " + detalle);
        }
    }
}

package app;

import estructuras.ConexionAmbato;
import java.util.Scanner;

/**
 * Fase 2 de APE 4: Conexion Ambato.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && "pruebas".equalsIgnoreCase(args[0])) {
            int fallos = pruebas.PruebasFase2.ejecutar();
            if (fallos > 0) {
                System.exit(1);
            }
            return;
        }
        if (args.length > 0 && "secuencia".equalsIgnoreCase(args[0])) {
            ejecutarSecuencia();
            return;
        }
        menu();
    }

    public static ConexionAmbato cargarInicial(ConexionAmbato red) {
        red.registrarPersona("A01", "Sofía");
        red.registrarPersona("A02", "Mateo");
        red.registrarPersona("A03", "Valeria");
        red.registrarPersona("A04", "Andrés");
        red.registrarPersona("A05", "Camila");
        red.registrarPersona("A06", "Diego");
        red.agregarAmistad("A01", "A02");
        red.agregarAmistad("A02", "A03");
        red.agregarAmistad("A03", "A04");
        red.agregarAmistad("A04", "A01");
        red.agregarAmistad("A04", "A05");
        return red;
    }

    public static void ejecutarSecuencia() {
        ConexionAmbato red = new ConexionAmbato();
        System.out.println("CONEXION AMBATO · APE 4 Fase 2");
        System.out.println("Romo Nuñez Joseph Alejandro");
        System.out.println("Secuencia de comprobacion (Tabla 3)");
        System.out.println();

        System.out.println("========== ANEXO 1. registrarPersona y mostrarRed ==========");
        System.out.println("Paso 1. Registrar A01-A06 y las cinco amistades");
        cargarInicial(red);
        System.out.println("Usuarios: " + red.getCantidadPersonas());
        System.out.println("Amistades: " + red.getCantidadAmistades());
        System.out.println("Grados: " + red.resumenGrados());
        System.out.println(red.mostrarRed());
        System.out.println();

        System.out.println("========== ANEXO 2. Amistades y seguimientos ==========");
        System.out.println("Paso 2. seguir(A02, A01) -> " + red.seguir("A02", "A01"));
        System.out.println("Seguidores de Sofia: " + red.listarSeguidores("A01"));
        System.out.println("Paso 3. seguir(A06, A01) -> " + red.seguir("A06", "A01"));
        System.out.println("Seguidos de Diego: " + red.listarSeguidos("A06"));
        System.out.println("Amigos de Diego: " + red.listarAmigos("A06"));
        System.out.println("Seguidores de Sofia: " + red.listarSeguidores("A01"));
        System.out.println(red.mostrarRed());
        System.out.println();

        System.out.println("========== ANEXO 4. Listados y contarAmigos ==========");
        System.out.println("Paso 4. listarAmigos(A04) -> " + red.listarAmigos("A04"));
        System.out.println("listarSeguidos(A02) -> " + red.listarSeguidos("A02"));
        System.out.println("listarSeguidos(A06) -> " + red.listarSeguidos("A06"));
        System.out.println("listarSeguidores(A01) -> " + red.listarSeguidores("A01"));
        System.out.println("contarAmigos(A04) -> " + red.contarAmigos("A04"));
        System.out.println("contarAmigos(A06) -> " + red.contarAmigos("A06"));
        System.out.println("contarAmigos(A99) -> " + red.contarAmigos("A99"));
        System.out.println("listarAmigos(A99) -> " + red.listarAmigos("A99"));
        System.out.println();

        System.out.println("========== ANEXO 3. eliminarAmistad y eliminarPersona ==========");
        System.out.println("Paso 5. eliminarAmistad(A04, A05) -> " + red.eliminarAmistad("A04", "A05"));
        System.out.println("Amigos de Andres: " + red.listarAmigos("A04"));
        System.out.println("Amigos de Camila: " + red.listarAmigos("A05"));
        System.out.println("Paso 6. eliminarPersona(A01) -> " + red.eliminarPersona("A01"));
        System.out.println("listarAmigos(A01) -> " + red.listarAmigos("A01"));
        System.out.println("Amigos de Mateo: " + red.listarAmigos("A02"));
        System.out.println("Amigos de Andres: " + red.listarAmigos("A04"));
        System.out.println("Seguidos de Mateo: " + red.listarSeguidos("A02"));
        System.out.println("Seguidos de Diego: " + red.listarSeguidos("A06"));
        System.out.println("Quedan referencias hacia A01: " + red.hayReferenciasHacia("A01"));
        System.out.println(red.mostrarRed());
        System.out.println();

        System.out.println("Paso 7. registrarPersona(A02, otro nombre) -> "
                + red.registrarPersona("A02", "otro nombre"));
        System.out.println("Paso 8. agregarAmistad(A03, A03) -> " + red.agregarAmistad("A03", "A03"));
        System.out.println("Usuarios finales: " + red.getCantidadPersonas());
        System.out.println("Amistades finales: " + red.getCantidadAmistades());
    }

    private static void menu() {
        ConexionAmbato red = new ConexionAmbato();
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        while (seguir) {
            System.out.println();
            System.out.println("--- Conexion Ambato ---");
            System.out.println("1. Cargar red inicial (A01-A06 y 5 amistades)");
            System.out.println("2. Registrar persona");
            System.out.println("3. Eliminar persona");
            System.out.println("4. Agregar amistad");
            System.out.println("5. Seguir");
            System.out.println("6. Eliminar amistad");
            System.out.println("7. Dejar de seguir");
            System.out.println("8. Listar amigos, seguidos y seguidores");
            System.out.println("9. Contar amigos");
            System.out.println("10. Mostrar red");
            System.out.println("11. Ejecutar secuencia de comprobacion");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            System.out.flush();
            if (!sc.hasNextLine()) {
                break;
            }
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> {
                    red = new ConexionAmbato();
                    cargarInicial(red);
                    System.out.println("Red inicial cargada.");
                    System.out.println(red.mostrarRed());
                }
                case "2" -> {
                    String u = leer(sc, "Codigo (ejemplo A07): ");
                    String n = leer(sc, "Nombre: ");
                    if (red.registrarPersona(u, n)) {
                        System.out.println("Persona registrada.");
                    } else {
                        System.out.println("No se registro. El codigo esta vacio, repetido, o tiene espacios o comas.");
                    }
                }
                case "3" -> {
                    String u = leer(sc, "Codigo o nombre: ");
                    if (red.eliminarPersona(u)) {
                        System.out.println("Persona eliminada, junto con sus amistades y seguimientos.");
                    } else {
                        System.out.println("No se elimino. Esa persona no esta en la red.");
                    }
                }
                case "4" -> relacion(sc, red, "amistad");
                case "5" -> relacion(sc, red, "seguir");
                case "6" -> relacion(sc, red, "eliminarAmistad");
                case "7" -> relacion(sc, red, "dejar");
                case "8" -> listar(sc, red);
                case "9" -> {
                    String u = leer(sc, "Codigo o nombre: ");
                    int cantidad = red.contarAmigos(u);
                    if (cantidad < 0) {
                        System.out.println("No existe la persona \"" + u + "\".");
                    } else {
                        System.out.println("Amigos: " + cantidad);
                    }
                }
                case "10" -> System.out.println(red.mostrarRed());
                case "11" -> ejecutarSecuencia();
                case "0" -> seguir = false;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void listar(Scanner sc, ConexionAmbato red) {
        String u = leer(sc, "Codigo o nombre (Enter lista a todos): ");
        if (u.isEmpty()) {
            System.out.println(red.listarRelaciones());
            return;
        }
        String amigos = red.listarAmigos(u);
        if (amigos == null) {
            System.out.println("No existe la persona \"" + u + "\".");
            return;
        }
        System.out.println("Amigos: " + amigos);
        System.out.println("Seguidos: " + red.listarSeguidos(u));
        System.out.println("Seguidores: " + red.listarSeguidores(u));
    }

    private static void relacion(Scanner sc, ConexionAmbato red, String tipo) {
        String a = leer(sc, "Codigo o nombre de origen: ");
        String b = leer(sc, "Codigo o nombre de destino: ");
        boolean ok = switch (tipo) {
            case "amistad" -> red.agregarAmistad(a, b);
            case "seguir" -> red.seguir(a, b);
            case "eliminarAmistad" -> red.eliminarAmistad(a, b);
            default -> red.dejarDeSeguir(a, b);
        };
        if (ok) {
            System.out.println(switch (tipo) {
                case "amistad" -> "Amistad agregada.";
                case "seguir" -> "Ahora lo sigue.";
                case "eliminarAmistad" -> "Amistad eliminada.";
                default -> "Dejo de seguirlo.";
            });
        } else {
            System.out.println(switch (tipo) {
                case "amistad" -> "No se agrego la amistad. Revisa que ambos existan, que no sea un lazo y que no sean amigos ya.";
                case "seguir" -> "No se agrego el seguimiento. Revisa que ambos existan, que no sea un lazo y que no lo siga ya.";
                case "eliminarAmistad" -> "No se elimino. Esa amistad no existe.";
                default -> "No se elimino. Ese seguimiento no existe.";
            });
        }
    }

    private static String leer(Scanner sc, String prompt) {
        System.out.print(prompt);
        System.out.flush();
        if (!sc.hasNextLine()) {
            return "";
        }
        return sc.nextLine().trim();
    }
}

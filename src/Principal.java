import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Grafo.Edge;
import Grafo.Graph;
import Grafo.TreeMapGraph;
import Grafo.Vertex;

/**
 * Es un menú que le permite seleccionar una opción, y dependiendo de la opción
 * que seleccione, llamará
 * a otra función
 */
public class Principal {

    public static void main(String[] args) {
        System.out.println("Cargando");
         Graph<DecoratedElement<Personaje>, Integer> grafo=CargarDatos();
        //Graph<DecoratedElement<Personaje>, Integer> grafo = cargarDatosPruebas();
        Menu(grafo);
    }

    /**
     * Lee un archivo csv y crea un gráfico con los datos del archivo
     * 
     * @return Un gráfico de elementos decorados de Personaje.
     */
    public static Graph<DecoratedElement<Personaje>, Integer> CargarDatos() {
        Graph<DecoratedElement<Personaje>, Integer> g = new TreeMapGraph<DecoratedElement<Personaje>, Integer>();
        try {
            Scanner lector = new Scanner(new File("marvel-unimodal-edges.csv"));
            String linea = lector.nextLine();
            do {
                linea = lector.nextLine();
                String[] splitted = linea.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if(splitted[0].equals("Iron Man / Tony Stark")&&splitted[1].equals("Thanos"))
                System.out.println(splitted[2]);
                g.insertEdge(new DecoratedElement<Personaje>(new Personaje(splitted[0])),
                        new DecoratedElement<Personaje>(new Personaje(splitted[1])), Integer.valueOf(splitted[2]));
            } while (lector.hasNextLine());
            lector.close();
            return g;
        } catch (IOException ex) {
            System.out.println("Holasdfklas");
            System.exit(1);
            return null;
        }

    }

    /**
     * Es un menú que le permite seleccionar una opción, y dependiendo de la opción
     * que seleccione,
     * llamará a otra función
     * 
     * @param grafo Gráfico<ElementoDecorado<Personaje>, Entero>
     */
    public static void Menu(Graph<DecoratedElement<Personaje>, Integer> grafo) {
       while (false==false){
        System.out.println("\n\nMenu Principal");
        System.out.println("Selecciona una opción");
        System.out.println("1 Mostar datos de los personajes");
        System.out.println("2 Conexión entre personajes");
        System.out.println("3 Formar equipo con dos personajes");
        System.out.println("4 Salir");
        Scanner sc = new Scanner(System.in);
        try {
            List<Vertex<DecoratedElement<Personaje>>> vertices = iterableToList(grafo.getVertices());
            switch (sc.nextInt()) {
                case 1:
                    System.out.println("Cantidad de personajes: " + grafo.getN());
                    System.out.println("Cantidad de relaciones: " + grafo.getM());
                    mostarMayor(vertices, grafo);
                    mostarMenor(vertices, grafo);
                    //Menu(grafo);
                    break;
                case 2:
                    for (Vertex<DecoratedElement<Personaje>> vertex : vertices) {
                        System.out.println(vertex.getID());
                    }
                    sc.nextLine();
                    Vertex<DecoratedElement<Personaje>> inicio = obtenerPersonaje(vertices, sc);
                    Vertex<DecoratedElement<Personaje>> fin = obtenerPersonaje(vertices, sc);
                    DecoratedElement<Personaje> inicioPersonaje = inicio.getElement();
                    if (realizarCamino(grafo, fin, inicio)) {
                        recorrerCamino(inicioPersonaje);
                        System.out.print(fin.getID());
                    } else
                        System.out.println("Camino no encontrado");
                    //Menu(grafo);
                    break;
                case 3:
                    vertices.forEach(a -> System.out.println(a.getID()));
                    sc.nextLine();
                    inicio = obtenerPersonaje(vertices, sc);
                    fin = obtenerPersonaje(vertices, sc);
                    equipoVertices(grafo, fin, inicio);
                    if (inicio.getElement().getParent() != null) {
                        recorrerCamino(inicio.getElement());
                        System.out.println(fin.getID());
                    } else
                        System.out.println("no se pudo hacer un equipo entre " + inicio.getID() + " y " + fin.getID());
                   // Menu(grafo);
                    break;
                case 4:
                    System.out.println("Nos vemos");
                    sc.close();
                    return;
                    //break;
                default:
                    System.out.println("Opcion no valida: ");
                    //Menu(grafo)

            }
        } catch (InputMismatchException e) {
            System.out.println("Opcion no valida: ");
           // Menu(grafo);
        }
       }
    }

    /**
     * Imprime el personaje con más relaciones y el personaje con menos relaciones
     * 
     * @param personajes Lista de vértices
     * @param grafo         Gráfico<ElementoDecorado<Personaje>, Entero>
     */
    private static void mostarMenor(List<Vertex<DecoratedElement<Personaje>>> personajes,
            Graph<DecoratedElement<Personaje>, Integer> grafo) {

        try {
            System.out.println(
                    "El personaje con menor número de relaciones es: " + personajes.get(personajes.size() - 1).getID());
            for (int i = personajes.size() - 2; i >= 0; i--)
                if (iterableToList2(grafo.incidentEdges(personajes.get(i)))
                        .size() == iterableToList2(grafo.incidentEdges(personajes.get(personajes.size() - 1))).size())
                    System.out.println("El personaje con menor número de relaciones es: " + personajes.get(i).getID());

        } catch (NoSuchElementException e) {
            System.out.println("NO hay elementos en el grafo");
        }
    }

    /**
     * Imprime el carácter con el menor número de relaciones.
     * 
     * @param personajes Lista de vértices
     * @param grafo         Gráfico<ElementoDecorado<Personaje>, Entero>
     */
    private static void mostarMayor(List<Vertex<DecoratedElement<Personaje>>> personajes,
            Graph<DecoratedElement<Personaje>, Integer> grafo) {
        try {
            System.out.println("El personaje con mayor número de relaciones es: " + personajes.get(0).getID());
            for (int i = 1; i < personajes.size() - 1; i++)
                if (iterableToList2(grafo.incidentEdges(personajes.get(i)))
                        .size() == iterableToList2(grafo.incidentEdges(personajes.get(0))).size())
                    System.out.println("El personaje con mayor número de relaciones es: " + personajes.get(i).getID());

        } catch (NoSuchElementException e) {
            System.out.println("NO hay elementos en el grafo");
        }
    }

    /**
     * Le pide al usuario un nombre, si el nombre está vacío vuelve a llamarse a sí
     * mismo, si el nombre
     * no está vacío comprueba si el nombre está en la lista de vértices, si lo está
     * devuelve el
     * vértice, si no lo está llama mismo de nuevo
     * 
     * @param personajes Lista de vértices de tipo DecoratedElement<Personaje>
     * @param scanner    Objeto de escáner
     * @return Un vértice de un elemento decorado de un personaje.
     */
    private static Vertex<DecoratedElement<Personaje>> obtenerPersonaje(
            List<Vertex<DecoratedElement<Personaje>>> personajes, Scanner scanner) {
        System.out.println("Dime el nombre de un personaje:");
        String nombre = scanner.nextLine();
        if (nombre.equals(""))
            return obtenerPersonaje(personajes, scanner);
        else {
            for (Vertex<DecoratedElement<Personaje>> vertex : personajes)
                if (vertex.getID().equals(nombre))
                    return vertex;
            return obtenerPersonaje(personajes, scanner);
        }

    }

    /**
     * Toma un gráfico, un vértice inicial y un vértice final y devuelve verdadero
     * si hay una ruta
     * desde el vértice inicial hasta el vértice final
     * 
     * @param graph  La gráfica
     * @param inicio El vértice inicial
     * @param fin    El vértice al que queremos llegar
     * @return un valor booleano.
     */
    private static boolean realizarCamino(Graph<DecoratedElement<Personaje>, Integer> graph,
            Vertex<DecoratedElement<Personaje>> inicio, Vertex<DecoratedElement<Personaje>> fin) {
        LinkedList<Vertex<DecoratedElement<Personaje>>> queue = new LinkedList<Vertex<DecoratedElement<Personaje>>>();
        queue.add(inicio);
        boolean acabar = false;
        do {
            Vertex<DecoratedElement<Personaje>> v = queue.poll();
            Iterator<Edge<Integer>> vertices = graph.incidentEdges(v);
            while (vertices.hasNext() && !acabar) {
                Vertex<DecoratedElement<Personaje>> adVertex = graph.opposite(v, vertices.next());
                try {
                    acabar = adVertex.equals(fin);
                    if ((adVertex.getElement().getParent() == null) && !(adVertex.equals(inicio))) {
                        queue.add(adVertex);
                        adVertex.getElement().setParent(v.getElement());
                    }
                } catch (NullPointerException e) {
                }
            }
        } while (fin.getElement().getParent() == null && !queue.isEmpty() && !acabar);
        return acabar;
    }

    /**
     * Toma un gráfico, un vértice inicial y un vértice final, y devuelve el vértice
     * inicial
     * 
     * @param graph  La gráfica
     * @param inicio el vértice inicial
     * @param fin    El vértice al que quiero llegar
     * @return El método devuelve el vértice que es el punto inicial del gráfico.
     */
    private static Vertex<DecoratedElement<Personaje>> equipoVertices(Graph<DecoratedElement<Personaje>, Integer> graph,
            Vertex<DecoratedElement<Personaje>> inicio,
            Vertex<DecoratedElement<Personaje>> fin) {
        Edge<Integer> e = null;
        inicio.getElement().setVisited(true);
        Iterator<Edge<Integer>> it = graph.incidentEdges(inicio);
        Vertex<DecoratedElement<Personaje>> nodoAux = null;
        while (it.hasNext() && !(inicio.equals(fin))) {
            e = it.next();
            nodoAux = graph.opposite(inicio, e);
            if (!nodoAux.getElement().isVisited() && e.getElement() < 10) {
                nodoAux.getElement().setParent(inicio.getElement());
                equipoVertices(graph, nodoAux, fin);
            }
        }
        return inicio;
    }

    /**
     * Imprime la ruta desde la raíz hasta el nodo actual.
     * 
     * @param personaje El personaje al que quieres encontrar el realizarCamino.
     */
    private static void recorrerCamino(DecoratedElement<Personaje> personaje) {
        while (personaje.getParent() != null) {
            System.out.print(personaje.getID() + " -> ");
            personaje = personaje.getParent();
        }
    }

    /**
     * Toma un iterador y devuelve una lista.
     * 
     * @param iterator el iterador para convertir a una lista
     * @return Una lista de vértices.
     */
    private static List<Vertex<DecoratedElement<Personaje>>> iterableToList(
            Iterator<Vertex<DecoratedElement<Personaje>>> iterator) {
        List<Vertex<DecoratedElement<Personaje>>> list = new ArrayList<>();
        while (iterator.hasNext())
            list.add(iterator.next());
        return list;
    }

    /**
     * Toma un iterador y devuelve una lista de los elementos en el iterador
     * 
     * @param iterator el iterador para convertir a una lista
     * @return Una lista de bordes.
     */
    private static List<Edge<Integer>> iterableToList2(Iterator<Edge<Integer>> iterator) {
        List<Edge<Integer>> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    // Cargando los datos del archivo "personajes.txt" en un gráfico.
    private static Graph<DecoratedElement<Personaje>, Integer> cargarDatosPruebas() {
        Graph<DecoratedElement<Personaje>, Integer> grafo = new TreeMapGraph<DecoratedElement<Personaje>, Integer>();
        Personaje[] personajes = new Personaje[10];
        for (int i = 0; i < 10; i++)
            personajes[i] = new Personaje("" + (char) ('a' + i));
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[0]), new DecoratedElement<Personaje>(personajes[1]),
                2);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[0]), new DecoratedElement<Personaje>(personajes[2]),
                8);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[0]), new DecoratedElement<Personaje>(personajes[4]),
                12);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[0]), new DecoratedElement<Personaje>(personajes[3]),
                24);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[2]), new DecoratedElement<Personaje>(personajes[5]),
                6);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[4]), new DecoratedElement<Personaje>(personajes[5]),
                4);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[5]), new DecoratedElement<Personaje>(personajes[6]),
                3);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[6]), new DecoratedElement<Personaje>(personajes[7]),
                5);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[6]), new DecoratedElement<Personaje>(personajes[8]),
                35);
        grafo.insertEdge(new DecoratedElement<Personaje>(personajes[8]), new DecoratedElement<Personaje>(personajes[9]),
                7);
        return grafo;
    }
}
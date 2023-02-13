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

public class Principal {

    public static void main(String[] args) {
        Graph<Personaje, Integer> ge=Cargar();
        Graph<Personaje,Integer> grafo=cargarDatosPruebas();
        Menu(grafo);
        //Menu(ge);
    }

    public static Graph<Personaje, Integer> Cargar(){
        Graph<Personaje, Integer> g = new TreeMapGraph<Personaje,Integer>();
        try {
           // FileReader ficheroCSV = new FileReader("marvel-unimodal-edges.csv"); // Ruta en el sistema del fichero de datos
           Scanner lector = new Scanner(new File("marvel-unimodal-edges.csv")); // Buffer de entrada
            String linea = lector.nextLine(); // Inicio de la lectura del fichero
            do {
                linea = lector.nextLine();
                String[] splitted = linea.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                g.insertEdge(new Personaje(splitted[0]),new Personaje(splitted[1]),Integer.valueOf(splitted[2]));
            } while (lector.hasNextLine());
            lector.close();
            return g; // Devolvemos el array con todos los heroes encontrados y sus datos.
        } catch (IOException ex) {
            //FicheroNoEncontrado();
            System.exit(1);
            return null;
        } 

    }

    public static void Menu(Graph<Personaje, Integer> ge) {
        System.out.println("\n\n\n\nMenu Principal");
        System.out.println("Selecciona una opción");
        System.out.println("1 Mostar datos de los personajes");
        System.out.println("2 Conexión entre personajes");
        System.out.println("3 Formar equipo con dos personajes");
        System.out.println("4 Salir");
        Scanner sc = new Scanner(System.in);
        try {
            List<Vertex<Personaje>> vertices = iterableToList(ge.getVertices());
            switch(sc.nextInt()){
                case 1:
                    System.out.println("Numero de personajes:" + ge.getN());
                    System.out.println("Numeros de relaciones:" + ge.getM());
                    
                    for (Vertex<Personaje> vertex : vertices) {
                        System.out.println(vertex.getID());
                    }
                    mostarMayor(vertices,ge);
                    mostarMenor(vertices, ge);
                    Menu(ge);
                    break;
                case 2:
                    for (Vertex<Personaje> vertex : vertices) {
                        System.out.println(vertex.getID());
                    }
                    sc = new Scanner(System.in);
                    Vertex<Personaje> inicio = devolverPersonaje(vertices, sc);
                    Vertex<Personaje> fin = devolverPersonaje(vertices, sc);
                    Personaje finPersonaje= fin.getElement();
                    if(camino(ge, inicio, fin))
                    {
                        System.out.print(inicio.getID());
                        recorreCamino(finPersonaje);
                    }else
                    System.out.println("Camino no encontrado");
                    Menu(ge);
                    break;
                case 3:
                    vertices.forEach(a -> System.out.println(a.getID()));
                    sc = new Scanner(System.in);
                    inicio = devolverPersonaje(vertices, sc);
                    fin = devolverPersonaje(vertices, sc);
                    equipoVertices(ge, inicio, fin);
                    if (fin.getElement().getParent() != null) {
                        recorreCamino(fin.getElement());
                        System.out.println("");
                    } else
                        System.out.println("no se pudo hacer un equipo entre " + inicio.getID() + " y " + fin.getID());
                    Menu(ge);
                    break;
                case 4:
                    System.out.println("Nos vemos");
                    sc.close();
                    break;
                default:
                    System.out.println("Opcion no valida: ");
                    Menu(ge);
                    
            }
        } catch (InputMismatchException e) {
            System.out.println("Opcion no valida: ");
            Menu(ge);
        }
    }
   
    private static void mostarMayor(List<Vertex<Personaje>> personajes,Graph<Personaje, Integer> ge) {
       
        try {
            System.out.println("El personaje con mayor número de relaciones es: "+ personajes.get(personajes.size()-1).getID());
            for(int i= personajes.size()-2;i>=0;i--)
            if(iterableToList2(ge.incidentEdges(personajes.get(i))).size()==
            iterableToList2(ge.incidentEdges(personajes.get(personajes.size()-1))).size())
                System.out.println("El personaje con menor número de relaciones es: "+personajes.get(i).getID());
          
        } catch (NoSuchElementException e) {
            System.out.println("NO hay elementos en el grafo");
        }
    }
    private static void mostarMenor(List<Vertex<Personaje>> personajes, Graph<Personaje, Integer> ge) {
        try {
            System.out.println("El personaje con menor número de relaciones es: "+ personajes.get(0).getID());
            for(int i= 1;i<personajes.size()-1;i++)
            if(iterableToList2(ge.incidentEdges(personajes.get(i))).size()==
            iterableToList2(ge.incidentEdges(personajes.get(0))).size())
                System.out.println("El personaje con menor número de relaciones es: "+personajes.get(i).getID());
          
        } catch (NoSuchElementException e) {
            System.out.println("NO hay elementos en el grafo");
        }
    }

    private static Vertex<Personaje> devolverPersonaje(List<Vertex<Personaje>> personajes, Scanner scanner)
    {
        System.out.println("Dime el nombre de un personaje:");
        String nombre= scanner.nextLine();
        if(nombre.equals(""))
        return devolverPersonaje(personajes, scanner);
        else{
            for (Vertex<Personaje> vertex : personajes)
                if(vertex.getID().equals(nombre))
                return vertex;
            return devolverPersonaje(personajes, scanner);
        }

    }
    private static boolean camino(Graph<Personaje, Integer> graph, Vertex<Personaje> inicio, Vertex<Personaje> fin) {
        LinkedList<Vertex<Personaje>> queue = new LinkedList<Vertex<Personaje>>();
        queue.add(inicio);
        boolean acabar= false;
        do {
            Vertex<Personaje> v = queue.poll();
            Iterator<Edge<Integer>> vertices = graph.incidentEdges(v);
            while (vertices.hasNext() && !acabar) {
                Vertex<Personaje> adVertex = graph.opposite(v, vertices.next());
                try {
                    acabar=adVertex.equals(fin);
                    if ((adVertex.getElement().getParent() == null)&& !(adVertex.equals(inicio))) {
                        queue.add(adVertex);
                        adVertex.getElement().setParent(v.getElement());
                    }
                } catch (NullPointerException e) {
                }
            }
        } while (fin.getElement().getParent() == null && !queue.isEmpty() && !acabar);
            return acabar;
    }

    private static Vertex<Personaje> equipoVertices(Graph<Personaje, Integer> graph, Vertex<Personaje> inicio,
    Vertex<Personaje> fin) {
        Edge<Integer> e = null;
        inicio.getElement().setVisited(true);
        Iterator<Edge<Integer>> it = graph.incidentEdges(inicio);
        Vertex<Personaje> nodoAux = null;
        while (it.hasNext() && !(inicio.equals(fin))) {
            e = it.next();
            nodoAux = graph.opposite(inicio, e);
            if (!nodoAux.getElement().isVisited() && e.getElement() < 10) {
                nodoAux.getElement().setParent(inicio.getElement());
                equipoVertices(graph, nodoAux, fin); 
            }}
        return inicio;
    }

    private static void recorreCamino(Personaje personaje){
        while(personaje.getParent()!=null)
                        {
                            System.out.println(" -> "+personaje.getID());
                            personaje=personaje.getParent();
                        }
                        System.out.println("");
    }

    private static List<Vertex<Personaje>> iterableToList(Iterator<Vertex<Personaje>> iterator) {
        List<Vertex<Personaje>> list = new ArrayList<>();
        while (iterator.hasNext())
            list.add(iterator.next());
        return list;
    }
    private static List<Edge<Integer>> iterableToList2(Iterator<Edge<Integer>> iterator) {
        List<Edge<Integer>> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
    private static Graph<Personaje, Integer> cargarDatosPruebas() {
        Graph<Personaje, Integer> grafo = new TreeMapGraph<Personaje, Integer>();
        Personaje[] personajes = new Personaje[10];
        // Personaje pers = new Personaje("l");
        for (int i = 0; i < 10; i++)
            personajes[i] = new Personaje("" + (char) ('a' + i));
        grafo.insertEdge(personajes[0], personajes[1], 2);
        grafo.insertEdge(personajes[0], personajes[2], 8);
        grafo.insertEdge(personajes[0], personajes[3], 24);
        grafo.insertEdge(personajes[0], personajes[4], 12);
        grafo.insertEdge(personajes[2], personajes[5], 6);
        grafo.insertEdge(personajes[4], personajes[5], 4);
        grafo.insertEdge(personajes[5], personajes[6], 3);
        grafo.insertEdge(personajes[6], personajes[7], 5);
        grafo.insertEdge(personajes[6], personajes[8], 35);
        grafo.insertEdge(personajes[8], personajes[9], 7);
        return grafo;
    }
}
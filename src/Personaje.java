import Grafo.Element;
import Grafo.Vertex;

public class Personaje implements  Element{
    private String nombre;
    private boolean visited;
    private Personaje parent;
    public Personaje(String nombre){
        this.nombre= nombre;
        visited= false;
    }
    @Override
    public String getID() {
        return nombre;
    }

    public boolean isVisited() {
        return visited;
    }

    public Personaje getParent() {
        return parent;
    }

    public void setVisited(boolean visited){
        this.visited= visited;
    }

    public void setParent(Personaje parent){
        this.parent= parent;
    }

}

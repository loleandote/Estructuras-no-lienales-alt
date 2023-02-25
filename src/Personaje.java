import Grafo.Element;

public class Personaje implements Element{
    private String nombre;
    public Personaje(String nombre){
        this.nombre= nombre;
    }
    @Override
    public String getID() {
        return nombre;
    }
}

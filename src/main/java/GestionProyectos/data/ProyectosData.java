package GestionProyectos.data;

import GestionProyectos.Logic.Proyecto;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "proyectos")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProyectosData {
    @XmlElement(name = "proyecto")
    private List<Proyecto> proyectos = new ArrayList<>();

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }
}

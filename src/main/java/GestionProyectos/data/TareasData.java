package GestionProyectos.data;

import GestionProyectos.Logic.Tarea;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "tareas")
@XmlAccessorType(XmlAccessType.FIELD)
public class TareasData {
    @XmlElement(name = "tarea")
    private List<Tarea> tareas = new ArrayList<>();

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }
}

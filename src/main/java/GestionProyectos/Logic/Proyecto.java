package GestionProyectos.Logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@XmlRootElement(name = "proyecto")
@XmlAccessorType(XmlAccessType.FIELD)
public class Proyecto {
    private int codigo;
    private String descripcion;

    @XmlElement(name = "encargado")
    private User encargadoGeneral;

    @XmlTransient // no persistimos las tareas dentro de archivo.xml
    private List<Tarea> tareas = new ArrayList<>();

    public Proyecto() {
    }

    public Proyecto(String descripcion, User encargadoGeneral) {
        this.codigo = ThreadLocalRandom.current().nextInt(1000, 10000);
        this.descripcion = descripcion;
        this.encargadoGeneral = encargadoGeneral;
    }

    public void agregarTarea(Tarea tarea) {
        tarea.setCodigoProyecto(this.codigo);
        tareas.add(tarea);
        try {
            GestionProyectos.data.TareasData dataTareas = new GestionProyectos.data.TareasData();
            for (Tarea t : tareas) {
                dataTareas.getTareas().add(t);
            }
            new GestionProyectos.data.XmlPersister<>("tarea.xml", GestionProyectos.data.TareasData.class).store(dataTareas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public User getEncargadoGeneral() {
        return encargadoGeneral;
    }

    public void setEncargadoGeneral(User encargadoGeneral) {
        this.encargadoGeneral = encargadoGeneral;
    }
}

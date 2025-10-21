package GestionProyectos.MVC;

import GestionProyectos.ClasesGenericas.AbstractModel;
import GestionProyectos.Logic.Proyecto;
import GestionProyectos.Logic.Tarea;
import GestionProyectos.Logic.User;

import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {

    public static final String PROYECTOS_PROPERTY = "listProyectos";
    public static final String USUARIOS_PROPERTY = "listUsuarios";
    public static final String PROYECTO_ACTUALIZADO = "proyectoActualizado";

    private List<Proyecto> listProyectos;
    private List<User> listUsuarios;

    public Model() {
        listProyectos = new ArrayList<>();
        listUsuarios = new ArrayList<>();
    }

    public List<Proyecto> getListProyectos() {
        return listProyectos;
    }

    public List<User> getListUsuarios() {
        return listUsuarios;
    }

    public void setListProyectos(List<Proyecto> proyectos) {
        List<Proyecto> old = this.listProyectos;
        this.listProyectos = proyectos != null ? proyectos : new ArrayList<>();
        firePropertyChange(PROYECTOS_PROPERTY, old, this.listProyectos);
    }

    public void setListUsuarios(List<User> usuarios) {
        List<User> old = this.listUsuarios;
        this.listUsuarios = usuarios != null ? usuarios : new ArrayList<>();
        firePropertyChange(USUARIOS_PROPERTY, old, this.listUsuarios);
    }

    public void addProyecto(Proyecto p) {
        if (p == null) return;
        List<Proyecto> old = new ArrayList<>(listProyectos);
        listProyectos.add(p);
        firePropertyChange(PROYECTOS_PROPERTY, old, listProyectos);

        try {
            GestionProyectos.data.ProyectosData dataProyectos = new GestionProyectos.data.ProyectosData();
            dataProyectos.setProyectos(listProyectos);
            new GestionProyectos.data.XmlPersister<>("archivo.xml", GestionProyectos.data.ProyectosData.class)
                    .store(dataProyectos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addTareaAProyecto(Proyecto proyecto, Tarea tarea) {
        if (proyecto == null || tarea == null) return;
        proyecto.agregarTarea(tarea);
        firePropertyChange(PROYECTO_ACTUALIZADO, null, proyecto);
        firePropertyChange(PROYECTOS_PROPERTY, null, listProyectos);
    }

    public void notificarProyectoActualizado(Proyecto proyecto) {
        firePropertyChange(PROYECTO_ACTUALIZADO, null, proyecto);
    }
}

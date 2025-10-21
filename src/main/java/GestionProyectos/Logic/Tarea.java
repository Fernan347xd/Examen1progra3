package GestionProyectos.Logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tarea")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tarea {
    private static int contador = 1;

    private int numero;
    private String descripcion;
    private String fechaFinalizacion;
    private String prioridad;
    private String estado;
    private User user;

    private int codigoProyecto;

    public Tarea() {
    }

    public Tarea(String descripcion, java.time.LocalDate fechaFinalizacion,
                 String prioridad, String estado, User user) {
        this.numero = contador++;
        this.descripcion = descripcion;
        this.fechaFinalizacion = fechaFinalizacion.toString();
        this.prioridad = prioridad;
        this.estado = estado;
        this.user = user;
    }

    // getters y setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(String fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getCodigoProyecto() { return codigoProyecto; }
    public void setCodigoProyecto(int codigoProyecto) { this.codigoProyecto = codigoProyecto; }
}

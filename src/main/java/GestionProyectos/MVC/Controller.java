package GestionProyectos.MVC;

import GestionProyectos.Logic.Proyecto;
import GestionProyectos.Logic.Tarea;
import GestionProyectos.Logic.User;
import GestionProyectos.MainPanel;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Controller {

    private final Model model;
    private final MainPanel view;

    public Controller(Model model, MainPanel view) {
        this.model = model;
        this.view = view;
        initController();
    }

    public void crearProyecto(User user) {
        String descripcion = view.getDescripcionJtext().getText().trim();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(view.getPrincipal(),
                    "La descripción no puede estar vacía",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user == null) {
            JOptionPane.showMessageDialog(view.getPrincipal(),
                    "Debe seleccionar un encargado",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Proyecto nuevo = new Proyecto(descripcion, user);
        model.addProyecto(nuevo);

        view.getDescripcionJtext().setText("");
        view.getTablaProyectos().updateUI();
    }

    private void initController() {
        view.getCrearButton().addActionListener(e -> {
            User usuario = (User) view.getEncargadoJcombobox().getSelectedItem();
            crearProyecto(usuario);
        });

        if (view.getCrearTareaButton() != null) {
            view.getCrearTareaButton().addActionListener(e -> crearTarea());
        }

        view.getTablaProyectos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTablaProyectos().getSelectedRow();
                if (row >= 0) {
                    Proyecto seleccionado = model.getListProyectos().get(row);
                    model.notificarProyectoActualizado(seleccionado);
                } else {
                    model.notificarProyectoActualizado(null);
                }
            }
        });
    }

    public void crearTarea() {
        int row = view.getTablaProyectos().getSelectedRow();
        if (row < 0) {
            return;
        }

        Proyecto proyecto = model.getListProyectos().get(row);
        String descripcion = view.getDescripcionTarea().getText();
        String fechaStr = (String) view.getVenceTarea().getSelectedItem();
        LocalDate fecha = null;

        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                fecha = LocalDate.parse(fechaStr.trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(view.getPrincipal(),
                        "Formato de fecha inválido. Use YYYY-MM-DD",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String prioridad = (String) view.getPrioridadTarea().getSelectedItem();
        String estado = (String) view.getEstadoTarea().getSelectedItem();
        User usuario = (User) view.getResponsable().getSelectedItem();

        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view.getPrincipal(),
                    "La descripción de la tarea no puede estar vacía",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (usuario == null) {
            JOptionPane.showMessageDialog(view.getPrincipal(),
                    "Debe seleccionar un responsable",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Tarea tarea = new Tarea(descripcion.trim(), fecha, prioridad, estado, usuario);
        model.addTareaAProyecto(proyecto, tarea);

        // limpiar campos de tarea
        view.getDescripcionTarea().setText("");
        view.getVenceTarea().setSelectedIndex(0);
        view.getPrioridadTarea().setSelectedIndex(0);
        view.getEstadoTarea().setSelectedIndex(0);
        view.getResponsable().setSelectedIndex(-1);
    }
}
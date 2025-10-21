package GestionProyectos;

import GestionProyectos.Logic.Proyecto;
import GestionProyectos.Logic.Tarea;
import GestionProyectos.Logic.User;
import GestionProyectos.MVC.Controller;
import GestionProyectos.MVC.Model;
import GestionProyectos.MVC.ProyectoTableModel;
import GestionProyectos.MVC.TareaTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainPanel extends JPanel implements PropertyChangeListener {

    private JPanel Principal;
    private JPanel Proyectos;
    private JButton CrearButton;
    private JTextField DescripcionJtext;
    private JComboBox<User> EncargadoJcombobox;
    private JTable TablaProyectos;
    private JPanel TareasJpanel;
    private JTable TablaTareas;
    private JLabel textoPanel2;
    private JButton CrearTarea;
    private JTextField DescripcionTarea;
    private JComboBox<String> VenceTarea;  // Cambiado a String para manejar fechas
    private JComboBox<String> PrioridadTarea;
    private JComboBox<String> EstadoTarea;
    private JComboBox<User> Responsable;
    private JButton EditarButton;

    private Model model;
    private Controller controller;
    private ProyectoTableModel proyectoTableModel;
    private TareaTableModel tareaTableModel;
    private Proyecto proyectoSeleccionado;

    public MainPanel() {
        EditarButton.addActionListener(e -> {
            if (proyectoSeleccionado == null) {
                JOptionPane.showMessageDialog(MainPanel.this,
                        "Debe seleccionar un proyecto primero",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int row = TablaTareas.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(MainPanel.this,
                        "Debe seleccionar una tarea para editar",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Tarea tareaSeleccionada = proyectoSeleccionado.getTareas().get(row);

            JComboBox<String> comboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
            comboPrioridad.setSelectedItem(tareaSeleccionada.getPrioridad());

            JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Pendiente", "En progreso", "Completada"});
            comboEstado.setSelectedItem(tareaSeleccionada.getEstado());

            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            panel.add(new JLabel("Seleccione la nueva prioridad:"));
            panel.add(comboPrioridad);
            panel.add(new JLabel("Seleccione el nuevo estado:"));
            panel.add(comboEstado);

            int result = JOptionPane.showConfirmDialog(
                    MainPanel.this,
                    panel,
                    "Editando tarea " + tareaSeleccionada.getNumero(),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                tareaSeleccionada.setPrioridad((String) comboPrioridad.getSelectedItem());
                tareaSeleccionada.setEstado((String) comboEstado.getSelectedItem());

                model.notificarProyectoActualizado(proyectoSeleccionado);
                tareaTableModel.fireTableDataChanged();
                proyectoTableModel.fireTableDataChanged();

                JOptionPane.showMessageDialog(MainPanel.this,
                        "Tarea actualizada exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);

        proyectoTableModel = new ProyectoTableModel(
                new int[]{
                        ProyectoTableModel.CODIGO,
                        ProyectoTableModel.DESCRIPCION,
                        ProyectoTableModel.ENCARGADO,
                        ProyectoTableModel.NUM_TAREAS
                },
                model.getListProyectos()
        );
        TablaProyectos.setModel(proyectoTableModel);

        tareaTableModel = new TareaTableModel(
                new int[]{
                        TareaTableModel.NUMERO,
                        TareaTableModel.DESCRIPCION,
                        TareaTableModel.VENCE,
                        TareaTableModel.PRIORIDAD,
                        TareaTableModel.ESTADO,
                        TareaTableModel.USUARIO
                },
                List.of()
        );
        TablaTareas.setModel(tareaTableModel);

        EncargadoJcombobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    setText(((User) value).getName());
                }
                return this;
            }
        });

        Responsable.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    setText(((User) value).getName());
                }
                return this;
            }
        });

        inicializarComboBoxesTarea();

        textoPanel2.setText("No hay proyecto seleccionado");

        habilitarControlesTareas(false);
    }

    private void inicializarComboBoxesTarea() {
        PrioridadTarea.setModel(new DefaultComboBoxModel<>(
                new String[]{"Alta", "Media", "Baja"}
        ));

        EstadoTarea.setModel(new DefaultComboBoxModel<>(
                new String[]{"Pendiente", "En progreso", "Completada"}
        ));

        DefaultComboBoxModel<String> modeloFechas = new DefaultComboBoxModel<>();
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        modeloFechas.addElement("");

        for (int i = 0; i <= 30; i++) {
            modeloFechas.addElement(hoy.plusDays(i).format(formatter));
        }

        VenceTarea.setModel(modeloFechas);
        VenceTarea.setEditable(true); // Permitir entrada manual de fechas
    }

    private void habilitarControlesTareas(boolean habilitar) {
        CrearTarea.setEnabled(habilitar);
        DescripcionTarea.setEnabled(habilitar);
        VenceTarea.setEnabled(habilitar);
        PrioridadTarea.setEnabled(habilitar);
        EstadoTarea.setEnabled(habilitar);
        Responsable.setEnabled(habilitar);
    }

    public void setController(Controller controller) {
        this.controller = controller;

        TablaProyectos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = TablaProyectos.getSelectedRow();
                if (row >= 0) {
                    proyectoSeleccionado = model.getListProyectos().get(row);
                    model.notificarProyectoActualizado(proyectoSeleccionado);
                    habilitarControlesTareas(true);
                } else {
                    proyectoSeleccionado = null;
                    model.notificarProyectoActualizado(null);
                    habilitarControlesTareas(false);
                }
            }
        });

        CrearTarea.addActionListener(e -> {
            if (proyectoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un proyecto primero",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String descripcion = DescripcionTarea.getText().trim();
            String fechaStr = (String) VenceTarea.getSelectedItem();
            String prioridad = (String) PrioridadTarea.getSelectedItem();
            String estado = (String) EstadoTarea.getSelectedItem();
            User responsable = (User) Responsable.getSelectedItem();

            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese una descripción para la tarea",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (responsable == null) {
                JOptionPane.showMessageDialog(this,
                        "Por favor seleccione un responsable",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fechaVencimiento = null;
            if (fechaStr != null && !fechaStr.trim().isEmpty()) {
                try {
                    fechaVencimiento = LocalDate.parse(fechaStr.trim());
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Formato de fecha inválido. Use YYYY-MM-DD",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Tarea nuevaTarea = new Tarea(descripcion, fechaVencimiento, prioridad, estado, responsable);
            proyectoSeleccionado.agregarTarea(nuevaTarea);
            model.notificarProyectoActualizado(proyectoSeleccionado);
            proyectoTableModel.fireTableDataChanged();

            DescripcionTarea.setText("");
            VenceTarea.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this,
                    "Tarea creada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();

        if (Model.PROYECTOS_PROPERTY.equals(prop)) {
            proyectoTableModel = new ProyectoTableModel(
                    new int[]{
                            ProyectoTableModel.CODIGO,
                            ProyectoTableModel.DESCRIPCION,
                            ProyectoTableModel.ENCARGADO,
                            ProyectoTableModel.NUM_TAREAS
                    },
                    model.getListProyectos()
            );
            TablaProyectos.setModel(proyectoTableModel);

        } else if (Model.USUARIOS_PROPERTY.equals(prop)) {
            EncargadoJcombobox.setModel(
                    new DefaultComboBoxModel<>(model.getListUsuarios().toArray(new User[0]))
            );
            Responsable.setModel(
                    new DefaultComboBoxModel<>(model.getListUsuarios().toArray(new User[0]))
            );

        } else if (Model.PROYECTO_ACTUALIZADO.equals(prop)) {
            Proyecto p = (Proyecto) evt.getNewValue();
            if (p != null) {
                textoPanel2.setText("Proyecto: " + p.getDescripcion());
                tareaTableModel = new TareaTableModel(
                        new int[]{
                                TareaTableModel.NUMERO,
                                TareaTableModel.DESCRIPCION,
                                TareaTableModel.VENCE,
                                TareaTableModel.PRIORIDAD,
                                TareaTableModel.ESTADO,
                                TareaTableModel.USUARIO
                        },
                        p.getTareas()
                );
                TablaTareas.setModel(tareaTableModel);
            } else {
                textoPanel2.setText("No hay proyecto seleccionado");
                tareaTableModel = new TareaTableModel(
                        new int[]{
                                TareaTableModel.NUMERO,
                                TareaTableModel.DESCRIPCION,
                                TareaTableModel.VENCE,
                                TareaTableModel.PRIORIDAD,
                                TareaTableModel.ESTADO,
                                TareaTableModel.USUARIO
                        },
                        List.of()
                );
                TablaTareas.setModel(tareaTableModel);
            }
        }
    }

    public JButton getCrearButton() { return CrearButton; }
    public JTextField getDescripcionJtext() { return DescripcionJtext; }
    public JComboBox<User> getEncargadoJcombobox() { return EncargadoJcombobox; }
    public JTable getTablaProyectos() { return TablaProyectos; }
    public JTable getTablaTareas() { return TablaTareas; }
    public JPanel getPrincipal() { return Principal; }
    public JButton getCrearTareaButton() { return CrearTarea; }
    public JTextField getDescripcionTarea() { return DescripcionTarea; }
    public JComboBox<String> getVenceTarea() { return VenceTarea; }
    public JComboBox<String> getPrioridadTarea() { return PrioridadTarea; }
    public JComboBox<String> getEstadoTarea() { return EstadoTarea; }
    public JComboBox<User> getResponsable() { return Responsable; }

    private void setButtonIcon(JButton button, String resourcePath) {
        java.net.URL url = getClass().getResource(resourcePath);
        if (url != null) {
            ImageIcon iconoOriginal = new ImageIcon(url);
            Image img = iconoOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        }
    }

}
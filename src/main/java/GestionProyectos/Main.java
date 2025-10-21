import GestionProyectos.Logic.Proyecto;
import GestionProyectos.Logic.Tarea;
import GestionProyectos.Logic.User;
import GestionProyectos.MVC.Controller;
import GestionProyectos.MVC.Model;
import GestionProyectos.MainPanel;
import GestionProyectos.data.*;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Model model = new Model();
                MainPanel view = new MainPanel();
                Controller controller = new Controller(model, view);

                view.setModel(model);
                view.setController(controller);

                XmlPersister<Data> usuariosPersister = new XmlPersister<>("data.xml", Data.class);
                Data dataUsuarios;
                File fUsuarios = new File("data.xml");
                if (fUsuarios.exists()) {
                    dataUsuarios = usuariosPersister.load();
                } else {
                    dataUsuarios = new Data(); // vacío
                    usuariosPersister.store(dataUsuarios); // lo crea
                }
                model.setListUsuarios(dataUsuarios.getUsuarios());

                XmlPersister<ProyectosData> proyectosPersister = new XmlPersister<>("archivo.xml", ProyectosData.class);
                ProyectosData dataProyectos;
                File fProyectos = new File("archivo.xml");
                if (fProyectos.exists()) {
                    dataProyectos = proyectosPersister.load();
                } else {
                    dataProyectos = new ProyectosData();
                    proyectosPersister.store(dataProyectos);
                }
                for (Proyecto p : dataProyectos.getProyectos()) {
                    model.addProyecto(p);
                }

                XmlPersister<TareasData> tareasPersister = new XmlPersister<>("tarea.xml", TareasData.class);
                TareasData dataTareas;
                File fTareas = new File("tarea.xml");
                if (fTareas.exists()) {
                    dataTareas = tareasPersister.load();
                } else {
                    dataTareas = new TareasData();
                    tareasPersister.store(dataTareas);
                }

                for (Tarea t : dataTareas.getTareas()) {
                    for (Proyecto p : model.getListProyectos()) {
                        if (p.getCodigo() == t.getCodigoProyecto()) {
                            p.agregarTarea(t);
                        }
                    }
                }

                JFrame frame = new JFrame("Sistema de Gestión de Proyectos");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(view.getPrincipal());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al cargar o crear archivos XML",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
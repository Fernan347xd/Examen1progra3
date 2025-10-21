package GestionProyectos.MVC;

import GestionProyectos.ClasesGenericas.AbstractTableModel;
import GestionProyectos.Logic.Proyecto;

import java.util.List;

public class ProyectoTableModel extends AbstractTableModel<Proyecto> implements javax.swing.table.TableModel {

    public static final int CODIGO = 0;
    public static final int DESCRIPCION = 1;
    public static final int ENCARGADO = 2;
    public static final int NUM_TAREAS = 3;

    public ProyectoTableModel(int[] cols, List<Proyecto> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[CODIGO] = "Código";
        colNames[DESCRIPCION] = "Descripción";
        colNames[ENCARGADO] = "Encargado";
        colNames[NUM_TAREAS] = "N° Tareas";
    }

    @Override
    public Object getPropetyAt(Proyecto p, int columnIndex) {
        return switch (columnIndex) {
            case CODIGO -> p.getCodigo();
            case DESCRIPCION -> p.getDescripcion();
            case ENCARGADO -> p.getEncargadoGeneral().getName();
            case NUM_TAREAS -> p.getTareas().size();
            default -> null;
        };
    }
}

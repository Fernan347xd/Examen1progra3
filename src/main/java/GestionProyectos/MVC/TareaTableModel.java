package GestionProyectos.MVC;

import GestionProyectos.ClasesGenericas.AbstractTableModel;
import GestionProyectos.Logic.Tarea;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class TareaTableModel extends AbstractTableModel<Tarea> {

    private Icon Alta;
    private Icon Media;
    private Icon Baja;

    public static final int NUMERO = 0;
    public static final int DESCRIPCION = 1;
    public static final int VENCE = 2;
    public static final int PRIORIDAD = 3;
    public static final int ESTADO = 4;
    public static final int USUARIO = 5;

    public TareaTableModel(int[] cols, List<Tarea> rows) {
        super(cols, rows);
        Alta = new ImageIcon(TareaTableModel.class.getResource("/Icons/alta.png"));
        Media = new ImageIcon(TareaTableModel.class.getResource("/Icons/media.png"));
        Baja = new ImageIcon(TareaTableModel.class.getResource("/Icons/baja.png"));
    }

    @Override
    protected void initColNames() {
        colNames = new String[6];
        colNames[NUMERO] = "N°";
        colNames[DESCRIPCION] = "Descripción";
        colNames[VENCE] = "Vence";
        colNames[PRIORIDAD] = "Prioridad";
        colNames[ESTADO] = "Estado";
        colNames[USUARIO] = "Responsable";
    }

    @Override
    public String getColumnName(int column) {
        return colNames[cols[column]];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        if (cols[col] == PRIORIDAD) {
            return Icon.class;
        } else if (cols[col] == NUMERO) {
            return Integer.class;
        } else if (cols[col] == VENCE) {
            return LocalDate.class;
        }
        return super.getColumnClass(col);
    }

    @Override
    protected Object getPropetyAt(Tarea t, int col) {
        switch (cols[col]) {
            case NUMERO:
                return t.getNumero();
            case DESCRIPCION:
                return t.getDescripcion();
            case VENCE:
                return t.getFechaFinalizacion();
            case PRIORIDAD:
                if (t.getPrioridad() != null) {
                    switch (t.getPrioridad()) {
                        case "Alta": return Alta;
                        case "Media": return Media;
                        case "Baja": return Baja;
                    }
                }
                return null;
            case ESTADO:
                return t.getEstado();
            case USUARIO:
                return t.getUser().getName();
            default:
                return null;
        }
    }
}
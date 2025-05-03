package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

public abstract class Parameter {

    private final String value;

    protected Parameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public abstract boolean isValueValid();

    public abstract void appendToCommand(StringBuilder cmd);

    public abstract void appendToTable(DefaultTableModel tableModel);

}

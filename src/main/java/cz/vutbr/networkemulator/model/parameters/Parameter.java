package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

public abstract class Parameter {

    private final String name;
    private String value;

    protected Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isValueSet() {
        return value != null && !value.isEmpty();
    }

    public abstract void appendToCommand(StringBuilder cmd);

    public abstract void appendToTable(DefaultTableModel model);

}

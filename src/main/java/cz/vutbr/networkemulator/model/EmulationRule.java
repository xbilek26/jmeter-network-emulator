package cz.vutbr.networkemulator.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Parameter;

/**
 * Represents a network emulation rule with its identifier (classID), filter,
 * and a list of parameters.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class EmulationRule {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(EmulationRule.class);

    String classId;
    Filter filter;
    List<Parameter> parameters;

    public EmulationRule(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getHandleId() {
        return classId.substring(2);
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}

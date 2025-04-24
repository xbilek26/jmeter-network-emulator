package cz.vutbr.networkemulator.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Parameter;

/**
 * Represents a traffic class that holds network parameters.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class TrafficClass {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(TrafficClass.class);

    String name;
    Filter filter;
    List<Parameter> parameters;

    public TrafficClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

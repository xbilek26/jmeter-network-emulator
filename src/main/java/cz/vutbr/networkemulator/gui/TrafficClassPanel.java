package cz.vutbr.networkemulator.gui;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.gui.filter.FilterPanel;
import cz.vutbr.networkemulator.gui.parameters.CorruptionPanel;
import cz.vutbr.networkemulator.gui.parameters.DelayPanel;
import cz.vutbr.networkemulator.gui.parameters.DuplicationPanel;
import cz.vutbr.networkemulator.gui.parameters.LossPanel;
import cz.vutbr.networkemulator.gui.parameters.RatePanel;
import cz.vutbr.networkemulator.gui.parameters.ReorderingPanel;
import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Corruption;
import cz.vutbr.networkemulator.model.parameters.Delay;
import cz.vutbr.networkemulator.model.parameters.Duplication;
import cz.vutbr.networkemulator.model.parameters.Loss;
import cz.vutbr.networkemulator.model.parameters.Parameter;
import cz.vutbr.networkemulator.model.parameters.Rate;
import cz.vutbr.networkemulator.model.parameters.Reordering;

public class TrafficClassPanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(TrafficClassPanel.class);

    private final String PROPERTY_FILTER = "filter_";
    private final String PROPERTY_PARAMETERS = "parameters_";

    private FilterPanel filterPanel;
    private DelayPanel delayPanel;
    private ReorderingPanel reorderingPanel;
    private LossPanel lossPanel;
    private RatePanel ratePanel;
    private DuplicationPanel duplicationPanel;
    private CorruptionPanel corruptionPanel;

    private final String UNDERSCORE = "_";

    public TrafficClassPanel(String niName, String tcName) {
        setName(niName + UNDERSCORE + tcName);
        init();
    }

    public Filter getFilter() {
        Filter filter = new Filter();
        filter.setIpProtocol(filterPanel.getIpProtocol());
        filter.setSrcAddress(filterPanel.getSrcAddress());
        filter.setSrcSubnetMask(filterPanel.getSrcSubnetMask());
        filter.setDstAddress(filterPanel.getDstAddress());
        filter.setDstSubnetMask(filterPanel.getDstSubnetMask());
        filter.setSrcPort(filterPanel.getSrcPort());
        filter.setDstPort(filterPanel.getDstPort());
        filter.setIcmpType(filterPanel.getIcmpType());
        filter.setIcmpCode(filterPanel.getIcmpCode());

        return filter;
    }

    public List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Delay(
                delayPanel.getValue(),
                delayPanel.getJitter(),
                delayPanel.getCorrelation(),
                delayPanel.getDistribution()
        ));
        parameters.add(new Reordering(
                reorderingPanel.getValue(),
                reorderingPanel.getCorrelation()
        ));
        parameters.add(new Loss(
                lossPanel.getValue(),
                lossPanel.getCorrelation()
        ));
        parameters.add(new Rate(
                ratePanel.getValue(),
                ratePanel.getOverhead()
        ));
        parameters.add(new Duplication(
                duplicationPanel.getValue(),
                duplicationPanel.getCorrelation()
        ));
        parameters.add(new Corruption(
                corruptionPanel.getValue()
        ));

        return parameters;
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        filterPanel = new FilterPanel();
        delayPanel = new DelayPanel();
        reorderingPanel = new ReorderingPanel();
        lossPanel = new LossPanel();
        ratePanel = new RatePanel();
        duplicationPanel = new DuplicationPanel();
        corruptionPanel = new CorruptionPanel();

        add(filterPanel);
        add(delayPanel);
        add(reorderingPanel);
        add(lossPanel);
        add(ratePanel);
        add(duplicationPanel);
        add(corruptionPanel);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setComponentsEnabled(this, enabled);
    }

    private void setComponentsEnabled(Container container, boolean enabled) {
        for (Component component : container.getComponents()) {
            component.setEnabled(enabled);
            if (component instanceof Container nested) {
                setComponentsEnabled(nested, enabled);
            }
        }
    }

    public void modifyTestElement(TestElement te) {
        CollectionProperty filter = new CollectionProperty(PROPERTY_FILTER + getName(), new ArrayList<>());
        filter.addItem(filterPanel.getIpProtocol());
        filter.addItem(filterPanel.getSrcAddress());
        filter.addItem(filterPanel.getSrcSubnetMask());
        filter.addItem(filterPanel.getDstAddress());
        filter.addItem(filterPanel.getDstSubnetMask());
        filter.addItem(filterPanel.getSrcPort());
        filter.addItem(filterPanel.getDstPort());
        filter.addItem(filterPanel.getIcmpType());
        filter.addItem(filterPanel.getIcmpCode());
        te.setProperty(filter);

        CollectionProperty parameters = new CollectionProperty(PROPERTY_PARAMETERS + getName(), new ArrayList<>());
        parameters.addItem(delayPanel.getValue());
        parameters.addItem(delayPanel.getJitter());
        parameters.addItem(delayPanel.getCorrelation());
        parameters.addItem(delayPanel.getDistribution());
        parameters.addItem(reorderingPanel.getValue());
        parameters.addItem(reorderingPanel.getCorrelation());
        parameters.addItem(lossPanel.getValue());
        parameters.addItem(lossPanel.getCorrelation());
        parameters.addItem(ratePanel.getValue());
        parameters.addItem(ratePanel.getOverhead());
        parameters.addItem(duplicationPanel.getValue());
        parameters.addItem(duplicationPanel.getCorrelation());
        parameters.addItem(corruptionPanel.getValue());
        te.setProperty(parameters);
    }

    public void configure(TestElement te) {
        JMeterProperty filterPropery = (CollectionProperty) te.getProperty(PROPERTY_FILTER + getName());
        if (filterPropery instanceof CollectionProperty filter) {
            filterPanel.setIpProtocol(filter.get(0).getStringValue());
            filterPanel.setSrcAddress(filter.get(1).getStringValue());
            filterPanel.setSrcSubnetMask(filter.get(2).getStringValue());
            filterPanel.setDstAddress(filter.get(3).getStringValue());
            filterPanel.setDstSubnetMask(filter.get(4).getStringValue());
            filterPanel.setSrcPort(filter.get(5).getStringValue());
            filterPanel.setDstPort(filter.get(6).getStringValue());
            filterPanel.setIcmpType(filter.get(7).getStringValue());
            filterPanel.setIcmpCode(filter.get(8).getStringValue());
        }

        JMeterProperty parametersProperty = te.getPropertyOrNull(PROPERTY_PARAMETERS + getName());
        if (parametersProperty instanceof CollectionProperty parameters) {
            delayPanel.setValue(parameters.get(0).getStringValue());
            delayPanel.setJitter(parameters.get(1).getStringValue());
            delayPanel.setCorrelation(parameters.get(2).getStringValue());
            delayPanel.setDistribution(parameters.get(3).getStringValue());
            reorderingPanel.setValue(parameters.get(4).getStringValue());
            reorderingPanel.setCorrelation(parameters.get(5).getStringValue());
            lossPanel.setValue(parameters.get(6).getStringValue());
            lossPanel.setCorrelation(parameters.get(7).getStringValue());
            ratePanel.setValue(parameters.get(8).getStringValue());
            ratePanel.setOverhead(parameters.get(9).getStringValue());
            duplicationPanel.setValue(parameters.get(10).getStringValue());
            duplicationPanel.setCorrelation(parameters.get(11).getStringValue());
            corruptionPanel.setValue(parameters.get(12).getStringValue());
        }
    }
}

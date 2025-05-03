package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.gui.filter.FilterPanel;
import cz.vutbr.networkemulator.gui.parameters.CorruptionPanel;
import cz.vutbr.networkemulator.gui.parameters.DelayPanel;
import cz.vutbr.networkemulator.gui.parameters.DuplicationPanel;
import cz.vutbr.networkemulator.gui.parameters.LimitPanel;
import cz.vutbr.networkemulator.gui.parameters.LossPanel;
import cz.vutbr.networkemulator.gui.parameters.RatePanel;
import cz.vutbr.networkemulator.gui.parameters.ReorderingPanel;
import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Corruption;
import cz.vutbr.networkemulator.model.parameters.Delay;
import cz.vutbr.networkemulator.model.parameters.Duplication;
import cz.vutbr.networkemulator.model.parameters.Limit;
import cz.vutbr.networkemulator.model.parameters.Loss;
import cz.vutbr.networkemulator.model.parameters.Parameter;
import cz.vutbr.networkemulator.model.parameters.Rate;
import cz.vutbr.networkemulator.model.parameters.Reordering;
import net.miginfocom.swing.MigLayout;

public class EmulationRulePanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(EmulationRulePanel.class);

    private final String PROPERTY_FILTER = "filter_";
    private final String PROPERTY_PARAMETERS = "parameters_";

    private FilterPanel filterPanel;
    private DelayPanel delayPanel;
    private ReorderingPanel reorderingPanel;
    private LossPanel lossPanel;
    private RatePanel ratePanel;
    private DuplicationPanel duplicationPanel;
    private CorruptionPanel corruptionPanel;
    private LimitPanel limitPanel;

    public EmulationRulePanel(String niName, String classId) {
        setName(String.format("%s_%s", niName, classId));
        init();
    }

    public Filter getFilter() {
        Filter filter = new Filter();
        filter.setIpVersion(filterPanel.getIpVersion());
        filter.setProtocol(filterPanel.getProtocol());
        filter.setIpv4SrcAddress(filterPanel.getIpv4SrcAddress());
        filter.setIpv4SrcSubnetPrefix(filterPanel.getIpv4SrcSubnetPrefix());
        filter.setIpv4DstAddress(filterPanel.getIpv4DstAddress());
        filter.setIpv4DstSubnetPrefix(filterPanel.getIpv4DstSubnetPrefix());
        filter.setIpv6SrcAddress(filterPanel.getIpv6SrcAddress());
        filter.setIpv6SrcSubnetPrefix(filterPanel.getIpv6SrcSubnetPrefix());
        filter.setIpv6DstAddress(filterPanel.getIpv6DstAddress());
        filter.setIpv6DstSubnetPrefix(filterPanel.getIpv6DstSubnetPrefix());
        filter.setSrcPort(filterPanel.getSrcPort());
        filter.setDstPort(filterPanel.getDstPort());
        filter.setIcmpType(filterPanel.getIcmpType());
        filter.setIcmpCode(filterPanel.getIcmpCode());
        filter.setDscp(filterPanel.getDscp());
        filter.setEcn(filterPanel.getEcn());
        filter.setFlowLabel(filterPanel.getFlowLabel());

        return filter;
    }

    public List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Delay(
                delayPanel.getValue(),
                delayPanel.getJitter(),
                delayPanel.getCorrelation(),
                delayPanel.getDistribution()));
        parameters.add(new Reordering(
                reorderingPanel.getValue(),
                reorderingPanel.getCorrelation()));
        parameters.add(new Loss(
                lossPanel.getValue(),
                lossPanel.getCorrelation()));
        parameters.add(new Rate(
                ratePanel.getValue(),
                ratePanel.getOverhead()));
        parameters.add(new Duplication(
                duplicationPanel.getValue(),
                duplicationPanel.getCorrelation()));
        parameters.add(new Corruption(
                corruptionPanel.getValue()));
        parameters.add(new Limit(
                limitPanel.getValue()));

        return parameters;
    }

    private void init() {
        setLayout(new BorderLayout());

        filterPanel = new FilterPanel();
        delayPanel = new DelayPanel();
        reorderingPanel = new ReorderingPanel();
        lossPanel = new LossPanel();
        ratePanel = new RatePanel();
        duplicationPanel = new DuplicationPanel();
        corruptionPanel = new CorruptionPanel();
        limitPanel = new LimitPanel();

        JPanel parametersPanel = new JPanel(new MigLayout("", "[grow][grow]", "grow"));
        parametersPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));

        parametersPanel.add(filterPanel, "growx, growy, span");
        parametersPanel.add(delayPanel, "growx, growy, span");
        parametersPanel.add(reorderingPanel, "growx, growy");
        parametersPanel.add(lossPanel, "growx, growy, wrap");
        parametersPanel.add(ratePanel, "growx, growy");
        parametersPanel.add(duplicationPanel, "growx, growy, wrap");
        parametersPanel.add(corruptionPanel, "growx, growy");
        parametersPanel.add(limitPanel, "growx, growy");

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filterPanel, parametersPanel);

        add(splitPane, BorderLayout.CENTER);
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
        filter.addItem(filterPanel.getIpVersion().getName());
        filter.addItem(filterPanel.getProtocol().getName());
        filter.addItem(filterPanel.getIpv4SrcAddress());
        filter.addItem(filterPanel.getIpv4SrcSubnetPrefix());
        filter.addItem(filterPanel.getIpv4DstAddress());
        filter.addItem(filterPanel.getIpv4DstSubnetPrefix());
        filter.addItem(filterPanel.getIpv6SrcAddress());
        filter.addItem(filterPanel.getIpv6SrcSubnetPrefix());
        filter.addItem(filterPanel.getIpv6DstAddress());
        filter.addItem(filterPanel.getIpv6DstSubnetPrefix());
        filter.addItem(filterPanel.getSrcPort());
        filter.addItem(filterPanel.getDstPort());
        filter.addItem(filterPanel.getIcmpType());
        filter.addItem(filterPanel.getIcmpCode());
        filter.addItem(filterPanel.getDscp());
        filter.addItem(filterPanel.getEcn());
        filter.addItem(filterPanel.getFlowLabel());
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
        parameters.addItem(limitPanel.getValue());
        te.setProperty(parameters);
    }

    public void configure(TestElement te) {
        JMeterProperty filterPropery = (CollectionProperty) te.getProperty(PROPERTY_FILTER + getName());
        if (filterPropery instanceof CollectionProperty filter) {
            filterPanel.setIpVersion(filter.get(0).getStringValue());
            filterPanel.setProtocol(filter.get(1).getStringValue());
            filterPanel.setIpv4SrcAddress(filter.get(2).getStringValue());
            filterPanel.setIpv4SrcSubnetPrefix(filter.get(3).getStringValue());
            filterPanel.setIpv4DstAddress(filter.get(4).getStringValue());
            filterPanel.setIpv4DstSubnetPrefix(filter.get(5).getStringValue());
            filterPanel.setIpv6SrcAddress(filter.get(6).getStringValue());
            filterPanel.setIpv6SrcSubnetPrefix(filter.get(7).getStringValue());
            filterPanel.setIpv6DstAddress(filter.get(8).getStringValue());
            filterPanel.setIpv6DstSubnetPrefix(filter.get(9).getStringValue());
            filterPanel.setSrcPort(filter.get(10).getStringValue());
            filterPanel.setDstPort(filter.get(11).getStringValue());
            filterPanel.setIcmpType(filter.get(12).getStringValue());
            filterPanel.setIcmpCode(filter.get(13).getStringValue());
            filterPanel.setDscp(filter.get(14).getStringValue());
            filterPanel.setEcn(filter.get(15).getStringValue());
            filterPanel.setFlowLabel(filter.get(16).getStringValue());
        }

        JMeterProperty parametersProperty = te.getProperty(PROPERTY_PARAMETERS + getName());
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
            limitPanel.setValue(parameters.get(13).getStringValue());
        }
    }
}

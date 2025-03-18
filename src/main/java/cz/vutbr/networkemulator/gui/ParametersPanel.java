package cz.vutbr.networkemulator.gui;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.verification.IpAddressVerifier;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class ParametersPanel extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(ParametersPanel.class);

    public static final String SRC_ADDRESS = "srcAddress";
    public static final String SRC_PORT = "srcPort";
    public static final String DST_ADDRESS = "dstAddress";
    public static final String DST_PORT = "dstPort";
    public static final String DELAY_VALUE = "delayValue";
    public static final String DROP_VALUE = "dropValue";
    public static final String RATE_VALUE = "rateValue";
    public static final String LOSS_VALUE = "lossValue";
    public static final String REORDERING_VALUE = "reorderingValue";
    public static final String DUPLICATION_VALUE = "duplicationValue";
    public static final String CORRUPTION_VALUE = "corruptionValue";
    public static final String DELAY_CORRELATION = "delayCorrelation";
    public static final String DROP_CORRELATION = "dropCorrelation";
    public static final String DUPLICATION_CORRELATION = "duplicationCorrelation";
    public static final String REORDERING_CORRELATION = "reorderingCorrelation";
    public static final String JITTER_VALUE = "jitterValue";

    private JTextField srcAddressField;
    private JTextField srcPortField;
    private JTextField dstAddressField;
    private JTextField dstPortField;
    private JTextField delayValueField;
    private JTextField dropValueField;
    private JTextField rateValueField;
    private JTextField lossValueField;
    private JTextField reorderingValueField;
    private JTextField duplicationValueField;
    private JTextField corruptionValueField;
    private JTextField delayCorrelationField;
    private JTextField dropCorrelationField;
    private JTextField duplicationCorrelationField;
    private JTextField reorderingCorrelationField;
    private JTextField jitterValueField;

    public ParametersPanel() {
        init();
    }

    public CollectionProperty getParametersFields() {
        CollectionProperty parameters = new CollectionProperty();
        parameters.addItem(new Argument(SRC_ADDRESS, srcAddressField.getText()));
        parameters.addItem(new Argument(SRC_PORT, srcPortField.getText()));
        parameters.addItem(new Argument(DST_ADDRESS, dstAddressField.getText()));
        parameters.addItem(new Argument(DST_PORT, dstPortField.getText()));
        parameters.addItem(new Argument(DELAY_VALUE, delayValueField.getText()));
        parameters.addItem(new Argument(DROP_VALUE, dropValueField.getText()));
        parameters.addItem(new Argument(RATE_VALUE, rateValueField.getText()));
        parameters.addItem(new Argument(LOSS_VALUE, lossValueField.getText()));
        parameters.addItem(new Argument(REORDERING_VALUE, reorderingValueField.getText()));
        parameters.addItem(new Argument(DUPLICATION_VALUE, duplicationValueField.getText()));
        parameters.addItem(new Argument(CORRUPTION_VALUE, corruptionValueField.getText()));
        parameters.addItem(new Argument(DELAY_CORRELATION, delayCorrelationField.getText()));
        parameters.addItem(new Argument(DROP_CORRELATION, dropCorrelationField.getText()));
        parameters.addItem(new Argument(DUPLICATION_CORRELATION, duplicationCorrelationField.getText()));
        parameters.addItem(new Argument(REORDERING_CORRELATION, reorderingCorrelationField.getText()));
        parameters.addItem(new Argument(JITTER_VALUE, jitterValueField.getText()));

        log.info("Parameters: {}", parameters.getStringValue());

        return parameters;
    }

    public void setParameterFields(CollectionProperty parameters) {
        for (int i = 0; i < parameters.size(); i++) {
            Argument arg = (Argument) parameters.get(i).getObjectValue();
            String name = arg.getName();
            String value = arg.getValue();

            switch (name) {
                case SRC_ADDRESS:
                    srcAddressField.setText(value);
                    break;
                case SRC_PORT:
                    srcPortField.setText(value);
                    break;
                case DST_ADDRESS:
                    dstAddressField.setText(value);
                    break;
                case DST_PORT:
                    dstPortField.setText(value);
                    break;
                case DELAY_VALUE:
                    delayValueField.setText(value);
                    break;
                case DROP_VALUE:
                    dropValueField.setText(value);
                    break;
                case RATE_VALUE:
                    rateValueField.setText(value);
                    break;
                case LOSS_VALUE:
                    lossValueField.setText(value);
                    break;
                case REORDERING_VALUE:
                    reorderingValueField.setText(value);
                    break;
                case DUPLICATION_VALUE:
                    duplicationValueField.setText(value);
                    break;
                case CORRUPTION_VALUE:
                    corruptionValueField.setText(value);
                    break;
                case DELAY_CORRELATION:
                    delayCorrelationField.setText(value);
                    break;
                case DROP_CORRELATION:
                    dropCorrelationField.setText(value);
                    break;
                case DUPLICATION_CORRELATION:
                    duplicationCorrelationField.setText(value);
                    break;
                case REORDERING_CORRELATION:
                    reorderingCorrelationField.setText(value);
                    break;
                case JITTER_VALUE:
                    jitterValueField.setText(value);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown parameter: " + name);
            }
        }
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(createFilterPanel());
        add(createDelayPanel());
        add(createDropPanel());
        add(createRatePanel());
        add(createLossPanel());
        add(createReorderingPanel());
        add(createDuplicationPanel());
        add(createCorruptionPanel());
    }

    private JPanel createFilterPanel() {
        dstAddressField = new JTextField(20);
        dstPortField = new JTextField(7);
        srcAddressField = new JTextField(20);
        srcPortField = new JTextField(7);

        JLabel srcAddressLabel = new JLabel("Src Address:");
        srcAddressLabel.setLabelFor(srcAddressField);
        JLabel srcPortLabel = new JLabel("Src Port:");
        srcPortLabel.setLabelFor(srcPortField);
        JLabel dstAddressLabel = new JLabel("Dst Address:");
        dstAddressLabel.setLabelFor(dstAddressField);
        JLabel dstPortLabel = new JLabel("Dst Port:");
        dstPortLabel.setLabelFor(dstPortField);

        dstAddressField.setInputVerifier(new IpAddressVerifier());
        dstPortField.setInputVerifier(new RangeVerifier(0, 1024));
        srcAddressField.setInputVerifier(new IpAddressVerifier());
        srcPortField.setInputVerifier(new RangeVerifier(0, 1024));

        JPanel srcPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        srcPanel.add(srcAddressLabel);
        srcPanel.add(srcAddressField);
        srcPanel.add(srcPortLabel);
        srcPanel.add(srcPortField);

        JPanel dstPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dstPanel.add(dstAddressLabel);
        dstPanel.add(dstAddressField);
        dstPanel.add(dstPortLabel);
        dstPanel.add(dstPortField);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.PAGE_AXIS));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
        filterPanel.add(srcPanel);
        filterPanel.add(dstPanel);

        return filterPanel;
    }

    private JPanel createDelayPanel() {
        delayValueField = new JTextField(10);
        jitterValueField = new JTextField(10);
        delayCorrelationField = new JTextField(10);

        String[] distributions = { "Uniform", "Normal", "Pareto Normal" };
        JComboBox<String> distributionsBox = new JComboBox<>(distributions);

        JLabel delayValueLabel = new JLabel("Startup Delay (ms):");
        delayValueLabel.setLabelFor(delayValueField);
        JLabel jitterValueLabel = new JLabel("Jitter (ms):");
        jitterValueLabel.setLabelFor(jitterValueField);
        JLabel delayCorrelationLabel = new JLabel("Correlation (%):");
        delayCorrelationLabel.setLabelFor(delayCorrelationField);
        JLabel distributionsLabel = new JLabel("Distribution:");
        distributionsLabel.setLabelFor(distributionsBox);

        delayValueField.setInputVerifier(new RangeVerifier(0, 10000));
        jitterValueField.setInputVerifier(new RangeVerifier(0, 10000));
        delayCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        delayPanel.setBorder(BorderFactory.createTitledBorder("Delay"));
        delayPanel.add(delayValueLabel);
        delayPanel.add(delayValueField);
        delayPanel.add(jitterValueLabel);
        delayPanel.add(jitterValueField);
        delayPanel.add(delayCorrelationLabel);
        delayPanel.add(delayCorrelationField);
        delayPanel.add(distributionsLabel);
        delayPanel.add(distributionsBox);

        return delayPanel;
    }

    private JPanel createDropPanel() {
        dropValueField = new JTextField(10);
        dropCorrelationField = new JTextField(10);

        JLabel dropValueLabel = new JLabel("Packet Drop (%):");
        dropValueLabel.setLabelFor(dropValueField);
        JLabel dropCorrelationLabel = new JLabel("Correlation (%):");
        dropCorrelationLabel.setLabelFor(dropCorrelationField);

        dropValueField.setInputVerifier(new RangeVerifier(0, 100));
        dropCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel dropPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dropPanel.setBorder(BorderFactory.createTitledBorder("Packet Drop"));
        dropPanel.add(dropValueLabel);
        dropPanel.add(dropValueField);
        dropPanel.add(dropCorrelationLabel);
        dropPanel.add(dropCorrelationField);

        return dropPanel;
    }

    private JPanel createRatePanel() {
        rateValueField = new JTextField(10);

        JLabel rateLabel = new JLabel("Rate (bps):");
        rateLabel.setLabelFor(rateLabel);

        rateValueField.setInputVerifier(new RangeVerifier(0, 100000));

        JPanel ratePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratePanel.setBorder(BorderFactory.createTitledBorder("Rate"));
        ratePanel.add(rateLabel);
        ratePanel.add(rateValueField);

        return ratePanel;
    }

    private JPanel createLossPanel() {
        lossValueField = new JTextField(10);

        JLabel lossLabel = new JLabel("Loss (%):");
        lossLabel.setLabelFor(lossValueField);

        lossValueField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel lossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lossPanel.setBorder(BorderFactory.createTitledBorder("Loss"));
        lossPanel.add(lossLabel);
        lossPanel.add(lossValueField);

        return lossPanel;
    }

    private JPanel createReorderingPanel() {
        reorderingValueField = new JTextField(10);
        reorderingCorrelationField = new JTextField(10);

        JLabel reorderingValueLabel = new JLabel("Reordering (%):");
        reorderingValueLabel.setLabelFor(reorderingValueField);
        JLabel reorderingCorrelationLabel = new JLabel("Correlation (%):");
        reorderingCorrelationLabel.setLabelFor(reorderingCorrelationField);

        reorderingValueField.setInputVerifier(new RangeVerifier(0, 100));
        reorderingCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel reorderingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reorderingPanel.setBorder(BorderFactory.createTitledBorder("Reordering"));
        reorderingPanel.add(reorderingValueLabel);
        reorderingPanel.add(reorderingValueField);
        reorderingPanel.add(reorderingCorrelationLabel);
        reorderingPanel.add(reorderingCorrelationField);

        return reorderingPanel;
    }

    private JPanel createDuplicationPanel() {
        duplicationValueField = new JTextField(10);
        duplicationCorrelationField = new JTextField(10);

        JLabel duplicationValueLabel = new JLabel("Duplication (%):");
        duplicationValueLabel.setLabelFor(duplicationValueField);
        JLabel duplicationCorrelationLabel = new JLabel("Correlation (%):");
        duplicationCorrelationLabel.setLabelFor(duplicationCorrelationField);

        duplicationValueField.setInputVerifier(new RangeVerifier(0, 100));
        duplicationCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel duplicationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        duplicationPanel.setBorder(BorderFactory.createTitledBorder("Duplication"));
        duplicationPanel.add(duplicationValueLabel);
        duplicationPanel.add(duplicationValueField);
        duplicationPanel.add(duplicationCorrelationLabel);
        duplicationPanel.add(duplicationCorrelationField);

        return duplicationPanel;
    }

    private JPanel createCorruptionPanel() {
        corruptionValueField = new JTextField(10);

        JLabel corruptionValueLabel = new JLabel("Corruption (%):");
        corruptionValueLabel.setLabelFor(corruptionValueField);

        corruptionValueField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel corruptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        corruptionPanel.setBorder(BorderFactory.createTitledBorder("Corruption:"));
        corruptionPanel.add(corruptionValueLabel);
        corruptionPanel.add(corruptionValueField);

        return corruptionPanel;
    }
}
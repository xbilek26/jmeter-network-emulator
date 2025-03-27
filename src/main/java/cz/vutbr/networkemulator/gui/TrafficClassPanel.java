package cz.vutbr.networkemulator.gui;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.testelement.property.CollectionProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;
import cz.vutbr.networkemulator.verification.IpAddressVerifier;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class TrafficClassPanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(TrafficClassPanel.class);

    private final String[] distributions = {"Uniform", "Normal", "Pareto Normal"};

    private JTextField srcAddressField;
    private JTextField srcPortField;
    private JTextField dstAddressField;
    private JTextField dstPortField;
    private JTextField delayValueField;
    private JTextField jitterField;
    private JTextField delayCorrelationField;
    private JComboBox<String> distributionsBox;
    private JTextField dropValueField;
    private JTextField dropCorrelationField;
    private JTextField rateField;
    private JTextField lossValueField;
    private JTextField reorderingValueField;
    private JTextField reorderingCorrelationField;
    private JTextField duplicationValueField;
    private JTextField duplicationCorrelationField;
    private JTextField corruptionField;

    public TrafficClassPanel(String niName, String tcName) {
        setName(niName + "_" + tcName);
        init();
    }

    public CollectionProperty getNetworkParameters() {
        CollectionProperty parameters = new CollectionProperty();
        parameters.addItem(srcAddressField.getText());
        parameters.addItem(srcPortField.getText());
        parameters.addItem(dstAddressField.getText());
        parameters.addItem(dstPortField.getText());
        parameters.addItem(delayValueField.getText());
        parameters.addItem(jitterField.getText());
        parameters.addItem(delayCorrelationField.getText());
        // TODO: Distribution
        parameters.addItem(dropValueField.getText());
        parameters.addItem(dropCorrelationField.getText());
        parameters.addItem(rateField.getText());
        parameters.addItem(lossValueField.getText());
        parameters.addItem(reorderingValueField.getText());
        parameters.addItem(reorderingCorrelationField.getText());
        parameters.addItem(duplicationValueField.getText());
        parameters.addItem(duplicationCorrelationField.getText());
        parameters.addItem(corruptionField.getText());

        return parameters;
    }

    public void setNetworkParameters(CollectionProperty parameters) {
        srcAddressField.setText(parameters.get(0).getStringValue());
        srcPortField.setText(parameters.get(1).getStringValue());
        dstAddressField.setText(parameters.get(2).getStringValue());
        dstPortField.setText(parameters.get(3).getStringValue());
        delayValueField.setText(parameters.get(4).getStringValue());
        jitterField.setText(parameters.get(5).getStringValue());
        delayCorrelationField.setText(parameters.get(6).getStringValue());
        // TODO: Distribution
        dropValueField.setText(parameters.get(7).getStringValue());
        dropCorrelationField.setText(parameters.get(8).getStringValue());
        rateField.setText(parameters.get(9).getStringValue());
        lossValueField.setText(parameters.get(10).getStringValue());
        reorderingValueField.setText(parameters.get(11).getStringValue());
        reorderingCorrelationField.setText(parameters.get(12).getStringValue());
        duplicationValueField.setText(parameters.get(13).getStringValue());
        duplicationCorrelationField.setText(parameters.get(14).getStringValue());
        corruptionField.setText(parameters.get(15).getStringValue());
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

        JLabel srcAddressLabel = new JLabel(NetworkEmulatorConstants.LABEL_SRC_ADDRESS);
        srcAddressLabel.setLabelFor(srcAddressField);
        JLabel srcPortLabel = new JLabel(NetworkEmulatorConstants.LABEL_SRC_PORT);
        srcPortLabel.setLabelFor(srcPortField);
        JLabel dstAddressLabel = new JLabel(NetworkEmulatorConstants.LABEL_DST_ADDRESS);
        dstAddressLabel.setLabelFor(dstAddressField);
        JLabel dstPortLabel = new JLabel(NetworkEmulatorConstants.LABEL_DST_PORT);
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
        filterPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_FILTER_PANEL));
        filterPanel.add(srcPanel);
        filterPanel.add(dstPanel);

        return filterPanel;
    }

    private JPanel createDelayPanel() {
        delayValueField = new JTextField(10);
        jitterField = new JTextField(10);
        delayCorrelationField = new JTextField(10);
        distributionsBox = new JComboBox<>(distributions);

        JLabel delayValueLabel = new JLabel(NetworkEmulatorConstants.LABEL_DELAY_VALUE);
        delayValueLabel.setLabelFor(delayValueField);
        JLabel jitterLabel = new JLabel(NetworkEmulatorConstants.LABEL_JITTER);
        jitterLabel.setLabelFor(jitterField);
        JLabel delayCorrelationLabel = new JLabel(NetworkEmulatorConstants.LABEL_DELAY_CORRELATION);
        delayCorrelationLabel.setLabelFor(delayCorrelationField);
        JLabel distributionsLabel = new JLabel(NetworkEmulatorConstants.LABEL_DISTRIBUTION);
        distributionsLabel.setLabelFor(distributionsBox);

        delayValueField.setInputVerifier(new RangeVerifier(0, 10000));
        jitterField.setInputVerifier(new RangeVerifier(0, 10000));
        delayCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        delayPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_DELAY_PANEL));
        delayPanel.add(delayValueLabel);
        delayPanel.add(delayValueField);
        delayPanel.add(jitterLabel);
        delayPanel.add(jitterField);
        delayPanel.add(delayCorrelationLabel);
        delayPanel.add(delayCorrelationField);
        delayPanel.add(distributionsLabel);
        delayPanel.add(distributionsBox);

        return delayPanel;
    }

    private JPanel createDropPanel() {
        dropValueField = new JTextField(10);
        dropCorrelationField = new JTextField(10);

        JLabel dropValueLabel = new JLabel(NetworkEmulatorConstants.LABEL_DROP_VALUE);
        dropValueLabel.setLabelFor(dropValueField);
        JLabel dropCorrelationLabel = new JLabel(NetworkEmulatorConstants.LABEL_DROP_CORRELATION);
        dropCorrelationLabel.setLabelFor(dropCorrelationField);

        dropValueField.setInputVerifier(new RangeVerifier(0, 100));
        dropCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel dropPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dropPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_DROP_PANEL));
        dropPanel.add(dropValueLabel);
        dropPanel.add(dropValueField);
        dropPanel.add(dropCorrelationLabel);
        dropPanel.add(dropCorrelationField);

        return dropPanel;
    }

    private JPanel createRatePanel() {
        rateField = new JTextField(10);

        JLabel rateLabel = new JLabel(NetworkEmulatorConstants.LABEL_RATE);
        rateLabel.setLabelFor(rateLabel);

        rateField.setInputVerifier(new RangeVerifier(0, 100000));

        JPanel ratePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratePanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_RATE_PANEL));
        ratePanel.add(rateLabel);
        ratePanel.add(rateField);

        return ratePanel;
    }

    private JPanel createLossPanel() {
        lossValueField = new JTextField(10);

        JLabel lossLabel = new JLabel(NetworkEmulatorConstants.LABEL_LOSS);
        lossLabel.setLabelFor(lossValueField);

        lossValueField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel lossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lossPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_LOSS_PANEL));
        lossPanel.add(lossLabel);
        lossPanel.add(lossValueField);

        return lossPanel;
    }

    private JPanel createReorderingPanel() {
        reorderingValueField = new JTextField(10);
        reorderingCorrelationField = new JTextField(10);

        JLabel reorderingValueLabel = new JLabel(NetworkEmulatorConstants.LABEL_REORDERING_VALUE);
        reorderingValueLabel.setLabelFor(reorderingValueField);
        JLabel reorderingCorrelationLabel = new JLabel(NetworkEmulatorConstants.LABEL_REORDERING_CORRELATION);
        reorderingCorrelationLabel.setLabelFor(reorderingCorrelationField);

        reorderingValueField.setInputVerifier(new RangeVerifier(0, 100));
        reorderingCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel reorderingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reorderingPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_REORDERING_PANEL));
        reorderingPanel.add(reorderingValueLabel);
        reorderingPanel.add(reorderingValueField);
        reorderingPanel.add(reorderingCorrelationLabel);
        reorderingPanel.add(reorderingCorrelationField);

        return reorderingPanel;
    }

    private JPanel createDuplicationPanel() {
        duplicationValueField = new JTextField(10);
        duplicationCorrelationField = new JTextField(10);

        JLabel duplicationValueLabel = new JLabel(NetworkEmulatorConstants.LABEL_DUPLICATION_VALUE);
        duplicationValueLabel.setLabelFor(duplicationValueField);
        JLabel duplicationCorrelationLabel = new JLabel(NetworkEmulatorConstants.LABEL_DUPLICATION_CORRELATION);
        duplicationCorrelationLabel.setLabelFor(duplicationCorrelationField);

        duplicationValueField.setInputVerifier(new RangeVerifier(0, 100));
        duplicationCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel duplicationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        duplicationPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_DUPLICATION_PANEL));
        duplicationPanel.add(duplicationValueLabel);
        duplicationPanel.add(duplicationValueField);
        duplicationPanel.add(duplicationCorrelationLabel);
        duplicationPanel.add(duplicationCorrelationField);

        return duplicationPanel;
    }

    private JPanel createCorruptionPanel() {
        corruptionField = new JTextField(10);

        JLabel corruptionLabel = new JLabel(NetworkEmulatorConstants.LABEL_CORRUPTION);
        corruptionLabel.setLabelFor(corruptionField);

        corruptionField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel corruptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        corruptionPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_CORRUPTION_PANEL));
        corruptionPanel.add(corruptionLabel);
        corruptionPanel.add(corruptionField);

        return corruptionPanel;
    }
}

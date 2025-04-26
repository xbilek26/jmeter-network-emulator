package cz.vutbr.networkemulator.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Converter {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(Converter.class);

    public static List<String> convertToList(CollectionProperty property) {
        List<String> list = new ArrayList<>();
        if (property == null) {
            return list;
        }
        for (int i = 0; i < property.size(); i++) {
            list.add(property.get(i).getStringValue());
        }
        return list;
    }

    public static String convertToString(StringProperty property) {
        if (property == null) {
            return "";
        }
        return property.getStringValue();
    }

}
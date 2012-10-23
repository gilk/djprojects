/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.dj.filter.api.Filter;
import org.dj.property.api.DJProperty;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.lblrtm.lblrtmfilewriter.api.Field;
import org.lblrtm.lblrtmfilewriter.api.Format;
import org.lblrtm.lblrtmfilewriter.api.Record;

/**
 *
 * @author djabry
 */
public class FieldImpl extends DJNodeObjectAbstr implements Field {

    public FieldImpl(String name, Format format, int spaces, int width, Object value, String text, boolean def, Record parentRecord) {

        super();

        setName(name);

        DJProperty fieldOrderProp = getPropertyFactory().createProperty(PROP_FIELD_ORDER, 0);
        fieldOrderProp.setCanRead(false);
        putProperty(fieldOrderProp);

        DJProperty parentProp = getPropertyFactory().createProperty(PROP_PARENT_RECORD, parentRecord);
        parentProp.setCanRead(false);
        putProperty(parentProp);

        DJProperty defaultProp = getPropertyFactory().createProperty(PROP_DEFAULT, def);
        defaultProp.setCanWrite(false);
        defaultProp.setDisplayName("Use default value");
        defaultProp.setFilter(Filter.BOOLEAN);
        this.putProperty(defaultProp);

        DJProperty widthProp = getPropertyFactory().createProperty(PROP_WIDTH, width);
        widthProp.setCanWrite(false);
        widthProp.setDisplayName("Field width");
        widthProp.setFilter(Filter.INTEGER);
        putProperty(widthProp);

        DJProperty spacesProp = getPropertyFactory().createProperty(PROP_SPACES, spaces);
        spacesProp.setCanWrite(false);
        spacesProp.setFilter(Filter.INTEGER);
        spacesProp.setDisplayName("Number of preceeding spaces");
        putProperty(spacesProp);

        DJProperty formatProp = getPropertyFactory().createProperty(PROP_FORMAT, format);
        formatProp.setCanWrite(false);
        formatProp.setDisplayName("Field format");
        this.putProperty(formatProp);

        DJProperty valueProp = getPropertyFactory().createProperty(PROP_VALUE, value);
        valueProp.setCanWrite(false);
        valueProp.setDisplayName("Value");
        valueProp.setDescription("Value assigned to Field");
        this.putProperty(valueProp);

        DJProperty textProp = getPropertyFactory().createProperty(PROP_TEXT, text);
        textProp.setDisplayName("Preceeding text");
        textProp.setCanWrite(false);
        putProperty(textProp);

        parentRecord.addChild(this);

    }

    public FieldImpl(String name, Format format, int width, int spaces, Object value, Record parentRecord) {

        this(name, format, width, spaces, value, "", false, parentRecord);
    }

    public void setFormat(Format format) {

        this.setPropertyValue(PROP_FORMAT, format);
    }

    @Override
    public Format getFormat() {

        return (Format) this.getPropertyValue(PROP_FORMAT);

    }

    @Override
    public void setValue(Object value) {

        this.setPropertyValue(PROP_VALUE, value);
    }

    @Override
    public Object getValue() {

        DJTAPE5 t5 = getParentRecord().getParentTAPE5();
        if (t5 != null) {
            Object val = t5.getRunTemplate().getFieldValueModifier().getValueForField(this);

            if (val != null) {

                return val;
            }

        }



        return this.getPropertyValue(PROP_VALUE);
    }

    @Override
    public Object getValueFor(Object arg) {

        if (arg == null) {

            return getValue();
        }

        return null;
    }

    @Override
    public String getValueString() {

        String formatString = "";
        try {

            Object value = getValue();
            if (isDefault()) {

                int totalWidth = getWidth() + getSpaces();
                formatString = "%" + totalWidth + "s";
                value = " ";

            } else {

                int lengthPrecString = getText().length();

                int diff = getSpaces() - lengthPrecString;

                if (getSpaces() == 0) {

                    setText("");

                } else if (diff >= 0) {

                    String in = "%" + getSpaces() + "s";

                    setText(String.format(in, getText()));

                } else {

                    setText(getText().substring(0, getSpaces() - 1));
                }
                //System.out.println("Field = "+getName());
                //System.out.println("Value = "+value);
                formatString = getText() + "%" + getWidth() + getFormat().getFormatString();
            }

            String s = String.format(formatString, value);
            return s.substring(0, getSpaces() + getWidth());

        } catch (NullPointerException nPE) {
            System.out.println("Could not print value for " + this.getName());

        }



        return "";

    }

    @Override
    public int getSpaces() {
        return (Integer) this.getPropertyValue(PROP_SPACES);
    }

    public void setSpaces(int spaces) {

        this.setPropertyValue(PROP_SPACES, spaces);
    }

    @Override
    public int getWidth() {
        return (Integer) this.getPropertyValue(PROP_WIDTH);
    }

    public void setWidth(int width) {

        this.setPropertyValue(PROP_WIDTH, width);
    }

    @Override
    public String getText() {
        return (String) this.getPropertyValue(PROP_TEXT);
    }

    public void setText(String preceedingText) {
        this.setPropertyValue(PROP_TEXT, preceedingText);
    }

    @Override
    public boolean isDefault() {
        return (Boolean) getPropertyValue(PROP_DEFAULT);
    }

    public void setDefault(boolean def) {

        this.setPropertyValue(PROP_DEFAULT, def);
    }

    @Override
    public Record getParentRecord() {
        DJNodeObject parent = getParent();

        if (parent instanceof Record) {
            Record record = (Record) parent;
            return record;
        }

        return null;
    }

    @Override
    public int getFieldOrder() {
        return (Integer) getPropertyValue(PROP_FIELD_ORDER);
    }

    public void setFieldOrder(int fOrder) {
        this.setPropertyValue(PROP_FIELD_ORDER, fOrder);
    }

    @Override
    public Field duplicate(Record parentRecord) {
        Field f = new FieldImpl(getName(), getFormat(), getSpaces(), getWidth(), getValue(), getText(), isDefault(), parentRecord);
        return f;
    }

    @Override
    public int getLevel() {
        return (Integer) getParentRecord().getPropertyValue(RecordAbstr.PROP_LEVEL);
    }
}

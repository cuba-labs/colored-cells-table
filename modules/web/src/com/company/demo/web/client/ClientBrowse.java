package com.company.demo.web.client;

import com.company.demo.entity.Client;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.ColorPicker;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.vaadin.server.Page;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class ClientBrowse extends AbstractLookup {
    @Inject
    private ColorPicker colorPicker;

    @Inject
    private GroupTable<Client> clientsTable;

    @Inject
    private GroupDatasource<Client, UUID> clientsDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        // import initial CSS when we load data
        clientsDs.addCollectionChangeListener(e -> {
            if (e.getOperation() == CollectionDatasource.Operation.REFRESH) {
                for (Client client : clientsDs.getItems()) {
                    injectColorCss(client.getColor());
                }
            }
        });

        colorPicker.addValueChangeListener(e -> {
            Client client = clientsTable.getSingleSelected();
            if (client != null) {
                client.setColor((String) e.getValue());
                injectColorCss((String) e.getValue());
                clientsTable.repaint();
            }
        });

        clientsTable.addStyleProvider((entity, property) -> {
            if ("color".equals(property) && entity.getColor() != null) {
                return "colored-cell-" + entity.getColor();
            }
            return null;
        });
    }

    protected void injectColorCss(String color) {
        Page.Styles styles = Page.getCurrent().getStyles();
        styles.add(String.format(
                ".colored-cell-%s { background-color: #%s; }",
                color, color));
    }
}
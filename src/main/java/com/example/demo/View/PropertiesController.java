package com.example.demo.View;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Map;

public class PropertiesController {

    @FXML private TableView<Map.Entry<String,String>> table;
    @FXML private TableColumn<Map.Entry<String,String>,String> keyCol;
    @FXML private TableColumn<Map.Entry<String,String>,String> valueCol;

    // מקבלים Map עם ההגדרות – אפשר לשלוח מה-MenuController
    public void init(Map<String,String> props) {
        keyCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getKey()));
        valueCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getValue()));
        table.setItems(FXCollections.observableArrayList(props.entrySet()));
    }

    @FXML private void onClose() {
        ((Stage) table.getScene().getWindow()).close();
    }
}

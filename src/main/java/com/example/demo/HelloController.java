package com.example.demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.EventListener;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import org.json.*;
public class HelloController {
    private String selectedItemOfCategory=null;
    @FXML
    private VBox categoryVbox;
    @FXML
    private ComboBox Kategori;
    @FXML
    private Label resultLabel;
    @FXML
    private TableView<bilgisayarTablo> dataTablo;
    @FXML
    public TableColumn<bilgisayarTablo, Integer> colStok;
    @FXML
    public TableColumn<bilgisayarTablo, Integer> colParcaId;
    @FXML
    public TableColumn<bilgisayarTablo, String> colName;
    @FXML
    public TableColumn<bilgisayarTablo, Float> colUnitPrice;

    private TextField nameTextField;
    private TextField UnitPriceTextField;
    private TextField StokTextFeild;
    private TextField ParcaIdField;
    private String _nameField;
    private float _unitPriceField;
    private int   _stokField;
    private int   _parcaId;
    @FXML
    public void initialize()  {
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("Stok"));
        colParcaId.setCellValueFactory(new PropertyValueFactory<>("ParcaId"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        Kategori.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                KategoriChangeControl(t1.toString());

            }
        });
        try{
            JSONObject response=ChooseController.getAll();
            refreshToTable(response);
        }catch (Exception e){
            resultLabel.setText(e.getMessage());
        }
    }

    @FXML
    protected  void onSaveClick() throws JSONException {

        setFeild();
        if(newFieldCheck()){
            switch (selectedItemOfCategory){
                case "Yeni":
                    JSONObject response=ChooseController.addNewItem(_nameField,_unitPriceField,_stokField);
                    if(Boolean.parseBoolean(response.get("success").toString())==true){
                        refreshToTable(response);
                        resultLabel.setText(response.get("message").toString());
                    }else{
                        resultLabel.setText(response.get("message").toString());
                    }
                    break;
                case "Düzenle":
                    JSONObject res=ChooseController.editItem(_nameField,_unitPriceField,_stokField,_parcaId);
                    if(Boolean.parseBoolean(res.get("success").toString())==true){
                        refreshToTable(res);
                        resultLabel.setText(res.get("message").toString());
                    }else{
                        resultLabel.setText(res.get("message").toString());
                    }

                    break;
                case "Çıkarma":
                    JSONObject res2=ChooseController.decreaseStock(_parcaId,_stokField);
                    if(Boolean.parseBoolean(res2.get("success").toString())==true){
                        refreshToTable(res2);
                        resultLabel.setText(res2.get("message").toString());
                    }else{
                        resultLabel.setText(res2.get("message").toString());
                    }
                    break;
                default:
                    break;
            }
        }


    }
    protected void setFeild(){
        switch (selectedItemOfCategory){
            case "Yeni":
                this.nameTextField= (TextField) categoryVbox.lookup("#nameTextField");
                this.UnitPriceTextField=(TextField) categoryVbox.lookup("#UnitPriceTextField");
                this.StokTextFeild=(TextField) categoryVbox.lookup("#StokTextFeild");
                break;
            case "Düzenle":
                this.nameTextField= (TextField) categoryVbox.lookup("#nameTextField");
                this.UnitPriceTextField=(TextField) categoryVbox.lookup("#UnitPriceTextField");
                this.StokTextFeild=(TextField) categoryVbox.lookup("#StokTextFeild");
                this.ParcaIdField = (TextField) categoryVbox.lookup("#parcaId");
                break;
            case "Çıkarma":
                this.ParcaIdField=(TextField)  categoryVbox.lookup("#parcaId");
                this.StokTextFeild=(TextField) categoryVbox.lookup("#StokTextFeild");
            default:
                break;
        }

    }
    protected Boolean newFieldCheck(){
        if(!(selectedItemOfCategory.equals("Çıkarma"))){
            if(nameTextField.getText().isEmpty() || nameTextField.getText().isBlank()){
                resultLabel.setText("İsim alanını lütfen doldurunuz");
                return false;
            }
            try{
                this._nameField=nameTextField.getText();
                this._unitPriceField=Float.parseFloat(UnitPriceTextField.getText());
                this._stokField=Integer.parseInt(StokTextFeild.getText());
                if(selectedItemOfCategory.equals("Düzenle")){
                    this._parcaId=Integer.parseInt(ParcaIdField.getText());

                }
                return true;
            }catch (NumberFormatException e){
                resultLabel.setText("Birim fiyatına ve Stok sayısına bir sayı giriniz");
                return false;
            }
        }else if(selectedItemOfCategory.equals("Çıkarma")){
            try{
                this._stokField=Integer.parseInt(StokTextFeild.getText());
                this._parcaId=Integer.parseInt(ParcaIdField.getText());
                return true;
            }catch (NumberFormatException e){
                resultLabel.setText("Urun id ve Stok sayısına bir sayı giriniz");
                return false;
            }
        }
        return  false;
    }
    @FXML
    protected void refreshToTable(JSONObject response) throws JSONException {
        JSONArray resData= (JSONArray) response.get("data");
        ObservableList<bilgisayarTablo> observableList=FXCollections.observableArrayList();
        for (int i=0;i<resData.length();i++){
            JSONObject jsonObject= (JSONObject) resData.get(i);
            int stok=Integer.parseInt(jsonObject.get("stok").toString());
            String name=jsonObject.get("name").toString();
            float unitPrice=Float.parseFloat(jsonObject.get("unitPrice").toString());
            int parcaId=Integer.parseInt(jsonObject.get("parcaId").toString());

            bilgisayarTablo pc=new bilgisayarTablo(name,stok,unitPrice,parcaId);
            observableList.add(pc);
        }

        dataTablo.setItems(observableList);
    }
    protected void KategoriChangeControl(String seleceted){
        categoryVbox.getChildren().clear();
        this.selectedItemOfCategory=seleceted;
        if(seleceted.equals("Yeni")){
            String [] fieldId={"nameTextField","UnitPriceTextField","StokTextFeild"};
            String [] fieldText={"İsim","Birim fiyatı","Stok sayisi"};
            addChilds(fieldId, fieldText);
        }else if(seleceted.equals("Düzenle")){
            String [] fieldId={"parcaId","nameTextField","UnitPriceTextField","StokTextFeild"};
            String [] fieldText={"ParcaId","İsim","Birim fiyatı","Stok sayisi"};
            addChilds(fieldId, fieldText);
        } else if (seleceted.equals("Çıkarma")) {
            String [] fieldId={"parcaId","StokTextFeild"};
            String [] fieldText={"ParcaId","Stok sayisi"};
            addChilds(fieldId, fieldText);
        }
        Button btn=new Button();
        btn.getStyleClass().add("button-56");
        btn.setId("PostButton");
        btn.setLayoutX(112.0);
        btn.setLayoutY(32.0);
        btn.setMnemonicParsing(false);
        btn.prefHeight(38.0);
        btn.setPrefWidth(137.0);
        btn.setText("Kaydet");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    onSaveClick();
                } catch (JSONException e) {
                    resultLabel.setText(e.getMessage());
                }
            }
        });
//        btn.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        Pane pn=new Pane();
        pn.setPrefHeight(102.0);
        pn.setPrefWidth(300.0);
        pn.getChildren().add(btn);
        categoryVbox.getChildren().add(pn);
    }

    private void addChilds(String[] fieldId, String[] fieldText) {
        for (int i=0;i<fieldId.length;i++){
            TextField tx=new TextField();
            tx.setId(fieldId[i]);
            tx.setLayoutX(110.0);
            tx.setLayoutY(15.0);
            tx.prefHeight(26.0);
            tx.prefWidth(168.0);

            Label lb =new Label();
            lb.setLayoutX(60.0);
            lb.setLayoutY(19.0);
            lb.setPrefHeight(17.0);
            lb.prefWidth(60.0);
            lb.setText(fieldText[i]);
            lb.setStyle("-fx-padding: -15px; -fx-border-insets: -15px;-fx-background-insets: -15px;");


            Pane pane=new Pane(tx);
            pane.setPrefHeight(54.0);
            pane.prefWidth(400.0);
            pane.getChildren().add(lb);
            if(this.selectedItemOfCategory.equals("Düzenle") && fieldId[i].equals("parcaId")){
                Button btn=new Button();
                btn.setText("Getir");
                btn.setLayoutX(280.0);
                btn.setLayoutY(15.0);
//                btn.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent actionEvent) {
//
//                    }
//                });
                btn.setOnMouseClicked(mouseEvent -> getInfo());
                btn.prefHeight(26.0);
                btn.prefWidth(100.0);
                pane.getChildren().add(btn);
            }

            categoryVbox.getChildren().add(pane);
        }
    }
    protected void getInfo(){
        bilgisayarTablo bt=dataTablo.getSelectionModel().getSelectedItem();
        if(bt!=null){
            setFeild();
            nameTextField.setText(bt.getName());
            ParcaIdField.setText(String.valueOf(bt.getParcaId()));
            UnitPriceTextField.setText(String.valueOf(bt.getUnitPrice()));
            StokTextFeild.setText(String.valueOf(bt.getStok()));
        }
    }
    @FXML
    protected void refreshButton() {
        try{

            JSONObject response=BilgisayarRequest.sendHttpGETRequest("http://localhost:8083/api/bilgisayar/getall");
            refreshToTable(response);

        }catch (Exception e){
            System.out.println(e);
        }

    }
}

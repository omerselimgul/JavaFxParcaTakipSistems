package com.example.demo;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class bilgisayarTablo {
    private SimpleIntegerProperty stok;
    private SimpleStringProperty name;
    private SimpleFloatProperty unitPrice;

    private SimpleIntegerProperty parcaId;
    public bilgisayarTablo(String name,int stok,float unitPrice,int parcaId){
        this.name = new SimpleStringProperty(name);
        this.stok = new SimpleIntegerProperty(stok);
        this.unitPrice = new SimpleFloatProperty(unitPrice);
        this.parcaId=new SimpleIntegerProperty(parcaId);
    }

    public int getParcaId(){
        return this.parcaId.get();
    }
    public int getStok() {
        return stok.get();
    }

    public SimpleIntegerProperty stokProperty() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok=new SimpleIntegerProperty(stok);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name=new SimpleStringProperty(name);
    }

    public float getUnitPrice() {
        return unitPrice.get();
    }

    public SimpleFloatProperty unitPriceProperty() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice =new SimpleFloatProperty(unitPrice);
    }



}

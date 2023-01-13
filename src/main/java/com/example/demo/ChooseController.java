package com.example.demo;

import org.json.JSONException;
import org.json.JSONObject;

public class ChooseController {
    public static JSONObject getAll() throws JSONException {
        try{


            JSONObject response=BilgisayarRequest.sendHttpGETRequest("http://localhost:8083/api/bilgisayar/getall");
            return response;
        }catch (Exception e){
            JSONObject js=new JSONObject();
            js.put("message","İşlem sırasında bir hata olustu");
            return js;
        }
    }
    public static JSONObject addNewItem(String name,Float UnitPrice,int Stok) throws JSONException {
        try{

            String jsonString=new JSONObject()
                    .put("name", name)
                    .put("unitPrice", UnitPrice)
                    .put("stok", Stok)
                    .toString();

            JSONObject response=BilgisayarRequest.sendHttpPOSTRequest("http://localhost:8083/api/bilgisayar/add",jsonString);
            return response;
        }catch (Exception e){
            JSONObject js=new JSONObject();
            js.put("message","İşlem sırasında bir hata olustu");
            return js;
        }
    }
    public static JSONObject editItem(String name,Float UnitPrice,int Stok,int updateId) throws JSONException {
        try{
            String jsonString=new JSONObject()
                    .put("updateId",updateId)
                    .put("name", name)
                    .put("unitPrice", UnitPrice)
                    .put("stok", Stok)
                    .toString();

            JSONObject response=BilgisayarRequest.sendHttpPUTRequest("http://localhost:8083/api/bilgisayar/edit",jsonString);
            return response;
        }catch (Exception e){
            JSONObject js=new JSONObject();
            js.put("message","İşlem sırasında bir hata olustu");
            return js;
        }
    }
    public static JSONObject decreaseStock(int updateId,int Stok) throws JSONException {
        try{
            String jsonString=new JSONObject()
                    .put("updateId",updateId)
                    .put("stok",Stok )
                    .toString();

            JSONObject response=BilgisayarRequest.sendHttpPUTRequest("http://localhost:8083/api/bilgisayar/azalt",jsonString);
            return response;
        }catch (Exception e){
            JSONObject js=new JSONObject();
            js.put("message","İşlem sırasında bir hata olustu");
            return js;
        }
    }
}

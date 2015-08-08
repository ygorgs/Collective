package com.example.yuricesar.collective.data;

import android.content.Context;
import android.content.ContextWrapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franklin on 26/07/15.
 */
public class CelulaREST {

    //TODO mudar o url
    private static String URI = "http://192.168.0.103:8080/Restful/collective";
    private String result = "";
    private UserInfo usuario = null;
    private String remetente = "";

    /**
     *
     * Método responsável por fazer chamada ao web service e buscar as
     * informações (json) atraves da URI.
     *
     * @return UserInfo
     * @throws Exception
     */
    public List<Object> recomendacao(final UserInfo user, final List<Category> categorias) throws Exception {
        usuario = null;
        final List<Double> intereses = new ArrayList<Double>();
        new Thread(new Runnable()

        {
            public void run() {
                JSONArray jsonA = new JSONArray(categorias);
                JSONObject j = new JSONObject();
                try {
                    j.put("cliente", user.getId());
                    j.put("categorys", jsonA);

                    // Array de String que recebe o JSON do Web Service
                    String[] json = new WebService().post(URI + "/recomendacao", j.toString());
                    //mudar
                    if (json[0].equals("200")) {

                        Gson gson = new Gson();

                        JsonParser parser = new JsonParser();

                        // Fazendo o parse do JSON para um JsonArray
                        JsonObject jsonObject = (JsonObject) parser.parse(json[1]);
                        JsonArray jsonArray = (JsonArray) jsonObject.get("intereses");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            intereses.add(Double.parseDouble(jsonArray.get(i).toString()));
                        }
                        usuario = gson.fromJson(jsonObject.get("user"), UserInfo.class);
                    } else {
                        try {
                            throw new Exception(json[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        List<Object> result = new ArrayList();
        result.add(usuario);
        result.add(intereses);
        return result;
    }

    public void novoUsuario(final UserInfo user) throws Exception {
        new Thread(new Runnable()

        {
            public void run() {

                JSONObject j = new JSONObject();
                try {
                    j.put("id", user.getId());
                    j.put("nome", user.getName());
                    j.put("email", user.getEmail());
                    j.put("picture", user.getURLPicture());

                    // Array de String que recebe o JSON do Web Service
                    new WebService().post(URI + "/newuser", j.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void novaAmizade(final UserInfo user, final UserInfo amigo) throws Exception {
        new Thread(new Runnable()

        {
            public void run() {

                JSONObject j = new JSONObject();
                try {
                    j.put("idCliente", user.getId());
                    j.put("idAmigo", amigo.getId());

                    // Array de String que recebe o JSON do Web Service
                    new WebService().post(URI + "/newfriend", j.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void atualizaLocalizacao(final String id, final double latitude, final double longitude) throws Exception {
        new Thread(new Runnable()

        {
            public void run() {

                JSONObject j = new JSONObject();

                try {
                    j.put("id", id);
                    j.put("latitude", latitude);
                    j.put("longitude", longitude);

                    new WebService().post(URI + "/localizacao", j.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void enviarMsg(final UserInfo origem, final UserInfo destino, final String msg) throws Exception {
        new Thread(new Runnable()

        {
            public void run() {

                JSONObject j = new JSONObject();
                try {
                    j.put("idCliente", origem.getId());
                    j.put("idDestino", destino.getId());
                    j.put("msg", msg);

                    // Array de String que recebe o JSON do Web Service
                    new WebService().post(URI + "/enviar", j.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public List<String> receberMsg(final UserInfo user) throws Exception {
        result = "";
        remetente = "";
        new Thread(new Runnable()

        {
            public void run() {

                JSONObject j = new JSONObject();
                try {
                    j.put("idCliente", user.getId());

                    // Array de String que recebe o JSON do Web Service
                    String[] json = new WebService().post(URI + "/receber", j.toString());

                    if (json[0].equals("200")) {

                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject = (JsonObject) parser.parse(json[1]);
                        result = jsonObject.get("msg").toString();
                        remetente = jsonObject.get("user").toString();

                    } else {
                        try {
                            throw new Exception(json[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        List<String> saida = new ArrayList<String>();
        saida.add(remetente);
        saida.add(result);
        return saida;
    }
}
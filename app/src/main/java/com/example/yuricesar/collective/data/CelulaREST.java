package com.example.yuricesar.collective.data;

import android.location.Location;

import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
    private UserInfo result = null;
    private String r = "";

    /**
     *
     * Método responsável por fazer chamada ao web service e buscar as
     * informações (json) atraves da URI.
     *
     * @return UserInfo
     * @throws Exception
     */
    public UserInfo recomendacao() throws Exception {
        new Thread(new Runnable()

        {
            public void run() {
                //TODO mudar para o id do cliente
                String id = "oi";
                //TODO mudar para a lista de categorias q o usuario quer comparar
                List<String> categorias = new ArrayList<>();

                JSONArray jsonA = new JSONArray(categorias);
                JSONObject j = new JSONObject();
                try {
                    j.put("cliente", id);
                    j.put("categorys", jsonA);

                    // Array de String que recebe o JSON do Web Service
                    String[] json = new WebService().post(URI + "/recomendacao", j.toString());

                    if (json[0].equals("200")) {

                        Gson gson = new Gson();

                        JsonParser parser = new JsonParser();

                        // Fazendo o parse do JSON para um JsonArray
                        JsonArray array = parser.parse(json[1]).getAsJsonArray();

                        for (int i = 0; i < array.size(); i++) {

                            // Criando usuario com os dados do servidor
                            result = gson.fromJson(array.get(i), UserInfo.class);
                        }
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
        return result;
    }

    public void novoUsuario() throws Exception {
        new Thread(new Runnable()

        {
            public void run() {
                //TODO mudar para o id do cliente
                String id = "oi";
                //TODO mudar para o nome do cliente
                String nome = "ei";
                //TODO mudar para o email do cliente
                String email = "ie";
                //TODO mudar para o picture do cliente
                String picture = "io";
                //TODO mudar para o latitude do cliente
                String latitude = "hue";
                //TODO mudar para o longitude do cliente
                String longitude = "br";

                JSONObject j = new JSONObject();
                try {
                    j.put("id", id);
                    j.put("nome", nome);
                    j.put("email", email);
                    j.put("picture", picture);
                    j.put("latitude", picture);
                    j.put("longitude", picture);

                    // Array de String que recebe o JSON do Web Service
                    new WebService().post(URI + "/newuser", j.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void novaAmizade() throws Exception {
        new Thread(new Runnable()

        {
            public void run() {
                //TODO mudar para o id do cliente
                String idCliente = "oi";
                //TODO mudar para o id do amigo
                String idAmigo = "ei";

                JSONObject j = new JSONObject();
                try {
                    j.put("idCliente", idCliente);
                    j.put("idAmigo", idAmigo);

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

    public void userProximos() throws Exception {
        new Thread(new Runnable()

        {
            public void run() {
                //TODO mudar para o id do cliente
                String idCliente = "oi";

                JSONObject j = new JSONObject();
                try {
                    j.put("idCliente", idCliente);

                    // Array de String que recebe o JSON do Web Service
                    String[] json = new WebService().post(URI + "/geo", j.toString());

                    if (json[0].equals("200")) {

                        Gson gson = new Gson();

                        JsonParser parser = new JsonParser();

                        // Fazendo o parse do JSON para um JsonArray
                        JsonArray array = parser.parse(json[1]).getAsJsonArray();

                        for (int i = 0; i < array.size(); i++) {

                            // Criando usuario com os dados do servidor
                            result = gson.fromJson(array.get(i), UserInfo.class);
                        }
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
    }

    public void enviarMsg() throws Exception {
        new Thread(new Runnable()

        {
            public void run() {
                //TODO mudar para o id do cliente
                String idCliente = "oi";
                //TODO mudar para o id do destino
                String idDestino = "ei";
                //TODO mudar para a msg
                String msg = "ie";

                JSONObject j = new JSONObject();
                try {
                    j.put("idCliente", idCliente);
                    j.put("idDestino", idDestino);
                    j.put("msg", msg);

                    // Array de String que recebe o JSON do Web Service
                    new WebService().post(URI + "/enviar", j.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void receberMsg() throws Exception {
        new Thread(new Runnable()

        {
            public void run() {
                //TODO mudar para o id do cliente
                String idCliente = "oi";

                JSONObject j = new JSONObject();
                try {
                    j.put("idCliente", idCliente);

                    // Array de String que recebe o JSON do Web Service
                    String[] json = new WebService().post(URI + "/receber", j.toString());

                    if (json[0].equals("200")) {

                        r = json[1];
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
    }
}
package com.example.yuricesar.collective.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franklin on 10/08/15.
 */
public class PreferenceAuxiliar {
    private List<Category> categorias;
    private String distancia;
    private static PreferenceAuxiliar instace;

    private PreferenceAuxiliar() {
        categorias = new ArrayList<>();
        instace = this;
    }

    public static PreferenceAuxiliar getInstace() {
        if (instace == null) {
            instace = new PreferenceAuxiliar();
        }
        return instace;
    }

    public List<Category> getCategoriasSelecionadas () {
        return categorias;
    }

    public void addCategoria (Category c) {
        if (!categorias.contains(c)) {
            categorias.add(c);
        }
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia (int d) {
        distancia = String.valueOf(d * 1000);
    }
}

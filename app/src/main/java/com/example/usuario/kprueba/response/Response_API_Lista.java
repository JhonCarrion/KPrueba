package com.example.usuario.kprueba.response;

import com.example.usuario.kprueba.response.POJO.QueryC;

import java.util.ArrayList;

public class Response_API_Lista {

    private QueryC query;

    public QueryC getQuery() {
        return query;
    }

    public void setQuery(QueryC query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "Response_API_Lista{" +
                "query=" + query +
                '}';
    }
}

package com.example.usuario.kprueba.API;


import com.example.usuario.kprueba.response.Response_API_Lista;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class API_Consultas {
    public interface consulta{


        @GET("/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid=375740&format=json")
        Call<Response_API_Lista> postLista();
    }
}

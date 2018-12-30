package com.example.usuario.kprueba.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.usuario.kprueba.API.API_Consultas;
import com.example.usuario.kprueba.response.Response_API_Lista;
import com.example.usuario.kprueba.servicios.OnComunicacionLista;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Presenter_Lista extends Presenter {

    private OnComunicacionLista onComunicacionListarUsuario;

    public Presenter_Lista(OnComunicacionLista onComunicacionListarUsuario) {
        this.onComunicacionListarUsuario = onComunicacionListarUsuario;
    }

    public void Listar() {

        API_Consultas.consulta service = retrofit.create(API_Consultas.consulta.class);

        Call<Response_API_Lista> call = service.postLista();

        call.enqueue(new Callback<Response_API_Lista>() {

            @Override
            public void onResponse(@NonNull Call<Response_API_Lista> call, @NonNull Response<Response_API_Lista> response) {
                Response_API_Lista responseApi = response.body();
                if (responseApi != null) {
                    if (response.code() == 200) {
                        Log.e("respuesta200", "200");
                        onComunicacionListarUsuario.respuestaListaUsuario(responseApi);
                    } else {
                        Log.e("respuesta200", "no200");
                        onComunicacionListarUsuario.errorListaUsuario(responseApi);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Response_API_Lista> call, @NonNull Throwable t) {
                Log.e("errorWS", t.getMessage());
                onComunicacionListarUsuario.errorListaUsuario(null);
            }

        });
    }

}

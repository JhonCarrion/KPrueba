package com.example.usuario.kprueba.clases;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.usuario.kprueba.R;
import com.example.usuario.kprueba.presenter.Presenter_Lista;
import com.example.usuario.kprueba.response.Response_API_Lista;
import com.example.usuario.kprueba.servicios.OnComunicacionLista;

public class WebServiceActivity extends AppCompatActivity implements OnComunicacionLista {

    private Presenter_Lista presenter_lista;

    private TextView lbl_temperatura, lbl_velocidad, lbl_humedad, lbl_presion, lbl_fechaHora, lbl_Descripcion, lbl_tem_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter_lista = new Presenter_Lista(this);
        presenter_lista.Listar();

        lbl_Descripcion = findViewById(R.id.lbl_ws_description);
        lbl_temperatura = findViewById(R.id.lbl_ws_temperatura);
        lbl_humedad = findViewById(R.id.lbl_ws_humedad);
        lbl_presion = findViewById(R.id.lbl_ws_presion);
        lbl_velocidad = findViewById(R.id.lbl_ws_velocidad);
        lbl_fechaHora = findViewById(R.id.lbl_ws_fecha_hora);
        lbl_tem_total = findViewById(R.id.lbl_ws_temperatura_total);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void respuestaListaUsuario(Response_API_Lista response) {
        if (response.getQuery() != null) {
            Log.e("lista", response.getQuery().toString());
            lbl_Descripcion.setText(response.getQuery().getResults().getChannel().getDescription());
            lbl_temperatura.setText(response.getQuery().getResults().getChannel().getWind().getChill() + "°F");
            lbl_humedad.setText(response.getQuery().getResults().getChannel().getAtmosphere().getHumidity() + "%");
            lbl_presion.setText(response.getQuery().getResults().getChannel().getAtmosphere().getPressure() + "in");
            lbl_velocidad.setText(response.getQuery().getResults().getChannel().getWind().getSpeed() + "mph");
            lbl_fechaHora.setText(response.getQuery().getResults().getChannel().getItem().getCondition().getDate());
            lbl_tem_total.setText(response.getQuery().getResults().getChannel().getItem().getCondition().getTemp() + "°F" + "\n" + response.getQuery().getResults().getChannel().getItem().getCondition().getText());
        }
    }

    @Override
    public void errorListaUsuario(Response_API_Lista response_api) {

    }
}

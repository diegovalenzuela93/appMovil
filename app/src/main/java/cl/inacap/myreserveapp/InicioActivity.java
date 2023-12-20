package cl.inacap.myreserveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class InicioActivity extends AppCompatActivity {
    Bundle bundle;
    Intent intento;
    TextView txvUser, infoReservas, infoPlan;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        vincularElementos();
        obtenerValores();
        mostrarUsuario();
    }

    private void mostrarUsuario() {
        txvUser.setText(usuario);
    }

    private void obtenerValores() {
        intento = getIntent();
        bundle = intento.getExtras();
        usuario = bundle.getString("usuario");

    }

    private void vincularElementos() {
        txvUser = (TextView) findViewById(R.id.txv_user);
        infoReservas = (TextView) findViewById(R.id.txv_info_reservas);
        infoPlan = (TextView) findViewById(R.id.txv_info_plan);
    }
}
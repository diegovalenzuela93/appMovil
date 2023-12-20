package cl.inacap.myreserveapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtUsuario, edtContrasena;
    TextView txvRegistro;
    Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vincularElementos();
        Listener();
    }

    private void mostrarAdvertencia(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void Listener() {
        txvRegistro.setOnClickListener(this);
        btnIniciar.setOnClickListener(this);
    }

    private void vincularElementos() {
        edtUsuario = (EditText) findViewById(R.id.edt_user);
        edtContrasena = (EditText) findViewById(R.id.edt_pass);
        txvRegistro = (TextView) findViewById(R.id.txv_registro);
        btnIniciar = (Button) findViewById(R.id.btn_iniciar);
    }

    @Override
    public void onClick(View view) {
        String usuario = edtUsuario.getText().toString();
        String contrasena = edtContrasena.getText().toString();

        //Intents para ingresar al registro o a la pantalla principal segun opcion marcada
        if (view == txvRegistro) {
            Intent intento = new Intent(MainActivity.this, registro.class);
            startActivity(intento);
        } else if (view == btnIniciar){
            //validacion campos usuario, contraseña no esten vacios
            if (usuario.isEmpty() || contrasena.isEmpty()){
                mostrarAdvertencia("Por favor, complete los campos");
            } else {
                Intent inicio = new Intent(MainActivity.this, InicioActivity.class);
                inicio.putExtra("usuar io", usuario);
                startActivity(inicio);
            }
        }
    }
}
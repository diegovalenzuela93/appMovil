package cl.inacap.myreserveapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cl.inacap.myreserveapp.model.Persona;

public class registro extends AppCompatActivity {
    //Declaramos variable para trabajar con los elementos del layout
    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;

    EditText edtNombre, edtApellido, edtCorreo, edtTelefono, edtContrasena;
    ListView datosPersonas;
    FirebaseFirestore firestore;
    CollectionReference personasCollection;
    Persona personaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        vincularElementos();
        iniciarFirebase();
        listarDatos();

        //traemos los datos del listview y llenamos los Editext para poder editar y actualizar
        datosPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSeleccionada = (Persona) parent.getItemAtPosition(position);
                edtNombre.setText(personaSeleccionada.getNombre());
                edtApellido.setText(personaSeleccionada.getApellido());
                edtCorreo.setText(personaSeleccionada.getCorreo());
                edtTelefono.setText(personaSeleccionada.getTelefono());
                edtContrasena.setText(personaSeleccionada.getContrasena());
            }
        });
    }

    //vinculamos los elementos del layout con  las variables
    private void vincularElementos() {
        edtNombre = findViewById(R.id.edt_nombre);
        edtApellido = findViewById(R.id.edt_apellido);
        edtCorreo = findViewById(R.id.edt_correo);
        edtTelefono = findViewById(R.id.edt_telefono);
        edtContrasena = findViewById(R.id.edt_contrasena);
        datosPersonas = findViewById(R.id.datos_personas);
    }
    private void listarDatos() {
        personasCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                return;
            }
            listPerson.clear();
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Persona p = doc.toObject(Persona.class);
                    if (p != null) {
                        listPerson.add(p);
                    }
                }
                ArrayAdapter<Persona> arrayAdapterPersona = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listPerson);
                datosPersonas.setAdapter(arrayAdapterPersona);
            }
        });
    }

    private void iniciarFirebase() {
        //llamamos a firestore y damos nombre a la coleccion que contendrá los datos almacenados
        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();
        personasCollection = firestore.collection("Personas");
    }

    @Override
    //vincular menu ActionBar con el activity para opciones del CRUD
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //traemos los datos contenidos en cada elemento del layout y lo almacenamos en variables
        String nombre = edtNombre.getText().toString();
        String apellido = edtApellido.getText().toString();
        String correo = edtCorreo.getText().toString();
        String telefono = edtTelefono.getText().toString();
        String contrasena = edtContrasena.getText().toString();

        int itemId = item.getItemId();
        //Guardar datos en firestore
        if (itemId == R.id.icon_add) {
            if (nombre.equals("") || apellido.equals("") || correo.equals("") || telefono.equals("") || contrasena.equals("")) {
                validacion();
            } else {
                agregarPersona(nombre, apellido, correo, telefono, contrasena);
                Toast.makeText(this, "Datos agregados Correctamente!", Toast.LENGTH_LONG).show();
                //limpiamos los editext posterior al guardado de datos
                limpiarTextViews();
            }
            //actualiza los datos
        } else if (itemId == R.id.icon_save) {
            actualizarPersona(nombre, apellido, correo, telefono, contrasena);
            limpiarTextViews();

            Toast.makeText(this, "Datos actualizados Correctamente!", Toast.LENGTH_LONG).show();
            //eliminamos un dato
        } else if (itemId == R.id.icon_delete) {
            eliminatPersona();
            limpiarTextViews();
            Toast.makeText(this, "Item Eliminado Correctamente!", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    //funcion para insertar datos en firestore
    private void agregarPersona(String nombre, String apellido, String correo, String telefono, String contrasena) {
        Persona persona = new Persona();
        persona.setUid(UUID.randomUUID().toString());
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setCorreo(correo);
        persona.setTelefono(telefono);
        persona.setContrasena(contrasena);

        personasCollection.document(persona.getUid()).set(persona);
    }

    //funcion para actualizar datos
    private void actualizarPersona(String nombre, String apellido, String correo, String telefono, String contrasena) {
        Persona persona = new Persona();
        persona.setUid(personaSeleccionada.getUid());
        persona.setNombre(edtNombre.getText().toString().trim());
        persona.setApellido(edtApellido.getText().toString().trim());
        persona.setCorreo(edtCorreo.getText().toString().trim());
        persona.setTelefono(edtTelefono.getText().toString().trim());
        persona.setContrasena(edtContrasena.getText().toString().trim());

        personasCollection.document(persona.getUid()).set(persona);
    }

    //funcion para eliminar datos de firestore
    private void eliminatPersona(){
        Persona persona = new Persona();
        persona.setUid(personaSeleccionada.getUid());
        personasCollection.document(persona.getUid()).delete();
    }

    private void limpiarTextViews() {
        edtNombre.setText("");
        edtApellido.setText("");
        edtCorreo.setText("");
        edtTelefono.setText("");
        edtContrasena.setText("");
    }

    private void validacion() {
        //validamos que campo nombre no quede vacio
        String nombre = edtNombre.getText().toString();
        if (nombre.equals("")) {
            edtNombre.setError("Campo obligatorio");
        }
    }
}

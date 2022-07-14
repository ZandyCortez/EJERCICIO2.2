package com.pm1.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pm1.retrofit.interfaces.UsuariosApi;
import com.pm1.retrofit.models.Usuarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ListView listaP;
    ArrayList<String> titulos = new ArrayList<>();
    ArrayAdapter Aadapter;
    TextView txtrespuesta;
    EditText editid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtrespuesta=(TextView)findViewById(R.id.responseBody);
        editid=(EditText) findViewById(R.id.txtid);
        editid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()<=0 || editable.toString().isEmpty()){}
                else{
                    if(editable.toString().length()>=101){
                        txtrespuesta.setText("");
                        editid.setText("");
                    }else{
                        obtenerListaPersona(editable.toString());
                    }
                }
            }
        });
        Aadapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,titulos);
        listaP=(ListView) findViewById(R.id.listaPerson);
        listaP.setAdapter(Aadapter);
    }

    private void obtenerListaPersona(String textto) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsuariosApi usuariosApi = retro.create(UsuariosApi.class);

        Call<Usuarios> callbody = usuariosApi.getUsuarios2(Integer.parseInt(textto.trim()));
        callbody.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if(response.isSuccessful()){
                    response.body(); // contiene la data bajada el body
                    int id = response.body().getId();
                    int userName = response.body().getUserId();
                    String title = response.body().getTitle();
                    String body = response.body().getBody();
                    txtrespuesta.setText(id+" | "+userName+" | "+title+" | "+body);
                }else{
                    try {
                        Toast.makeText(getApplicationContext(),response.errorBody().string(),Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //Log.i(response.body().string(),"OnReponse");
                //String returnBodyText = response.body().toString();
                //Toast.makeText(getApplicationContext(),returnBodyText,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {}
        });

        Call<List<Usuarios>> callista = usuariosApi.getUsuarios();
        callista.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                for (Usuarios usuario:response.body()){
                    Log.i(usuario.getTitle(),"OnReponse");
                    titulos.add(usuario.getBody());
                    Aadapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {t.getMessage();}
        });
    }
}
package com.pm1.retrofit.interfaces;

import com.pm1.retrofit.models.Usuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuariosApi
{
    String ruta0 = "/posts";
    String ruta1 = "/posts/{valor}";

    @GET(ruta0)
    Call<List<Usuarios>> getUsuarios();

    @GET(ruta1)
    Call<Usuarios> getUsuarios2(@Path(value="valor") int id);
}

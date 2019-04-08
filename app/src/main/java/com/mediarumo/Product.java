package com.mediarumo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Product extends RealmObject {

    @PrimaryKey
    public int id;
    public String nome;
    public String descricao;
    public String model;
    public float preco;
    public int quantidade;
    @SerializedName("urlLink")
    public String urlLink;
}

package com.mediarumo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Usuario extends RealmObject {

    @PrimaryKey
    private String usuarioId;
    private String usuarioEmail;
    private String usuarioNome;
    @SerializedName("usuarioPic")
    private String usuarioPic;
    public String usuarioToken;

    public Usuario() {}

    public Usuario(String usuarioId, String usuarioEmail, String usuarioNome, String usuarioPic) {
        this.usuarioId = usuarioId;
        this.usuarioEmail = usuarioEmail;
        this.usuarioNome = usuarioNome;
        this.usuarioPic = usuarioPic;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioPic() {
        return usuarioPic;
    }

    public void setUsuarioPic(String usuarioPic) {
        this.usuarioPic = usuarioPic;
    }
}


package com.example.usuario.kprueba.response.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("queryC")
    @Expose
    private QueryC queryC;

    public QueryC getQueryC() {
        return queryC;
    }

    public void setQueryC(QueryC queryC) {
        this.queryC = queryC;
    }

}

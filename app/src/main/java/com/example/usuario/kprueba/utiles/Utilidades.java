package com.example.usuario.kprueba.utiles;

public class Utilidades {

    public String urlRutaGoogle(String origin, String destination){
        return "https://maps.googleapis.com/maps/api/directions/json?origin="+origin+
                "&destination="+destination+"&key=AIzaSyBmjVTwfD-qrZKM0WWjBtpDnARdfNYjp_U";
    }

}

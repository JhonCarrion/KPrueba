package com.example.usuario.kprueba.utiles;

import android.app.Application;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class ChatApplication extends Application {

    public ChatApplication() {
        super();
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Variables_Globales.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}

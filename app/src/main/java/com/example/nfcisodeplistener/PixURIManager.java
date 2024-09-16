package com.example.nfcisodeplistener;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class PixURIManager {

    private final List<PixURIListener> listeners = new ArrayList<>();

    private static final PixURIManager instance = new PixURIManager();
    private PixURIManager(){}

    public static PixURIManager getInstance() {
        return instance;
    }

    public void newPixURI(Uri pixURI) {
        this.listeners.forEach(pixURIListener -> {
            pixURIListener.onNewPixURI(pixURI);
        });
    }

    public void addOnNewPixURIListener(PixURIListener pixURIListener) {
        this.listeners.add(pixURIListener);
    }
}

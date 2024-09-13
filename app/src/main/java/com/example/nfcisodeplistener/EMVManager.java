package com.example.nfcisodeplistener;

import java.util.ArrayList;
import java.util.List;

public class EMVManager {

    private final List<EMVListener> listeners = new ArrayList<>();

    private static final EMVManager instance = new EMVManager();
    private EMVManager(){}

    public static EMVManager getInstance() {
        return instance;
    }

    public void notify(String emv) {
        this.listeners.forEach(emvListener -> {
            emvListener.onNewEMV(emv);
        });
    }

    public void addOnNewEMVListener(EMVListener emvListener) {
        this.listeners.add(emvListener);
    }
}

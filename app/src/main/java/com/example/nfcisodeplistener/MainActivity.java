package com.example.nfcisodeplistener;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PixURIListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PixURIManager.getInstance().addOnNewPixURIListener(this);
    }

    @Override
    public void onNewPixURI(Uri pixURI) {
        new Handler(Looper.getMainLooper()).post(()->{
           TextView textView = findViewById(R.id.textView);
            List<String> qr = pixURI.getQueryParameters("qr");
            if(qr.size() != 1) {
                textView.setText(R.string.qrcode_nao_encontrado);
            }else{
                textView.setText(qr.get(0));
            }

        });
    }
}
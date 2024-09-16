package com.example.nfcisodeplistener;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PixURIListener {

    private EditText emvEditText;
    private EditText uriEditText;
    private TextView tamanhoURI;
    private TextView tamanhoEMV;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        emvEditText = findViewById(R.id.emv_edittext);
        uriEditText = findViewById(R.id.uri_edittext);
        tamanhoURI = findViewById(R.id.tamanho_uri);
        tamanhoEMV = findViewById(R.id.tamanho_emv);
        findViewById(R.id.limpar).setOnClickListener(view -> {
            emvEditText.setText("");
            uriEditText.setText("");
            tamanhoURI.setText("-");
            tamanhoEMV.setText("-");
        });

        PixURIManager.getInstance().addOnNewPixURIListener(this);
    }

    @Override
    public void onNewPixURI(Uri pixURI) {
        new Handler(Looper.getMainLooper()).post(()->{

            List<String> qr = pixURI.getQueryParameters("qr");
            if(qr.size() != 1) {
                emvEditText.setText(R.string.qrcode_nao_encontrado);
            }else{
                String qrCode = qr.get(0);
                emvEditText.setText(qrCode);
                tamanhoEMV.setText("tamanho:"+qrCode.length());
            }

            
            String uri = pixURI.toString();
            uriEditText.setText(uri);
            tamanhoURI.setText("tamanho:"+uri.length());
        });
    }
}
package com.example.network;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PHONE_STATE_PERMISSION = 1;
    private TelephonyManager telephonyManager;
    private SignalStrengthListener signalStrengthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_PHONE_STATE_PERMISSION);
                } else {
                    startSignalStrengthMonitoring();
                }
            }
        });
    }

    private void startSignalStrengthMonitoring() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        signalStrengthListener = new SignalStrengthListener();
        telephonyManager.listen(signalStrengthListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private void stopSignalStrengthMonitoring() {
        if (telephonyManager != null && signalStrengthListener != null) {
            telephonyManager.listen(signalStrengthListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSignalStrengthMonitoring();
    }

    private class SignalStrengthListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int signalStrengthDbm = getSignalStrengthDbm(signalStrength);
            int signalStrengthAsu = getSignalStrengthAsu(signalStrength);
            openSignalStrengthActivity(signalStrengthDbm, signalStrengthAsu);
            stopSignalStrengthMonitoring();
        }
    }

    private int getSignalStrengthDbm(SignalStrength signalStrength) {
        if (signalStrength != null) {
            int signalStrengthDbm = signalStrength.getGsmSignalStrength();
            if (signalStrengthDbm != 99) {
                // Signal strength is in dBm
                return -113 + 2 * signalStrengthDbm;
            }
        }
        return 0;
    }

    private int getSignalStrengthAsu(SignalStrength signalStrength) {
        if (signalStrength != null) {
            int signalStrengthDbm = signalStrength.getGsmSignalStrength();
            if (signalStrengthDbm != 99) {
                // Signal strength is in dBm
                int asu = (signalStrengthDbm <= -113) ? 0 : (signalStrengthDbm >= -51) ? 31 : (signalStrengthDbm + 113) / 2;
                return asu;
            }
        }
        return 0;
    }

    private void openSignalStrengthActivity(int signalStrengthDbm, int signalStrengthAsu) {
        Intent intent = new Intent(MainActivity.this, SignalStrengthActivity.class);
        intent.putExtra("SIGNAL_STRENGTH_DBM", signalStrengthDbm);
        intent.putExtra("SIGNAL_STRENGTH_ASU", signalStrengthAsu);
        startActivity(intent);
    }
}

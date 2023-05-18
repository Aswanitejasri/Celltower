package com.example.network;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SignalStrengthActivity extends AppCompatActivity {

    private TextView signalStrengthDbmTextView;
    private TextView signalStrengthAsuTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_strength);

        signalStrengthDbmTextView = findViewById(R.id.signalStrengthDbmTextView);
        signalStrengthAsuTextView = findViewById(R.id.signalStrengthAsuTextView);

        // Retrieve the signal strength values passed from MainActivity
        int signalStrengthDbm = getIntent().getIntExtra("SIGNAL_STRENGTH_DBM", 0);
        int signalStrengthAsu = getIntent().getIntExtra("SIGNAL_STRENGTH_ASU", 0);

        // Display the signal strength values
        signalStrengthDbmTextView.setText("Signal Strength (dBm): " + signalStrengthDbm);
        signalStrengthAsuTextView.setText("Signal Strength (ASU): " + signalStrengthAsu);
    }
}

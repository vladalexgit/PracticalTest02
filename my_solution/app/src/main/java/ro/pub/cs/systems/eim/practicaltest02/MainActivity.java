package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_load_url = findViewById(R.id.btn_load_url);
        Button btn_connect_srvr = findViewById(R.id.bt_connect);

        final EditText et_requested_url = findViewById(R.id.et_url);
        final EditText et_srvr_port = findViewById(R.id.et_port);

        final TextView tv_result = findViewById(R.id.textView);

        btn_connect_srvr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverPort = et_srvr_port.getText().toString();
                if (serverPort == null || serverPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                serverThread = new ServerThread(Integer.parseInt(serverPort));
                if (serverThread.getServerSocket() == null) {
                    Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                    return;
                }
                serverThread.start();
            }
        });

        btn_load_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientAddress = "127.0.0.1";
                String clientPort = et_srvr_port.getText().toString();
                if (clientAddress == null || clientAddress.isEmpty()
                        || clientPort == null || clientPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serverThread == null || !serverThread.isAlive()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String requestedUrl = et_requested_url.getText().toString();

                if (requestedUrl == null || requestedUrl.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client requested URL should be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

//                tv_result.setText("");

                clientThread = new ClientThread(
                        clientAddress, Integer.parseInt(clientPort), requestedUrl, tv_result
                );
                clientThread.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}

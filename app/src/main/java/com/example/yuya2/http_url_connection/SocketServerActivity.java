package com.example.yuya2.http_url_connection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerActivity extends BaseActivity {
    TextView socketServerTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_server);
        socketServerTitle = (TextView) findViewById(R.id.socket_server_title);
        findViewById(R.id.socket_server_back).setOnClickListener((View v)->{
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            ServerSocket server = new ServerSocket(3838, 5);
            while(true){
                socketServerTitle.setText("サーバーは稼働しています。");
                System.out.println("サーバーは稼働しています。");
                Socket socket = server.accept();
                PrintWriter output = new PrintWriter(socket.getOutputStream());
                output.println("こんにちは！こちらはサーバーです！");
                output.close();     // PrintWriterはclose()で閉じるのが基本
                socket.close();     // Socketはclose()で閉じるのが基本
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}

package com.example.yuya2.http_url_connection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {

    private UploadTask task;
    private TextView textView;
    // wordを入れる
    private EditText uriEditText;
    private EditText amountEditText;
    private String param = "";

    String port;
    String url;
    int amount;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uriEditText = findViewById(R.id.uriname);
        amountEditText = findViewById(R.id.value_amount);

        Button post = findViewById(R.id.post);
        Date date = new Date();
        final String dateStr = date.toString();
        final String dataKey = "date";

        final List<String> answerList = new ArrayList<>();


        // ボタンをタップして非同期処理を開始
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                port = uriEditText.getText().toString();
                amount = Integer.parseInt(amountEditText.getText().toString());

                //まずはテスト用のArrayListに中身をいれる、実際のアンケートデータを想定
                //中身は、value1,value2,value3,...valueN
                for(int i = 0; i<amount; i++){
                    StringBuilder sb = new StringBuilder();
                    sb.append("value");
                    sb.append(i+1);
                    answerList.add(sb.toString());
                }

                //できたArrayListをリクエストパラメータにしていく、ここからが実際のアプリへの実装
                StringBuilder sb = new StringBuilder();
                for (int i=0; i<answerList.size(); i++) {

                    sb.append("&param");
                    sb.append(i);
                    sb.append("=");
                    sb.append(answerList.get(i).toString());
                }
                param = new StringBuilder().append(dataKey).append("=").append(dateStr).append(sb.toString()).toString();


                if(param.length() != 0){
                    task = new UploadTask();
                    task.setListener(createListener());
                    task.execute(param,port);
                }

            }
        });

        // ブラウザを起動する
        Button browser = findViewById(R.id.browser);
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // phpで作成されたhtmlファイルへアクセス
                url = "https://localhost:"+port+"/result.jsp";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

                // text clear
                textView.setText("");
            }
        });

        textView = findViewById(R.id.text_view);
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    protected void onDestroy() {
        task.setListener(null);
        super.onDestroy();
    }

    private UploadTask.Listener createListener() {
        return new UploadTask.Listener() {
            @Override
            public void onSuccess(String result) {
                textView.setText(result);
            }
        };
    }
}
package cn.hhit.okhhtp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
       // getAsynchronous();
        getsynchronous();
    }

    void getAsynchronous() {


        //创建客户端
        //OkHttpClient client=new OkHttpClient();
        //OkHttpClient.Builder创建的对象，调用connection(),再调用bulid()返回okHttpClient对象
        OkHttpClient client = new OkHttpClient.Builder()
                //TimeUnit.MILLISECONDS  毫秒单位
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .build();
        //创建请求对象
        final Request request = new Request.Builder()
                .get()
                .url("http://10.0.2.2:9102/get/text")
                .build();
        //以上的写法是简法
//            Request.Builder builder=new Request.Builder();
//            builder.url("");
//            builder.get();
//            Request request=builder.build();
        //用客户端client创建任务（异步）  call呼叫
        Call task = client.newCall(request);
        //enqueue：入队  Callback：打回来
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("text", "task fail");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int responseCode = response.code();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    String str = body.string();
                    Log.e("text", str);

                }

            }
        });


    }
    OkHttpClient client=null;
    Request request=null;
    void getsynchronous(){
        //创建客户端
        //OkHttpClient client=new OkHttpClient();
        //OkHttpClient.Builder创建的对象，调用connection(),再调用bulid()返回okHttpClient对象
         client= new OkHttpClient.Builder()
                //TimeUnit.MILLISECONDS  毫秒单位
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .build();
        //创建请求对象
          request= new Request.Builder()
                .get()
                .url("http://10.0.2.2:9102/get/text")
                .build();
        //以上的写法是简法
//            Request.Builder builder=new Request.Builder();
//            builder.url("");
//            builder.get();
//            Request request=builder.build();

        new MyAsy().execute();



    }
    class MyAsy extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Response response = client.newCall(request).execute();
                  //获取响应头
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.e("text",responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                ResponseBody body=response.body();

                String str=body.string();
                Log.e("text",str);
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }



}

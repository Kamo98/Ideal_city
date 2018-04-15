package com.example.admin.ideal_city;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


import java.io.File;
import java.io.IOException;
import java.util.List;

public class Activity_Add_New_Obr extends AppCompatActivity {

    private Button but_sendNewObr;
    private Spinner spinner_category;
    private EditText editText_Addres;
    private EditText editText_Description;
    private WebView myBrowser;
    private AsyncTask<?, ?, ?> mRequestTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__new__obr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        spinner_category = (Spinner)findViewById(R.id.spinner_category);
        editText_Addres = (EditText)findViewById(R.id.ed_tx_adress);
        editText_Description = (EditText)findViewById(R.id.ed_tx_description);

        but_sendNewObr = (Button)findViewById(R.id.button_send_obr);

        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @TargetApi(Build.VERSION_CODES.N) @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };

       // webViewClient.onLoadResource();


        but_sendNewObr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i("chat", spinner_category.getSelectedItem().toString());
                String req_str = "https://rusmih98.000webhostapp.com/requests.php?action=insert&category=3"
                        /*spinner_category.getSelectedItem().toString()*/ + "&address=" + editText_Addres.getText() + "&address_x=0&address_y=0&image=SSILKA" +
                        "&user_id=123&description=" + editText_Description.getText();

                String url_server_img = "https://rusmih98.000webhostapp.com/get_image.php";
                String url_img = "H:/MyProjects/Tehnotrek_Lessons/Ideal_City/test.jpg";
                String name_of_file = "image";
                mRequestTask = new HttpAsyncRequest(Activity_Add_New_Obr.this).execute(req_str, name_of_file, url_img, url_server_img);

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


}

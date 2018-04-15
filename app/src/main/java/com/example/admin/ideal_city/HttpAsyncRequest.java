
package com.example.admin.ideal_city;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.Header;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class HttpAsyncRequest extends AsyncTask<String, Integer, String> {
    //private WeakReference<MainActivity.RequestListener> mListener;
    protected int mErrorStringID;
    private TextView tv_output;
    private Context context;

    public HttpAsyncRequest(Context context/*MainActivity.RequestListener listener*/) {
        //mListener = new WeakReference<>(listener);
       // this.tv_output = tv_output;
        this.context = context;
    }


    @Override
    protected void onPreExecute()
    {
    // Здесь находится то, что должно исполниться до того, как
    // начнётся основная работа. Например, вывод уведомления о
    // том, что пользователю следует подождать
        super.onPreExecute();
        /*mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(context.getResources().getString(R.string.text_processing));
        mProgressDialog.setMessage(context.getResources().getString(R.string.text_receiving_data));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();*/
    }

    @Override
    protected String doInBackground(String... params) {
        // тут у тебя обязательно в конце должен быть return,
        // отдающий String с тем значением, которое тебе нужно.
        // Почему именно String? Потому что ты его указал в
        // <Void, Void, String>



        if (params != null && params.length > 0) {
            List<NameValuePair> list_img = new ArrayList<NameValuePair>();
            list_img.add(new BasicNameValuePair(params[1], params[2]));
            post(params[3], list_img);

            HttpRequest request = new HttpRequest(params[0]);
            int status = request.makeRequest();

            if (status == HttpRequest.REQUEST_OK) {
                return request.getContent();
                //return StringUtils.join(Arrays.asList(request.getHeaders(), request.getContent()), "\n");
            }
            else if (status == HttpRequest.REQUEST_REDIRECT) {
                return doInBackground(request.getRedirectURL());
            }
            else {
                mErrorStringID = request.getErrorStringId();
            }
        }
        else {
            mErrorStringID = R.string.too_few_params;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // Обрати внимание, что данный метод ничего не должен
        // возвращать. Он обязательно должен быть void. Зато он
        // получает в качестве аргумента ответ, который посылает
        // doInBackground. И тут ты можешь сделать с ним что твоей
        // душе угодно. В твоём случае тебе с ним делать ничего не
        // надо, но на будущее запомни.
        super.onPostExecute(result); // это ОБЯЗАТЕЛЬНО нужно сделать, иначе
        // .get() не вернёт ничего, поскольку ни о каком результате понятия
        // иметь не будет.
        //mProgressDialog.dismiss();

        try {
            // ответ превратим в JSON массив
            JSONArray ja = new JSONArray(result);
            JSONObject jo;

            Integer i = 0;

            if (ja.length() > 0) {
                while (i < ja.length()) {

                    // разберем JSON массив построчно
                    jo = ja.getJSONObject(i);

                    String out = "categoryid = " + jo.getString("categoryid") + " \n" + "id_of_user = " + jo.getString("id_of_user") + " \n" + "description = " + jo.getString("description");
                    tv_output.setText(out);
                    i++;

                }
            }else {

            }
        } catch (Exception e) {
            // если ответ сервера не содержит валидный JSON
            Log.i("chat",
                    "+ FoneService ---------- ошибка ответа сервера:\n"
                            + e.getMessage());
        }
    }

    public void post(String url, List<NameValuePair> nameValuePairs) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);


        /*SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("text", "some string");
        try {
            params.put("image", new File("/mnt/sdcard/DCIM/Camera/1.jpg"));
        } catch (FileNotFoundException e) {e.printStackTrace(); }
        client.post(MAIN_URL + "/mapi/photo.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
            }
        });*/


        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        final File file = new File(nameValuePairs.get(0).getValue());
        FileBody fb = new FileBody(file);

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();

        builder.addPart("file", fb);
        builder.addTextBody("userName", "userName");
        builder.addTextBody("password", "password");
        builder.addTextBody("macAddress",  macAddress);


        httpPost.setEntity(builder.build());


        /*try {
            HttpResponse response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //return getContent(response);

        //try {

            //MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            /* example for setting a HttpMultipartMode */
            //builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            /* example for adding an image part */
            /*File file_img = new File(nameValuePairs.get(0).getValue());
            FileBody fileBody = new FileBody(file_img); //image should be a String
            builder.addPart("my_file", fileBody);*/


            /*MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File(nameValuePairs.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                }
            }
*/
            /*HttpEntity ent = builder.build();
            httpPost.setEntity(ent);*/

            //ttpResponse response = httpClient.execute(httpPost, localContext);
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
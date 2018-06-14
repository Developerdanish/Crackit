package com.example.danish.crackit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = "QueryUtils";


    public QueryUtils() {
    }

    public static List<Question> fetchQuestions(String requestUrl){
        Log.i(LOG_TAG, "Fetching questions from web");

        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Question> questions = extractFeatureFromJson(jsonResponse);

        return questions;
    }

    private static ArrayList<Question> extractFeatureFromJson(String json){
        ArrayList<Question> questions = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(json);
            JSONArray questionArray = root.getJSONArray("questions");

            for (int i=0; i<questionArray.length(); i++){
                JSONObject ques = questionArray.getJSONObject(i);

                String quesString = ques.getString("question");
                String ansString = ques.getString("Answer");

                Question newQues = new Question(quesString, ansString);

                questions.add(newQues);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        questions = removeDuplicate(questions);

        return questions;
    }

    private  static ArrayList<Question> removeDuplicate(ArrayList<Question> questions){
        ArrayList<Question> moddedQuestions = new ArrayList<>();

        for (int i=0; i<questions.size(); i++){
            int flag = 0;
            Question currentQues = questions.get(i);
            for (int j=0; j<moddedQuestions.size(); j++){
                if(currentQues.getmQuestion().equals(moddedQuestions.get(j).getmQuestion())){
                    flag = 1;
                    break;
                }
            }
            if (flag == 0){
                moddedQuestions.add(currentQues);
            }
        }

        return moddedQuestions;

    }

    private static URL createUrl(String StringUrl){
        URL url = null;
        try{
            url = new URL(StringUrl);
        }catch (MalformedURLException exception){
            Log.e(LOG_TAG, "Error creating url", exception);
            return null;
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try{
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error responce code: "  + httpURLConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving JSON response");
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream){
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null){
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "ERROR", e);
            }
        }
        return output.toString();
    }
}

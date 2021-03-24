package com.example.create_keyboard1.Otherclasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;
import java.util.Locale;

public class Speech extends Activity {
    static ArrayList<String> results;
    static float[] confidence;
    public static String spoken;
    public static boolean i = true;
    public static InputConnection ic;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Specify free form input
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Some thing..");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == RESULT_OK) {

            results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String confidenceExtra = RecognizerIntent.EXTRA_CONFIDENCE_SCORES;
            confidence = data.getFloatArrayExtra(confidenceExtra);
            // TODO Do something with the recognized voice strings
        }

        spoken = null;
        spoken=getWord();
        SimpleIME.ic.commitText(spoken, 1);
        //Main m =new Main();
        //m.onKey(28, key)
        //broadcastIntent();
//        finish();

    }

    public  String getWord() {
        float max = confidence[0];
        int j = 0;
        for (int i = 0; i < results.size(); i++) {
            if (max < confidence[i]) {
                j = i;
                max = confidence[i];
            }
        }
        //Log.i("main", "" + spoken);
        i = true;
        spoken=results.get(j);
     //   Log.i("main", "" + spoken);
        return spoken;
    }
/*  public void broadcastIntent()
    {
        Intent intent =new Intent();
        intent.setAction("ttsdone");
        sendBroadcast(intent);
    }*/

}

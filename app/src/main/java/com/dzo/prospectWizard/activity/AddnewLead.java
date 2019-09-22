package com.dzo.prospectWizard.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dzo.prospectWizard.ImageView.Action;
import com.dzo.prospectWizard.ImageView.CustomGallery;
import com.dzo.prospectWizard.ImageView.GalleryAdapter;
import com.dzo.prospectWizard.R;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.dzo.prospectWizard.R.id.i;


public class AddnewLead extends Activity {

    EditText lead_note;
    TextView userName, mail, phone;
    String edit, Message;
    Button submit;


    CustomGallery item;
    Handler handler;
    GalleryAdapter adapter;
    String[] all_path;
    private static final int CONTACT_PICK_REQUEST = 1000;
    boolean status = true;
    private long Time;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    Button buttonStart, buttonStop, buttonPlayLastRecordAudio, buttonStopPlayingRecording, Camera;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    String  encodedImage;
    ProgressDialog prgDialog;
    String encodedString = "";
    RequestParams params = new RequestParams();
    String imgPath, fileName, rep_id;
    String[] path;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    ImageView imgView;
    Uri savedUri;
    String selectedPath = "";
    ProgressDialog dialog;
    TextView  Timer_set;
    ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
    String contact_id;
    LinearLayout addnewLead,addlead;
    SharedPreferences sharedPreferences,sharedPreferences1;
    Typeface face;
    String Font;
    int Color1;
    CustomGallery temp_item = new CustomGallery();

    private GoogleApiClient client2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lead_activity);
        addnewLead=(LinearLayout)findViewById(R.id.addnewLead);
        addlead=(LinearLayout)findViewById(R.id.addlead);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        addnewLead.setBackgroundColor(Color1);
        addlead.setBackgroundColor(Color1);

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        if (Font != null){
            face= Typeface.createFromAsset(getAssets(),Font);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddnewLead.this);
        rep_id = preferences.getString("UserName", "");
        Intent intent=getIntent();
        Message=intent.getStringExtra("firstNames");
       String phoneNo=intent.getStringExtra("phone");
        String lname=intent.getStringExtra("lastname");
        contact_id=intent.getStringExtra("contact_ids");
         Log.e("jhdfshj",""+contact_id);

        userName = (TextView) findViewById(R.id.userName);
        userName.setText(Message);
        mail = (TextView) findViewById(R.id.mail);
        mail.setText(lname);
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(phoneNo);
        lead_note = (EditText) findViewById(R.id.lead_note);
        submit = (Button) findViewById(R.id.submit);
        buttonStart = (Button) findViewById(R.id.record);
        buttonStop = (Button) findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button) findViewById(R.id.button4);
        edit = lead_note.getText().toString();

        if (face != null){

            face = Typeface.createFromAsset(getAssets(), Font);
            userName.setTypeface(face);
            mail.setTypeface(face);
            phone.setTypeface(face);
            lead_note.setTypeface(face);
            submit.setTypeface(face);
        }

        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);


        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);
        handler = new Handler();
        random = new Random();




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(buttonStop.isEnabled()){
                    timeInMilliseconds = 0L;
                    customHandler.removeCallbacks(updateTimerThread);
                    buttonStop.setVisibility(View.GONE);
                    buttonStart.setVisibility(View.VISIBLE);
                    buttonPlayLastRecordAudio.setVisibility(View.VISIBLE);
                    mediaRecorder.stop();
                    buttonStop.setEnabled(false);
                    buttonPlayLastRecordAudio.setEnabled(true);
                    buttonStart.setEnabled(true);
                    buttonStopPlayingRecording.setEnabled(false);
                    Toast.makeText(AddnewLead.this, "Recording Completed",
                            Toast.LENGTH_LONG).show();
                }

                if(edit==null && AudioSavePathInDevice != null){
                    if (AudioSavePathInDevice != null) {
                        dialog = ProgressDialog.show(AddnewLead.this, "", "Uploading File...", true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                uploadFile(AudioSavePathInDevice);
                            }
                        }).start();
                    }
                }
               else if(edit!=null && AudioSavePathInDevice == null){
                    Update();

                }
               else if(edit!=null && AudioSavePathInDevice != null){
                    Update();
                    if (AudioSavePathInDevice != null) {
                        dialog = ProgressDialog.show(AddnewLead.this, "", "Uploading File...", true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                uploadFile(AudioSavePathInDevice);
                            }
                        }).start();
                    }
                }

            }
        });


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonStart.setVisibility(View.GONE);
                buttonStop.setVisibility(View.VISIBLE);
                customHandler.postDelayed(updateTimerThread, 0);

                if (checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    Toast.makeText(AddnewLead.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();

                }


            }
        });


        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeInMilliseconds = 0L;
                customHandler.removeCallbacks(updateTimerThread);
                buttonStop.setVisibility(View.GONE);
                buttonStart.setVisibility(View.VISIBLE);
                buttonPlayLastRecordAudio.setVisibility(View.VISIBLE);
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                Toast.makeText(AddnewLead.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonPlayLastRecordAudio.setVisibility(View.GONE);
                buttonStopPlayingRecording.setVisibility(View.VISIBLE);
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(AddnewLead.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonStopPlayingRecording.setVisibility(View.GONE);
                buttonPlayLastRecordAudio.setVisibility(View.VISIBLE);
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });

        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_LOAD_IMG);

    }

    private void galleryMultipuleIntent() {

        Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(i, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".png");
                imgPath = destination.toString();
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgView.setImageBitmap(thumbnail);
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];

                adapter.clear();
                List<String> a = new ArrayList<String>();
                a.add(imgPath);

                for (String str : a) {
                    item = new CustomGallery();
                    item.sdcardPath = str;
                    dataT.add(item);

                }

                adapter.addAll(dataT);

            } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
                adapter.clear();
                all_path = data.getStringArrayExtra("all_path");
                for (String string : all_path) {
                    item = new CustomGallery();
                    item.sdcardPath = string;
                    dataT.add(item);
                }

                adapter.addAll(dataT);


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
            try {
                if (requestCode == 100) {
                    savedUri = data.getData();
                    Toast.makeText(AddnewLead.this,
                            "Saved: " + savedUri.getPath(),
                            Toast.LENGTH_LONG).show();

                    selectedPath = getPath(savedUri);
                }
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(AddnewLead.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddnewLead.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }


        if (requestCode == CONTACT_PICK_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    private int uploadFile(final String AudioSavePathInDevice) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddnewLead.this);
        String companynURL=preferences.getString("companynURL","");

        String SERVER_URL = companynURL+"/prospect_app_dz/lead_audio_dz.php?rep_id=" + rep_id + "&contact_id=" + contact_id;
        Log.e("kjfukg", "" + SERVER_URL);
        int serverResponseCode = 0;
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(AudioSavePathInDevice);
        String[] parts = AudioSavePathInDevice.split("/");
        final String fileName = parts[parts.length - 1];
        if (!selectedFile.isFile()) {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", AudioSavePathInDevice);

                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + AudioSavePathInDevice + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddnewLead.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(AddnewLead.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddnewLead.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }
    }

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {
                Bitmap bm = BitmapFactory.decodeFile(imgPath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                //options.inSampleSize = 1200;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                Log.e("stdsvcx", "" + encodedString);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    public void makeHTTPCall() {
        prgDialog.setMessage("Invoking Php");
        AsyncHttpClient client = new AsyncHttpClient();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddnewLead.this);
        String companynURL=preferences.getString("companynURL","");
        client.post(companynURL+"/api/lead_image.php",
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.hide();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        prgDialog.hide();
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {

            }


            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    CONTACT_PICK_REQUEST);
        }
    }


    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AddnewLead.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;

            int milliseconds = (int) (updatedTime % 1000);


            customHandler.postDelayed(this, 0);

        }

    };


    public com.google.android.gms.appindexing.Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ManageLead Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new com.google.android.gms.appindexing.Action.Builder(com.google.android.gms.appindexing.Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(com.google.android.gms.appindexing.Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();


        client2.connect();
        AppIndex.AppIndexApi.start(client2, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();


        AppIndex.AppIndexApi.end(client2, getIndexApiAction());
        client2.disconnect();
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            buttonStopPlayingRecording.setVisibility(View.GONE);
            buttonPlayLastRecordAudio.setVisibility(View.VISIBLE);
            buttonStopPlayingRecording.setEnabled(false);
            buttonPlayLastRecordAudio.setEnabled(true);

        }

        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);
            Timer_set.setText(hms);
        }
    }

        public void Update() {
            edit = lead_note.getText().toString();


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddnewLead.this);
            String companynURL=preferences.getString("companynURL","");
            String UPDATE_URL = companynURL+"/api/update_note.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.contains("notok")) {

                            } else {
                                Intent i =new Intent(AddnewLead.this, HomeActivity.class);
                                startActivity(i);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddnewLead.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();

                    map.put("contact_id", contact_id);
                    map.put("rep_id", rep_id);
                    map.put("notes", edit);
                    Log.e("jhcdfsyuyuf",""+Message+rep_id+edit);
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AddnewLead.this);
            requestQueue.add(stringRequest);
            requestQueue.getCache().clear();

        }
    @Override
    public boolean dispatchTouchEvent(MotionEvent view) {
        if(getCurrentFocus()!=null){
            InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return super.dispatchTouchEvent(view);
    }
    }

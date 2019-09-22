package com.dzo.prospectWizard.ImageView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dzo.prospectWizard.R;
import com.dzo.prospectWizard.activity.BaseActivity;
import com.dzo.prospectWizard.activity.Utility;
import com.dzo.prospectWizard.activity.VolleyMultipart;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import cz.msebera.android.httpclient.Header;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ManageLeadActivity extends BaseActivity {

    String imgstr;
    CustomGallery item;
    File myNewFolder;
    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;
    String[] all_path;
    String[] temp_all_path;
    String Names, Names1;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    private static final int CONTACT_PICK_REQUEST = 1000;
    String firstNames = "";
    String lastNames = "";
    boolean status = true;
    private long Time;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    Button buttonStart, buttonStop, buttonPlayLastRecordAudio, buttonStopPlayingRecording, Camera,pauseRecorder;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    EditText FirstName, LastName, Email, Phone, Notes;
    String lastChar = "", FirstNames, LastNames, LPhone, LEmail, FirstN, LNotes, encodedImage, preferred_contact_phone, preferred_contact_text, preferred_contact_email,preferred_contact_intro, statements, Firsturltext;
    CheckBox Ctext, Cemail, Cphone,cphone1;
    //ProgressDialog prgDialog;
    String encodedString = "";
//    RequestParams params = new RequestParams();
    String imgPath, imgPath1, fileName, rep_id, Emails, imagePaths;
    String[] path;
    Bitmap bitmap ;
    private static int RESULT_LOAD_IMG = 1;
    private String userChoosenTask, b;
    ImageView imgView;
    Uri savedUri;
    String selectedPath = "";
   ProgressDialog dialog;
    TextView Timer, Timer_set;
    CounterClass timer;
    LinearLayout linear_layout;
    ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
    String contact_id;
    SharedPreferences preferences;
    CustomGallery temp_item = new CustomGallery();
    LinearLayout managelayout;
    SharedPreferences sharedPreferences,sharedPreferences1;
    int Color1;
    String Font;
    Typeface face;
     Uri imageUri;
    String timestamp,timestamp1,timestamp2,timestamp3,timestamp4,EncodeImage,timestamp5,timestamp6,timestamp7;
    public ImageView imageView,imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7;
    String FileName, fname,fname1,fname2,fname3,fname4,fname5,fname6,fname7;

    int a = 0;
    int c = 0;
    private GoogleApiClient client2;
    boolean pause=false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_manage_lead, frameLayout);
        managelayout=(LinearLayout)findViewById(R.id.managelayout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        managelayout.setBackgroundColor(Color1);

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        face = null;
        if (Font != null){
            face= Typeface.createFromAsset(getAssets(),Font);
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        preferences = PreferenceManager.getDefaultSharedPreferences(ManageLeadActivity.this);
        rep_id = preferences.getString("UserName", "");
        Emails = preferences.getString("Email", "");
    //    prgDialog = new ProgressDialog(this);
     //   prgDialog.setCancelable(false);
        pauseRecorder=findViewById(R.id.pauseRecorder);
        Timer = (TextView) findViewById(R.id.timer);
        Timer_set = (TextView) findViewById(R.id.timer_set);
        Ctext = (CheckBox) findViewById(R.id.ctext);
        Cemail = (CheckBox) findViewById(R.id.cemail);
        Cphone = (CheckBox) findViewById(R.id.cphone);
        cphone1= (CheckBox) findViewById(R.id.cphone1);
        FirstName = (EditText) findViewById(R.id.lead_fname);
        LastName = (EditText) findViewById(R.id.lead_lname);
        Email = (EditText) findViewById(R.id.lead_email);
        Phone = (EditText) findViewById(R.id.lead_phone);
        Notes = (EditText) findViewById(R.id.lead_note);
        imgView = (ImageView) findViewById(R.id.imgSinglePick);
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);
        imgView.clearColorFilter();
        buttonStart = (Button) findViewById(R.id.record);
        buttonStop = (Button) findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button) findViewById(R.id.button4);
        Camera = (Button) findViewById(R.id.camera);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);

        if (face != null) {
            FirstName.setTypeface(face);
            LastName.setTypeface(face);
            Email.setTypeface(face);
            Phone.setTypeface(face);
            Notes.setTypeface(face);
            Ctext.setTypeface(face);
            Cemail.setTypeface(face);
            Cphone.setTypeface(face);
            cphone1.setTypeface(face);
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(ManageLeadActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        LEmail = Email.getText().toString().trim();
        FirstN = FirstName.getText().toString().trim();
        editor.putString("Email", LEmail);
        editor.putString("FirstName", FirstN);
        editor.commit();
        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);
        initImageLoader();
        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);
        adapter.clear();
        adapter.clearCache();
        random = new Random();

        Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = Phone.getText().toString().length();


                if (digits > 1)
                    lastChar = Phone.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = Phone.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        Phone.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = PreferenceManager.getDefaultSharedPreferences(ManageLeadActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                int a = adapter.getCount();
                b = String.valueOf(a);
                Log.e("hdhfdj", "" + b);
                editor.putString("counadapter", b);
                editor.commit();
                if (FirstName.getText().toString().length() != 0) {
                    if (c <= 7) {
                        linear_layout.setVisibility(View.VISIBLE);
                        cameraIntent();
                    } else {
                        Toast.makeText(getApplicationContext(), "You have already selected eight images!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter first Name", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Timer.setVisibility(View.VISIBLE);
                buttonStart.setVisibility(View.GONE);
                buttonStop.setVisibility(View.VISIBLE);
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);

                if (checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        pauseRecorder.setVisibility(View.VISIBLE);

                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    Toast.makeText(ManageLeadActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();

                }


            }
        });
        pauseRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    if (pause) {
                        mediaRecorder.resume();
                        pause=false;
                        pauseRecorder.setBackgroundResource(R.drawable.pause);
                        startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 0);
                    } else {
                        mediaRecorder.pause();
                        pause=true;
                        pauseRecorder.setBackgroundResource(R.drawable.play);
                        timeSwapBuff += timeInMilliseconds;
                        customHandler.removeCallbacks(updateTimerThread);
                    }
                }
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ti = Timer.getText().toString();
                String[] namearray = ti.split(":");
                if (namearray.length > 1) {
                    firstNames += namearray[0];
                    lastNames += namearray[1];
                }
                int value = Integer.parseInt(firstNames);
                int values = Integer.parseInt(lastNames);
                int a = (value * 60 * 1000) + (values * 1000);
                Time = a;
                Timer_set.setText(ti);
                Timer_set.setVisibility(View.GONE);
                Timer.setVisibility(View.GONE);
                timeInMilliseconds = 0L;
                updatedTime = 0L;
                timeSwapBuff =0L;
                customHandler.removeCallbacks(updateTimerThread);
                buttonStop.setVisibility(View.GONE);
                buttonStart.setVisibility(View.VISIBLE);
                buttonPlayLastRecordAudio.setVisibility(View.VISIBLE);
                mediaRecorder.stop();
                pauseRecorder.setBackgroundResource(R.drawable.pause);
                pause=false;
                pauseRecorder.setVisibility(View.INVISIBLE);

                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                Toast.makeText(ManageLeadActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {
                timer = new CounterClass(Time, 1000);
                timer.start();
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
                Toast.makeText(ManageLeadActivity.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer.cancel();
                status = true;
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


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
         imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == 0 && resultCode == RESULT_OK) {
              try{
                  bitmap = MediaStore.Images.Media.getBitmap(
                          getContentResolver(), imageUri);
                  ByteArrayOutputStream bos = new ByteArrayOutputStream();
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);

                  // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                  byte[] byteFormat = bos.toByteArray();
                  // get the base 64 string
                  String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                  Log.e("dvvxvx",imgString);
                  File destination = new File(Environment.getExternalStorageDirectory(),
                          System.currentTimeMillis() + ".PNG");
                  // imgPath = destination.toString();
                  if(c==0){


                      imageView.setImageBitmap(bitmap);
                      timestamp = imgString;
                      fname = destination.toString();
                      c=c+1;
                  }else if(c==1){




                      imageView1.setImageBitmap(bitmap);
                      timestamp1 = imgString;
                      fname1 = destination.toString();
                      c++;

                  }else if(c==2){

                      fname2 = destination.toString();


                      imageView2.setImageBitmap(bitmap);
                      timestamp2 = imgString;
                      c=c+1;

                  }else if(c==3){


                      fname3 = destination.toString();


                      imageView3.setImageBitmap(bitmap);
                      timestamp3 = imgString;
                      c=c+1;

                  }else if(c==4) {

                      fname4 = destination.toString();

                      imageView4.setImageBitmap(bitmap);
                      timestamp4 = imgString;
                      c=c+1;

                  }else if(c==5){

                      fname5 = destination.toString();


                      imageView5.setImageBitmap(bitmap);
                      timestamp5 = imgString;
                      c=c+1;

                  }else if(c==6){


                      fname6 = destination.toString();


                      imageView6.setImageBitmap(bitmap);
                      timestamp6 = imgString;
                      c=c+1;

                  }else if(c==7) {

                      fname7 = destination.toString();

                      imageView7.setImageBitmap(bitmap);
                      timestamp7 = imgString;
                      c=c+1;

                  }

              }catch (FileNotFoundException e) {}

                }
            try {
                if (requestCode == 100) {
                    savedUri = data.getData();
                    Toast.makeText(ManageLeadActivity.this,
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
                        Toast.makeText(ManageLeadActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ManageLeadActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
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




    public void uploadImage(View v) {
        if(buttonStop.isEnabled()){
            String ti = Timer.getText().toString();
            String[] namearray = ti.split(":");
            if (namearray.length > 1) {
                firstNames += namearray[0];
                lastNames += namearray[1];
            }
            int value = Integer.parseInt(firstNames);
            int values = Integer.parseInt(lastNames);
            int a = (value * 60 * 1000) + (values * 1000);
            Time = a;
            Timer_set.setText(ti);
            Timer_set.setVisibility(View.GONE);
            Timer.setVisibility(View.GONE);
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
            Toast.makeText(ManageLeadActivity.this, "Recording Completed",
                    Toast.LENGTH_LONG).show();
        }

        if (FirstName.getText().length() == 0) {
            FirstName.setError("Field cannot be left blank.");
        }
        else {
            if (Cphone.isChecked() == true) {
                Cphone.setTag("Y");
            } else if (Cphone.isChecked() == false) {
                Cphone.setTag("N");
            }

            if (Ctext.isChecked() == true) {
                Ctext.setTag("Y");
            } else if (Ctext.isChecked() == false) {
                Ctext.setTag("N");
            }
            if (Cemail.isChecked() == true) {
                Cemail.setTag("Y");
            } else if (Cemail.isChecked() == false) {
                Cemail.setTag("N");
            }
            if (cphone1.isChecked() == true) {
                cphone1.setTag("Y");
            } else if (cphone1.isChecked() == false) {
                cphone1.setTag("N");
            }

            LeadDataUpload();


        }

    }

    private int uploadFile(final String AudioSavePathInDevice) {
        Firsturltext = FirstName.getText().toString().trim();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ManageLeadActivity.this);
        String companynURL=preferences.getString("companynURL","");
        String SERVER_URL = companynURL+"/prospect_app_dz/lead_audio_dz.php?rep_id=" + rep_id+ "&contact_id=" +contact_id;
        Log.e(" ",""+SERVER_URL);
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
         //   dialog.dismiss();

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
                        Toast.makeText(ManageLeadActivity.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(ManageLeadActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ManageLeadActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            //dialog.dismiss();
            return serverResponseCode;
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
       /* if (prgDialog != null) {
            prgDialog.dismiss();
        }*/
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
        ActivityCompat.requestPermissions(ManageLeadActivity.this, new
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
            if(!pause) {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;
                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;

                int milliseconds = (int) (updatedTime % 1000);

                Timer.setText("" + mins + ":"

                        + String.format("%02d", secs));
            }
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


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void LeadDataUpload() {
        FirstNames = FirstName.getText().toString().trim();
        LastNames = LastName.getText().toString().trim();
        LPhone = Phone.getText().toString().trim();
        LEmail = Email.getText().toString().trim();
        LNotes = Notes.getText().toString().trim();
        preferred_contact_text = (String) Ctext.getTag();
        preferred_contact_email = (String) Cemail.getTag();
        preferred_contact_phone = (String) Cphone.getTag();
        preferred_contact_intro =(String) cphone1.getTag();
        if(!LEmail.matches("")) {
            if (!isValidEmail(LEmail)) {
                Email.setError("Invalid Email");
                return;
            }
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ManageLeadActivity.this);
        String companynURL=preferences.getString("companynURL","");
        String LOGIN_URL = companynURL+"/prospect_app_dz/add_prospect_dz.php?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!response.contains("notok")) {


                            try {
                                JSONObject obj=new JSONObject(response);
                                contact_id=obj.getString("statement");


                                FirstName.setText("");
                                LastName.setText("");
                                Phone.setText("");
                                Email.setText("");
                                Notes.setText("");
                                imageView.setImageBitmap(null);
                                imageView1.setImageBitmap(null);
                                imageView2.setImageBitmap(null);
                                imageView3.setImageBitmap(null);
                                imageView4.setImageBitmap(null);
                                imageView5.setImageBitmap(null);
                                imageView6.setImageBitmap(null);
                                imageView7.setImageBitmap(null);
                                buttonPlayLastRecordAudio.setVisibility(View.GONE);

                                if(Ctext.isChecked()){
                                    Ctext.toggle();
                                }if(Cemail.isChecked()){
                                    Cemail.toggle();
                                }if(Cphone.isChecked()){
                                    Cphone.toggle();
                                }if(cphone1.isChecked()){
                                    cphone1.toggle();
                                }



                                //buttonStop.setVisibility(View.GONE);
                                bitmap = null;
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (c !=0) {
                                ImageMultiUploads();
                            } else {
                                Intent i1 = new Intent(ManageLeadActivity.this,ManageLeadActivity.class);
                                startActivity(i1);
                                finish();
                            }
                            if (AudioSavePathInDevice != null) {
                               // dialog = ProgressDialog.show(ManageLeadActivity.this, "", "Uploading File...", true);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadFile(AudioSavePathInDevice);

                                        AudioSavePathInDevice = null;
                                    }
                                }).start();
                            }
                            Toast.makeText(ManageLeadActivity.this, "Data sent successfully", Toast.LENGTH_SHORT).show();

                           /* Intent i = new Intent(ManageLeadActivity.this, ManageLeadActivity.class);
                            Toast.makeText(ManageLeadActivity.this, "data sent successfully", Toast.LENGTH_SHORT).show();
                            startActivity(i);*/


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("contact_id", "a");
                map.put("rep_id", rep_id);
                map.put("firstname", FirstNames);
                map.put("lastname", LastNames);
                map.put("phone", LPhone);
                map.put("email", LEmail);
                map.put("notes", LNotes);
                map.put("preferred_contact_email", preferred_contact_email);
                map.put("preferred_contact_text", preferred_contact_text);
                map.put("preferred_contact_phone", preferred_contact_phone);
                map.put("preferred_contact_intro", preferred_contact_intro);



                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void ImageMultiUploads(){
        Log.e("fvgvgd",""+c);
        //dialog = ProgressDialog.show(ManageLeadActivity.this,"Loading...","Please wait...",false,false);
        for(int i= 1; i<=c; i++ ){
            if(i==1){
                FileName = fname;
                EncodeImage =timestamp;
                a =i;
                ImageMulti();
                //uploadBitmap(timestamp);

            }else if(i==2){
                FileName = fname1;
                EncodeImage =timestamp1;
                a =i;
                ImageMulti();
               // uploadBitmap(timestamp1);
               // a =i;
            }else if(i==3){
                FileName = fname2;
                EncodeImage =timestamp2;
                a =i;
                ImageMulti();
               // uploadBitmap(timestamp2);
                 //a =i;
            }else if(i==4){
                FileName = fname3;

                EncodeImage =timestamp3;
                //uploadBitmap(timestamp3);
                a =i;
                ImageMulti();
            }else if(i==5){
                FileName = fname4;
                EncodeImage =timestamp4;
              //  uploadBitmap(timestamp4);

                a =i;
                ImageMulti();

            }else if(i==6){
                FileName = fname5;
                EncodeImage =timestamp5;
                a =i;
                ImageMulti();
                // uploadBitmap(timestamp2);
                //a =i;
            }else if(i==7){
                FileName = fname6;

                EncodeImage =timestamp6;
                //uploadBitmap(timestamp3);
                a =i;
                ImageMulti();
            }else if(i==8){
                FileName = fname7;
                EncodeImage =timestamp7;
                //  uploadBitmap(timestamp4);

                a =i;
                ImageMulti();

            }
        }
        c=0;

    }


    private void ImageMulti() {
        FirstN = FirstName.getText().toString().trim();
        String fileNameSegments[] = FileName.split("/");
        Names = fileNameSegments[fileNameSegments.length - 1];
        final String abc= Names;
        final String cddd= EncodeImage;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ManageLeadActivity.this);
        String companynURL=preferences.getString("companynURL","");
        String url = companynURL+"/prospect_app_dz/lead_image_dz.php?rep_id=" + rep_id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     Log.e("sfjgf",""+a+c);
                        if(a==c){
                            Intent i1 = new Intent(ManageLeadActivity.this,ManageLeadActivity.class);
                            startActivity(i1);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();

                pars.put("contact_id", contact_id);
                pars.put("filename", abc);
                pars.put("image", cddd);
                Log.e("vinays",""+c);
                Log.e("filename",""+a);
                return pars;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent view) {
        if(getCurrentFocus()!=null){
            InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return super.dispatchTouchEvent(view);
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}










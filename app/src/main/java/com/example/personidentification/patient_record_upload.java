package com.example.personidentification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
<<<<<<< Updated upstream

import org.bytedeco.javacpp.opencv_core;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
=======
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
>>>>>>> Stashed changes
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class patient_record_upload extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE=123;
    private static final int CAMERA_REQUEST = 121 ;

    private static final String TAG = "patient_record_upload";
    String patient_name,contact,medical,prescription,add_info;
    EditText name,phone,medical_history,prescription_taken,additional_info;
    ImageView imageview;
    //ProgressBar pb;
    Button btn_gallery,btn_take_pic,btn_upload;
    String mPath;
    static final int WIDTH = 256;
    static final int HEIGHT = 256;
    public String currentimagepath = null;
<<<<<<< Updated upstream
    CascadeClassifier face_cascade;
    LBPHFaceRecognizer faceRecognizer;
    private opencv_core.MatVector images;
    private Mat labels;
     BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status)  {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.d("OpenCV", "OpenCV loaded successfully");
                    try {
                        InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt2);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt2.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();
=======
>>>>>>> Stashed changes



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_record_upload);

        setAlert();
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        medical_history = findViewById(R.id.medical_history);
        prescription_taken = findViewById(R.id.presription_taken);
        additional_info = findViewById(R.id.additional_info);
        imageview = findViewById(R.id.imageview_pic);
        btn_gallery = findViewById(R.id.button1);
        btn_take_pic = findViewById(R.id.button2);
        btn_upload = (Button) findViewById(R.id.upload);
        // pb=findViewById(R.id.pb);
        //pb.setVisibility(View.GONE);
        final ProgressDialog dialog = new ProgressDialog(patient_record_upload.this);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        final PyObject pyobj = py.getModule("Algorithm");   // here give name of python file

        mPath = Environment.getExternalStorageDirectory() + "/PersonIdentifier/";
        Log.d("Path", mPath);
        File f = new File(mPath);
        if (!f.exists()) {
            f.mkdir();
        }


        File dir = new File("/storage/emulated/0/PersonIdentifier/");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }


        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "pick an image"), GALLERY_REQUEST_CODE);
            }
        });

        btn_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = getImagefile();
                    } catch (IOException ex) {

                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(patient_record_upload.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    }
                }
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete previous patient name folders
                File dir = new File("/storage/emulated/0/PersonIdentifier/");
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(dir, children[i]).delete();
                    }
                }


                patient_name = name.getText().toString();
                contact = phone.getText().toString();
                medical = medical_history.getText().toString();
                prescription = prescription_taken.getText().toString();
                add_info = additional_info.getText().toString();

                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(patient_record_upload.this, "name is mandatory", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                } else {
                    if (phone.getText().toString().length() == 10) {
                        if (imageview.getDrawable() == null) {
                            Toast.makeText(patient_record_upload.this, "patient image required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (medical_history.getText().toString().isEmpty() || prescription_taken.getText().toString().isEmpty()) {
                            Toast.makeText(patient_record_upload.this, "provide medical records and prescription details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (add_info.isEmpty()) {
                            add_info = "empty";
                        }

                        File pat_fold = new File(mPath + "/" + patient_name);
                        if (!pat_fold.exists()) {
                            pat_fold.mkdir();
                        }

                        BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        try {
                            File file = new File(mPath + "/" + patient_name + "/image.jpg");
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        //pb.setVisibility(View.VISIBLE);
                        dialog.show(patient_record_upload.this, "", "Please wait...Uploading", true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PyObject obj = pyobj.callAttr("Algorithm", mPath,
                                        patient_name,
                                        contact,
                                        medical,
                                        prescription,
                                        add_info);
                                name.setText("");
                                phone.setText("");
                                medical_history.setText("");
                                prescription_taken.setText("");
                                additional_info.setText("");
                                Log.d(TAG, obj.toString());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        Intent myintent = new Intent(patient_record_upload.this, MainActivity.class);
                                        //myintent.putExtra("translate", str);
                                        startActivity(myintent);
                                        Toast.makeText(patient_record_upload.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();


                    } else {
                        Toast.makeText(patient_record_upload.this, "not a valid number", Toast.LENGTH_SHORT).show();
                        phone.requestFocus();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            Uri imagedata = data.getData();
            imageview.setImageURI(imagedata);
        }

        if(requestCode==CAMERA_REQUEST){
            int targetW = imageview.getWidth();
            int targetH = imageview.getHeight();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentimagepath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(currentimagepath, bmOptions);
            imageview.setImageBitmap(bitmap);
        }
    }

    private File getImagefile() throws IOException {
        String imageFileName =  "Image";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentimagepath = image.getAbsolutePath();
        return image;
    }


    private void setAlert() {
        AlertDialog.Builder al = new AlertDialog.Builder(patient_record_upload.this);
        al.setTitle("Alert");
        al.setMessage("Make sure you only upload good quality image");
        al.setCancelable(true);
        al.setIcon(R.drawable.ic_launcher_background);
        al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  Toast.makeText(getApplicationContext(), "You clicked on YES :   ", Toast.LENGTH_SHORT).show();
            }
        });
        al.show();
    }


<<<<<<< Updated upstream

    public void load_cascade(){
        try {
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt2);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt2.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            face_cascade = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            if(face_cascade.empty())
            {
                Log.d("MyActivity","--(!)Error loading A\n");
                return;
            }
            else
            {
                Log.d("MyActivity",
                        "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MyActivity", "Failed to load cascade. Exception thrown: " + e);
        }
    }


    public void upload(View view) throws IOException {

        if(name.getText().toString().isEmpty()){
            Toast.makeText(patient_record_upload.this, "name is mandatory", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }
        else
        if (phone.getText().toString().length() == 0) {
            if(imageview.getDrawable()==null){
                Toast.makeText(this, "patient image required", Toast.LENGTH_SHORT).show();
                return;
            }
            if(medical_history.getText().toString().isEmpty() || prescription_taken.getText().toString().isEmpty()){
                Toast.makeText(this, "provide medical records and prescription details", Toast.LENGTH_SHORT).show();
                return;
            }

            // here comes identification code and upload details with image lbp values to firefox

            BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            Mat mRGBA = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap, mRGBA);

            Imgproc.cvtColor(mRGBA, mRGBA, Imgproc.COLOR_RGB2GRAY);
            Mat m;
            MatOfRect faceDetections = new MatOfRect();
            face_cascade.detectMultiScale(mRGBA, faceDetections);
            final Rect[] facesArray = faceDetections.toArray();
            if(facesArray.length == 0){
                Toast.makeText(this,"no face detected",Toast.LENGTH_LONG).show();
            }else{

            Rect r = facesArray[0];
          //  r.height = (int) ((r.height * 1.2)+2);
          //  r.width = (int) ((r.width * 1.3)+1);
            m = mRGBA.submat(r);
            Bitmap bmp = Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bmp);
            bmp = Bitmap.createScaledBitmap(bmp, WIDTH, HEIGHT, false);

                //delete the previous pic in faceRec fodler
                File dir = new File("/storage/emulated/0/PersonIdentifier/");
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                }
            FileOutputStream f;
                f = new FileOutputStream(mPath + "image" + ".jpg", true);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, f);
                f.close();

                //face identification lbp code here
                faceRecognizer= LBPHFaceRecognizer.create();

            Toast.makeText(patient_record_upload.this, "patient records uploaded successfully", Toast.LENGTH_SHORT).show();}
        } else {
            Toast.makeText(patient_record_upload.this, "not a valid number", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return;
        }



    }


=======
>>>>>>> Stashed changes
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_press,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.back) {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
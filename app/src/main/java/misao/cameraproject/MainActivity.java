package misao.cameraproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //send the code when click the button for image click
    private static final int REQUEST_CAPTURE_CODE = 100;

    //this is use when check the media tyee is image or not
    private static final int MEDIA_TYPE_IMAGE = 1;

    //this is directory name where the capture image is save
    private static final String IMAGE_DIRECTORY_NAME = "Hello";

    //is the uri to store the image uri
    private Uri fileUri;

    //this is instance of imageView where display the image after capture
    ImageView img;

    //it's button for click the image
    Button btn_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView)findViewById(R.id.imgView);
        btn_img = (Button)findViewById(R.id.btn_Img);

        btn_img.setOnClickListener(this);

        //this function is check your camera available or not
        if(!isDeviceSupportCamera())
        {
            Toast.makeText(getApplicationContext(),"Sorry! your device doesn't support camera",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //this is check your device is support the camera or not
    private boolean isDeviceSupportCamera() {
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //when i click the button then this function is run
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(intent, REQUEST_CAPTURE_CODE);
    }

    //this is get the image and convert in uri
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    //this is get the image from external storage directory
    private File getOutputMediaFile(int type) {
        //External Sd card location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

        //it's create the directory if your directory is not available
        if(!mediaStorageDir.exists())
        {
            if(!mediaStorageDir.mkdirs())
            {
                Log.d(IMAGE_DIRECTORY_NAME,"oops..Failed Create"+IMAGE_DIRECTORY_NAME+"directory");
                return null;
            }
        }

        //Create a file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;

        if(type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath()+ File.separator+"IMG_"+timeStamp+".jpg");
        }
        else
        {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CAPTURE_CODE && resultCode == RESULT_OK)
        {
            previewCaptureImage();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Sorry for capture the image",Toast.LENGTH_LONG).show();
        }
    }

    private void previewCaptureImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();

        //down size the image as it throws outOfMemory Exception for larger image

        options.inSampleSize = 8;

        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
        img.setImageBitmap(bitmap);
    }
}

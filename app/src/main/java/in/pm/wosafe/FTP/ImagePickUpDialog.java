package in.pm.wosafe.FTP;


import android.os.Bundle;
import in.pm.wosafe.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImagePickUpDialog extends Dialog {
    private Activity mContext;
    private static final int CAMERA_PIC_REQUEST = 2;
    private int OPEN_GALLERY_REQUEST = 1;

    public ImagePickUpDialog(Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_pick_up_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ImageView imgPhotofromCamera = (ImageView) findViewById(R.id.custom_gallery_addform_camera);
        ImageView imgPhotofromGallary = (ImageView) findViewById(R.id.custom_gallery_addform_gallary);
        TextView txtAddfromCamera = (TextView) findViewById(R.id.addfromcamera);
        TextView txtAddfromgallary = (TextView) findViewById(R.id.addfromgallary);
        ImageView btncloseDialog = (ImageView) findViewById(R.id.custom_gallery_close);
        TextView txtCloseDialog = (TextView) findViewById(R.id.custom_gallery_tv_close);

        imgPhotofromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                dismiss();
            }
        });
        imgPhotofromGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                // intent.setAction(Intent.ACTION_GET_CONTENT);

                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                mContext.startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        OPEN_GALLERY_REQUEST);
                dismiss();

            }
        });
        txtAddfromgallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//               intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                mContext.startActivityForResult(
                        Intent.createChooser(intent, "Select File"), OPEN_GALLERY_REQUEST);
                dismiss();

            }
        });
        txtAddfromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                dismiss();
            }
        });
        btncloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Choose camera or gallery to select image",Toast.LENGTH_LONG).show();
            }
        });

        txtCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Choose camera or gallery to select image",Toast.LENGTH_LONG).show();
            }
        });
//        show();
    }

    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.

        PackageManager packageManager = mContext.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(mContext, "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
                strAbsolutePath = photoFile.getAbsolutePath();
                Log.e("strAbsolutePath", strAbsolutePath);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("eeeeee", "dispatchTakePictureIntent: "+ex);
                Toast toast = Toast.makeText(mContext, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        fileUri);
                mContext.startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    public static String strAbsolutePath;

    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        strAbsolutePath = image.getAbsolutePath();
//        Log.e("Absolute Path", image.getAbsolutePath());
        return image;
    }
}
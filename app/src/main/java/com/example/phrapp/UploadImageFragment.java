package com.example.phrapp;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadImageFragment extends Fragment {

    // Media Upload permissions
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int CAMERA_REQUEST = 3;
    Activity activity;
    static String NERres="";
    public static String getNERres(){
        return NERres;
    }
    public static String[] permissions_old = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = permissions_33;
        } else {
            p = permissions_old;
        }
        return p;
    }

//    private ImageView imageView;
    private TextView resultText;
    //

    ImageView imageView;
    View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseStorage storage;
    StorageReference storageReference;
    public UploadImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadImageFragment newInstance(String param1, String param2) {
        UploadImageFragment fragment = new UploadImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_image, container, false);
        // Inflate the layout for this fragment
        imageView = view.findViewById(R.id.imageView);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Button button = view.findViewById(R.id.button);
        Button upload_button = view.findViewById(R.id.button2);
        resultText = view.findViewById(R.id.result_text);

        // Upload code
        activity = getActivity();

        assert activity != null;
        ActivityCompat.requestPermissions(activity,permissions(),PERMISSION_REQUEST_CODE);


        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("REQKARAN", "Upload Activity Started");

//                System.out.println("Upload Activity Started");
                openFileChooser();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Camera Activity Started");
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_camera, CAMERA_REQUEST);


            }
        });
        return view;
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST)
        {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri imageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
                    imageView.setImageBitmap(bitmap);

                    String base64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
                    Log.d("REQKARAN", "Image encoded");
                    sendImage(base64Image);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == CAMERA_REQUEST) {
            System.out.println("In Activity Result");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), photo, "Title", null);
            Uri uri = Uri.parse(path);


            imageView.setImageBitmap(photo);
            //  uri  =  data.getData();
            System.out.println("Uri of image is : " + uri);
            System.out.println("Image data " + photo);
            Document img = new Document();
            img.setImage(uri.toString());
            System.out.println("Logged IN User " + FirebaseAuth.getInstance().getCurrentUser().getUid());
            // img.setLoggedInUser("User"+FirebaseAuth.getInstance().getCurrentUser().getUid());
            System.out.println("Image got clicked");
            AppCompatActivity act = (AppCompatActivity) view.getContext();
            ImageDetailsFragment detailViewFragment = new ImageDetailsFragment();

            if( activity instanceof User_Logged_in_via_abha_HomePage){
                act.getSupportFragmentManager().beginTransaction().replace(R.id.user_logged_in_via_abha_homepage_fragment_frame, detailViewFragment).addToBackStack(null).commit();
            }
            else{
                act.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, detailViewFragment).addToBackStack(null).commit();
            }


            Bundle bundle = new Bundle();
            bundle.putSerializable("imageInfoObj", (Serializable) img);
            detailViewFragment.setArguments(bundle);
        }
    }
    private void sendImage(String base64Image) {
        OkHttpClient client = new OkHttpClient();
        Log.d("REQKARAN", "Client Created");

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, "{\"image\": \"" + base64Image + "\"}");
        Request request = new Request.Builder()
//                .url("http://10.0.2.2:8000/ocr")
//                .url("http://192.168.41.244:8000/ocr")
                .url("http://192.168.3.74:8000/ocr")
                .post(requestBody)
                .build();
        Log.d("REQKARAN", "Request Sent");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("REQKARAN", "WHO");

                if(result == ""){
                    Log.d("REQKARAN", "No text found");
                }
                else{
                    Log.d("REQKARAN", result);
                }
//                Log.d("REQKARAN1", result);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                                Log.d("REQKARAN", String.valueOf(result));
                        resultText.setText(result);
                        NERres=result;
                    }
                });
            }
        });
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        byte[] byteArray = byteArrayOS.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
package com.example.tugas3_2018031;

import static android.app.Activity.RESULT_OK;
import static com.example.tugas3_2018031.DBmain.TABLENAME;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tugas3_2018031.databinding.FragmentAlarmBinding;
import com.example.tugas3_2018031.databinding.FragmentInsertBinding;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentInsertBinding binding;

    DBmain dBmain;
    SQLiteDatabase sqLiteDatabase;
    int id = 0;
    public static final int MY_CAMERA_REQUEST_CODE = 100;
    public static final int MY_STORAGE_REQUEST_CODE = 101;
    String cameraPermission[];
    String storagePermission[];

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InsertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InsertFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InsertFragment newInstance(String param1, String param2) {
        InsertFragment fragment = new InsertFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInsertBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dBmain = new DBmain(getActivity().getApplicationContext());
        //findid();
        insertData();
        editData();
        binding.etImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int avatar = 0;
                if (avatar == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (avatar == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });

        return view;
    }


    private void editData() {
        if (getActivity().getIntent().getBundleExtra("userdata") != null) {
            Bundle bundle = getActivity().getIntent().getBundleExtra("userdata");
            id = bundle.getInt("id");
//for set name
            binding.etTitle.setText(bundle.getString("title"));
            binding.etDescription.setText(bundle.getString("description"));
//for image
            byte[] bytes = bundle.getByteArray("avatar");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.etImage.setImageBitmap(bitmap);
//visible edit button and hide submit button
            binding.btPosting.setVisibility(View.GONE);
            binding.btEDit.setVisibility(View.VISIBLE);
        }
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, MY_STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(getContext(), this);
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, MY_CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void insertData() {
        binding.btPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("title", binding.etTitle.getText().toString());
                cv.put("description", binding.etDescription.getText().toString());
                cv.put("avatar", imageViewToBy(binding.etImage));

                sqLiteDatabase = dBmain.getWritableDatabase();
                Long rec = sqLiteDatabase.insert("post", null, cv);
                if (rec != null) {
                    Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show(); binding.etTitle.setText("");
                    binding.etImage.setImageResource(R.mipmap.ic_launcher);
                    binding.etTitle.setText("");
                    binding.etDescription.setText("");
                } else {
                    Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
//for view display
        binding.btDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DisplayData.class));
            }
        });
//for storing new data or update data
        binding.btEDit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("title", binding.etTitle.getText().toString());
                cv.put("description", binding.etDescription.getText().toString());
                cv.put("avatar", imageViewToBy(binding.etImage));
                sqLiteDatabase = dBmain.getWritableDatabase();
                long recedit = sqLiteDatabase.update(TABLENAME, cv, "id=" + id, null);
                if (recedit != -1) {
                    Toast.makeText(getActivity(), "Update Succesfully", Toast.LENGTH_SHORT).show();
//clear data adfte submit
                            binding.etTitle.setText("");
                            binding.etDescription.setText("");
                    binding.etImage.setImageResource(R.mipmap.ic_launcher);
//edit hide and submit visible
                    binding.btEDit.setVisibility(View.GONE);
                    binding.btPosting.setVisibility(View.VISIBLE);
                    Intent a = new Intent(getActivity(), DisplayData.class);
                    startActivity(a);
                }
            }
        });
    }

    public static byte[] imageViewToBy(ImageView avatar) {
        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage_accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && storage_accepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            } break;
            case MY_STORAGE_REQUEST_CODE: {
                boolean storage_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storage_accepted) {
                    pickFromGallery();
                } else {
                    Toast.makeText(getActivity(), "please enable storage permission", Toast.LENGTH_SHORT).show();
                }
            } break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get().load(resultUri).into(binding.etImage);
                Log.e("resultUri ->", String.valueOf(resultUri));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("error ->", String.valueOf(error));
            }
        }
    }
}
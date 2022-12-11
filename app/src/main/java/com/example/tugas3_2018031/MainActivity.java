package com.example.tugas3_2018031;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;

import com.example.tugas3_2018031.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    RecyclerView recylerView;
    private ActivityMainBinding binding;

    String[] s1, s2, s3;
    int[] images = {R.drawable.bahan1, R.drawable.bahan2, R.drawable.bahan3, R.drawable.bahan4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recylerView = findViewById(R.id.rvDiary);
        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().enqueueUniqueWork("Notifikasi", ExistingWorkPolicy.REPLACE, request);
            }
        });
        s1 = getResources().getStringArray(R.array.judul);
        s2 = getResources().getStringArray(R.array.deskripsi);
        s3 = getResources().getStringArray(R.array.star);
        DiaryAdapter appAdapter = new DiaryAdapter(s1, s2, s3, images, this);
        recylerView.setAdapter(appAdapter);
        recylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }
}
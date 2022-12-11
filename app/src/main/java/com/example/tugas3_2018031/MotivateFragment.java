package com.example.tugas3_2018031;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tugas3_2018031.databinding.FragmentInsertBinding;
import com.example.tugas3_2018031.databinding.FragmentMotiveBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MotivateFragment extends Fragment implements View.OnClickListener {
    private FragmentMotiveBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle
                                     savedInstanceState) {
        binding = FragmentMotiveBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.btRandom.setOnClickListener(this);
        try {
            getData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        binding.tvMotivate.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btRandom) {
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://api.quotable.io/random")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }

    class DOTask extends AsyncTask<URL, Void, String> {
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //get data json
        public void parseJson(String data) throws JSONException {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String content = jsonObject.getString("content");
            String author = "- " + jsonObject.getString("author");
            binding.tvMotivate.setText(content);
            binding.tvAuthor.setText(author);
        }
    }
}

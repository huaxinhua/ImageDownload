package com.mmm.imagedownload;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageDetailsActivity extends BaseActivity {

    @Bind(R.id.photo_view)
    PhotoView photoView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        photoView.setImageURI(Uri.parse(url));

    }


    public static void skip(Activity activity, String url) {
        Intent intent = new Intent(activity, ImageDetailsActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }
}

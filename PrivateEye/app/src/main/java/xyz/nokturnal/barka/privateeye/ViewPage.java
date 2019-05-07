package xyz.nokturnal.barka.privateeye;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class ViewPage extends AppCompatActivity {

    TextView tvName, tvTime, tvDate;
    //ImageView userImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        tvName = findViewById(R.id.tvUName);
        tvTime = findViewById(R.id.tvFTime);
        tvDate = findViewById(R.id.tvFDate);

        tvName.setText(getIntent().getStringExtra("username"));
        tvDate.setText(getIntent().getStringExtra("date"));
        tvTime.setText(getIntent().getStringExtra("time"));


        new DropImage((ImageView)findViewById(R.id.imgFromBucket))
        .execute(getIntent().getStringExtra("link"));
    }

    private class DropImage extends AsyncTask<String, Void, Bitmap>
    {

        ImageView imgView;

        public DropImage(ImageView imgView)
        {
            this.imgView = imgView;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String imgUrl = url[0];
            Bitmap bitmap = null;

            try {
                InputStream in = new URL(imgUrl).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            }
            catch (Exception ex)
            {
                Log.e("Netwark", ex.toString());
                ex.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView.setImageBitmap(bitmap);
        }
    }
}

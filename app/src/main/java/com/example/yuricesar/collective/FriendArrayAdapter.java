package com.example.yuricesar.collective;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuricesar.collective.data.UserInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ygorg_000 on 04/08/2015.
 */
public class FriendArrayAdapter extends ArrayAdapter<UserInfo> {

    private TextView textView;
    private ImageView imageView;
    private List<UserInfo> userList = new ArrayList<UserInfo>();
    private LinearLayout layout;

    public FriendArrayAdapter(Context context, int resource, List<UserInfo> objects) {
        super(context, resource, objects);
    }

    public void add(UserInfo user) {
        userList.add(user);
        super.add(user);
    }

    public int getCount(){
        return this.userList.size();
    }

    public UserInfo getItem(int index){
        return this.userList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v==null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friend_element, parent, false);
        }
        layout = (LinearLayout)v.findViewById(R.id.List);
        UserInfo userObj = getItem(position);
        Log.d("Usuario", userObj.getName());
        imageView = (ImageView)v.findViewById(R.id.imageView);
        new DownloadImageTask(imageView).execute(userObj.getURLPicture());
        textView = (TextView)v.findViewById(R.id.textView);
        textView.setText(userObj.getName());
        return v;
    }

    public Bitmap decodeToBitmap(byte[] decodeByte){
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Bitmap circleBitmap = Bitmap.createBitmap(result.getWidth(), result.getHeight(), Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader (result,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(result.getWidth() / 2, result.getHeight() / 2, result.getWidth() / 2, paint);
            bmImage.setImageBitmap(circleBitmap);
        }
    }
}

package com.example.yuricesar.collective;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yuricesar.collective.data.CelulaREST;
import com.example.yuricesar.collective.data.DataBaseHelper;
import com.example.yuricesar.collective.data.UserInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mlocationRequest;
    private CelulaREST celulaREST;
    private UserInfo user;

    //ATRIBUTOS RECOMENDAÇÃO
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    @InjectView(R.id.frame)
    SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        Bundle extras = getIntent().getExtras();
        String id = (String) extras.get("ID");
        String nome = (String)extras.get("Nome");
        String picture = (String)extras.get("Picture");
        String email = (String)extras.get("Email");

            //user = DataBaseHelper.getInstance(this).getUser(id);

        // populate the navigation drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        mNavigationDrawerFragment.setUserData(id, nome, email, picture);

        callConecton();
        initLocationRequest();

        //RECOMENDAÇÃO
        al = new ArrayList<>();
        al.add("Felipe");
        al.add("Diego");
        al.add("Franklin");
        al.add("Ygor");
        al.add("Yuri");

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.user_name, al);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(MainActivity.this, "Esquerda!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(MainActivity.this, "Direita!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("LES ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(MainActivity.this, "Clicado!");
            }
        });
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }

    //TODO fazer isso sempre estar executando
    private void acharPessoasProximas() {
        UserInfo u = null;
        try {
            u = (UserInfo) celulaREST.userProximos(user).get(0);
            while (u != null) {
                notifyPeopleAround(u.getName(), media((List<Object>) celulaREST.userProximos(user).get(1)));
                u = (UserInfo) celulaREST.userProximos(user).get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double media (List<Object> list) {
        double soma = 0;
        for (int i = 0; i < list.size(); i++) {
            soma += (Double) list.get(i);
        }
        return soma/list.size();
    }

    private void notifyPeopleAround(String name, double interestesLevel) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_template_icon_bg) //TODO colocar o icon do collective
                        .setContentTitle("Pessoa próxima de você : "+name)
                        .setContentText("Nível de coisas em comum : "+interestesLevel+"%");
        mBuilder.setProgress(100, Integer.parseInt(String.valueOf(interestesLevel)) , false);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, LoginActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LoginActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(001, mBuilder.build());
    }

    //TODO fazer isso sempre estar executando
    private void receberMsg() {
        try {
            List<String> result = celulaREST.receberMsg(user);
            String msg = result.get(1);
            if (!msg.equals("")) {
                notifyMessages();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        receberMsg();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void notifyMessages() {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_template_icon_bg) //TODO colocar o icon do collective
                        .setContentTitle("Mensagens não lidas")
                        .setContentText("clique aqui");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, LoginActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LoginActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        //TODO naum testado, naum sei se precisa de um loop
        int notifyID = 1;
        int numMessages = 0;
        mBuilder.setContentText(numMessages + "mensagens não lidas")
                .setNumber(++numMessages);
        mNotificationManager.notify(
                notifyID,
                mBuilder.build());

        // mId allows you to update the notification later on.
        mNotificationManager.notify(001, mBuilder.build());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if( id == R.id.action_message) {
            friendList();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {

        Log.i("LOG", "onConnected(" + bundle + ")");
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(l != null) {
            try {
                celulaREST.atualizaLocalizacao(user.getId(), l.getLatitude(), l.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("LOG", "Latitude: " + l.getLatitude());
            Log.i("LOG", "Longitude: " + l.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            celulaREST.atualizaLocalizacao(user.getId(), location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public synchronized void callConecton() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    private void initLocationRequest(){

        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(5000);
        mlocationRequest.setFastestInterval(2000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void friendList(){
        Bundle extras = getIntent().getExtras();
        String userId = (String) extras.get("ID");
        Intent i = new Intent();
        i.setClass(this, FriendsListActivity.class);
        i.putExtra("UserId", userId);
        try {
            startActivity(i);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
            bmImage.setImageBitmap(result);
        }
    }

    public ImageView imagem(UserInfo user) {
        ImageView image = new ImageView(this);
        new DownloadImageTask(image).execute(user.getURLPicture());
        return image;
    }
}

package com.fit2081.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

import java.util.Random;


public class MainActivity extends AppCompatActivity{
    public static final String TAG = "BOOK";

    Button getAddbook_btn, getClear_fields_btn, getLoadBook_btn;
    EditText getBookID, getTitle, getAuthor, getISBN, getDescription, getPrice;
    TextView getRating;
//    SharedPreferences sp = getSharedPreferences("MatchName", MODE_PRIVATE);

    public static final String MY_PREF_FILENAME = "com.fit2081.bookstoreapp.data";
    private String Title;
    public static String BOOK_KEY= "book_key";
    long lastClickTime = 0;


    ArrayList<String> dataSource;
    ArrayList<Item> data = new ArrayList<>();
    ListView listview;
    ArrayAdapter adapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerAdapter myRecyclerAdapter;

    private BookViewModel bookViewModel;
    public static int counter = 1;
    DatabaseReference myRef;
    View myView;
    private GestureDetectorCompat myDetector;
    private ScaleGestureDetector myScaleDetector;
    TextView rateCount, showRating;
    RatingBar ratingBar;
    float rateValue;
    String temp;

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase();

    public static final String digits = "0123456789";

    public static final String alphaNummeric = upper + lower + digits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.drawer_layout); // made my own layout, replace default

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar); // removed the default toolbar from setContentView, and now we have set the toolbar with title and triple dots

//        Link the toolbar with the action bar (hamburger 3 lines)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav); // need to go to folder Values > Strings.xlm to create 'open_nav' and 'close_nav'
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        Comment out because buttons were removed
//        getAddbook_btn = findViewById(R.id.btnAddbook);
//        getClear_fields_btn = findViewById(R.id.btnClear_fields);
//        getLoadBook_btn = findViewById(R.id.loadBook_btn);
        getBookID = findViewById(R.id.bookID_input);
        getTitle = findViewById(R.id.title_input);
        getAuthor = findViewById(R.id.author_input);
        getISBN = findViewById(R.id.ISBN_input);
        getDescription = findViewById(R.id.description_input);
        getPrice = findViewById(R.id.price_input);
        getRating = findViewById(R.id.txtCount);

        dataSource = new ArrayList();
        listview = findViewById(R.id.listViewID);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataSource); // (1) context - get information about my application, (2) using predefined layout(eg themes, formatting, UI placements), (3) dataSource
        listview.setAdapter(adapter);

        navigationView.setNavigationItemSelectedListener(new navigationHandler());
        adapter.notifyDataSetChanged();

        myView = findViewById(R.id.myView);
//        -------- Week 5: FAB Button --------
        fabOnClick();

//        -------- WEEK 6: Recycler View --------
        myRecyclerView();

//        -------- Week 7: Relocated to FAB --------
//        addBook()
//        addItem()

//       -------- Week 8: Firebase --------
        firebase();

//        -------- Week 10: Touch Event Gesture --------
//        touchEvent();

//       -------- Week 11: Gesture Detectors --------
        myDetector = new GestureDetectorCompat(this, new Wk11GestureDector());
        myView = findViewById(R.id.myView);
        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                myDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

//      Week12: Create a rating review
        rateCount = findViewById(R.id.txtCount);
        ratingBar = findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateValue = ratingBar.getRating();
                if (rateValue <= 1 && rateValue >0 ){
                    rateCount.setText("Bad - " + rateValue + "/5");
                }
                else if (rateValue <= 2 && rateValue >1){
                    rateCount.setText("Not Good - " + rateValue + "/5");
                }
                else if (rateValue <= 3 && rateValue >2){
                    rateCount.setText("Average - " + rateValue + "/5");
                }
                else if (rateValue <= 4 && rateValue >3){
                    rateCount.setText("Good - " + rateValue + "/5");
                }
                else if (rateValue <= 5 && rateValue >4){
                    rateCount.setText("Very Good - " + rateValue + "/5");
                }
                else{
                    rateCount.setText("");
                }

            }
        });

    }
    class Wk11GestureDector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 10;
        private static final int SWIPE_VELOCITY_THRESHOLD = 10;
        static final int SWIPE_THRESHOLD2 = 10;
        private static final int SWIPE_VELOCITY_THRESHOLD2 = 1000;
        @Override
        public boolean onDown(@NonNull MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(@NonNull MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
            getISBN.setText(generateNewRandomString(6));
            Log.d("myTAG", "Single Tap - New ISBN");
            return true;
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float distanceX, float distanceY) {

            if (Math.abs(distanceX) > SWIPE_THRESHOLD && Math.abs(distanceY) < SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) {
                    // Scroll right
                    Log.d("myTAG", "Right to left - Decrease Price");
                    decPrice();
                } else {
                    // Scroll left
                    Log.d("myTAG", "Left to right - Increase Price");
                    incPrice();
                }
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent motionEvent) {
            loadBook();
            Log.d("myTAG", "Long Press - Load Saved Book");


        }

        @Override
        public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float velocityX, float velocityY) {
            float deltaX = motionEvent.getX() - motionEvent1.getX();

            if (Math.abs(deltaX) > SWIPE_THRESHOLD2 && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD2) {
                moveTaskToBack(true);
                return true;
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            clearFields();
            Log.d("myTAG", "Double Tap - Clear Fields");
            return true;
        }
    }

    public static String generateNewRandomString(int length) {
        char[] buf;
        Random random=new Random();
        if (length < 1) throw new IllegalArgumentException();
        buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = alphaNummeric.charAt(random.nextInt(alphaNummeric.length()));
        return new String(buf);
    }



//    public void listAllBooks(){
//
//        Gson gson = new Gson ();
//        String movieDataStr = gson.toJson(data);
//
//        Intent intent = new Intent (this, MainActivity2.class);
//        intent.putExtra(BOOK_KEY,movieDataStr);
//        startActivity(intent);
//
//    }

    public void touchEvent(){
//        -------- Week 10: Touch Event Gesture --------
        myView.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // initialise X,Y as you press down on the View
                        startX = event.getX();
                        startY = event.getY();
                        long currentClickTime = System.currentTimeMillis();
                        if(lastClickTime == 0){
                            lastClickTime = currentClickTime;
                        }
                        else{
                            if(currentClickTime - lastClickTime <= 300){
                                decPrice();
                                Toast.makeText(MainActivity.this, "Double Click - Decrement Price", Toast.LENGTH_SHORT).show();
                            }
                            lastClickTime = 0;
                        }
//                        return true;
                    case MotionEvent.ACTION_UP: // ending point - when you lift up your mouse
                        float endX = event.getX();
                        float finalX = endX - startX;
                        float endY = event.getY();
                        float finalY = endY - startY;

                        if (Math.abs(finalX) > Math.abs(finalY)) {
                            // Horizontal swipe
                            if (finalX > 0) {
                                // Right swipe - Add one dollar to price
                                incPrice();
                                Toast.makeText(MainActivity.this, "Swipe Right - Increase Price Complete", Toast.LENGTH_SHORT).show();
                            } else {
                                // Left swipe - Add book
                                addCard();
                                Toast.makeText(MainActivity.this, "Swipe Left - Add book Completed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // swipe up - Clear Field
                            if (finalY < 0) {
//                            // Upwards swipe
                                clearFields();
                                Toast.makeText(MainActivity.this, "Swipe Up - Clear Fields Completed", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // Downwards swipe
//                                Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();
                            }
                        }

                        return true;

                }
                return false;
            }

        });
    }
    public void myRecyclerView(){
//        -------- WEEK 6: Recycler View --------
        recyclerView = findViewById(R.id.recViewID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRecyclerAdapter = new RecyclerAdapter();
        myRecyclerAdapter.setData(data);
        recyclerView.setAdapter(myRecyclerAdapter);

    }

    public void firebase(){
//        -------- Week 8: Firebase --------
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("books");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                dataSource.add(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(dataSource.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addBook(){
        String priceText = (getPrice.getText().toString());
        Double finalPrice = Double.parseDouble(priceText);
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        Books book = new Books(getTitle.getText().toString(), getAuthor.getText().toString(), getISBN.getText().toString(), getDescription.getText().toString(),(getPrice.getText().toString()));
        BookViewModel.insert(book);
    }

    public void addItem() {

//        Item book = new Item(getBookID.getText().toString() ,getTitle.getText().toString(), getAuthor.getText().toString(), getISBN.getText().toString(), getDescription.getText().toString(),getPrice.getText().toString());
        Item book = new Item(counter + "", getTitle.getText().toString(), getAuthor.getText().toString(), getISBN.getText().toString(), getDescription.getText().toString(),getPrice.getText().toString(), getRating.getText().toString());
        data.add(book);
        myRecyclerAdapter.notifyDataSetChanged();
    }

//    Navigation Menu Bar
    class navigationHandler implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int id = item.getItemId();
            if (id == R.id.nav_menu_add_book) {
                getText();

            }
            else if (id == R.id.nav_menu_remove_last_book) {
                int index = dataSource.size() - 1;
                dataSource.remove(index);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Last Book Removed", Toast.LENGTH_LONG).show();

            }
            else if (id == R.id.nav_menu_remove_all_books) {
                dataSource.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"ListView Cleared", Toast.LENGTH_LONG).show();

            }
            else if (id == R.id.nav_num_of_items_in_list) {
                int numList = dataSource.size();
                Toast.makeText(MainActivity.this,"List Size: " + numList, Toast.LENGTH_LONG).show();

            }
            drawerLayout.closeDrawers();


            return true;

        }
//            int id = item.getItemId();
//            switch (id){
//                case R.id.nav_menu_add_book:
//                    break;
//                case R.id.nav_menu_remove_last_book:
//                    break;
//                case R.id.nav_menu_remove_all_books:
//                    Toast.makeText(MainActivity.this, "Test pass", Toast.LENGTH_LONG).show();
//                    adapter.clear();
//
//                    break;
//            }
//            drawerLayout.closeDrawers();
//            return true;
//        }
    }

//    Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.nav_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.options_menu_clear_fields) {
            clearFields();
        }
        else if (id == R.id.options_menu_load_data) {
            loadBook();
        }
        else if (id == R.id.options_remove_last_book) {
            int index = dataSource.size() - 1;
            dataSource.remove(index);
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,"Last Book Removed", Toast.LENGTH_LONG).show();

        }
        else if (id == R.id.options_remove_all_books) {
            dataSource.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,"ListView Cleared", Toast.LENGTH_LONG).show();
            bookViewModel.deleteAll();
            clearData();
        }
        else if (id == R.id.options_num_of_items_in_list) {
            int numList = dataSource.size();
            Toast.makeText(MainActivity.this,"List Size: " + numList, Toast.LENGTH_LONG).show();

        }
        else if (id == R.id.options_menu_view_all_books) {
//            handleBtn1();
//            listAllBooks();
        }
            return true;
    }

    public void clearData(){
        FirebaseDatabase.getInstance().getReference().setValue(null);
    }

    public void fabOnClick(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCard();
            }

        });
    }
    public void incPrice(){
        // increase cost by a dollar
        String price =  getPrice.getText().toString();
        float incPrice= Float.parseFloat(price);
        incPrice ++;
        String newPrice = String.valueOf(incPrice);

        getPrice.setText(newPrice);
    }
    public void decPrice(){
        // increase cost by a dollar
        String price =  getPrice.getText().toString();
        float decPrice= Float.parseFloat(price);
        decPrice --;
        String newPrice = String.valueOf(decPrice);

        getPrice.setText(newPrice);
    }
    public void addCard(){
        addItem();
        counter ++;
        addBook();
//                handleBtn1();
        Books book = new Books(getTitle.getText().toString(), getAuthor.getText().toString(), getISBN.getText().toString(), getDescription.getText().toString(),(getPrice.getText().toString()));
        myRef.push().setValue(book);
    }
//    public void handleBtn1() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frag1, new Fragment1()).addToBackStack("f1").commit();
//    }

        public void clearFields() {
        getBookID.getText().clear();
        getTitle.getText().clear();
        getAuthor.getText().clear();
        getISBN.getText().clear();
        getDescription.getText().clear();
        getPrice.getText().clear();
    }
    public void loadBook(){
        getBookID.setText("book");
        getTitle.setText("title");
        getAuthor.setText("author");
        getISBN.setText("isbn");
        getDescription.setText("des");
        getPrice.setText("100");
    }

   public void getText(){
        String printGetBookTitle = getTitle.getText().toString();
        String printGetPrice = getPrice.getText().toString();
        dataSource.add(printGetBookTitle + " | " + printGetPrice);
        adapter.notifyDataSetChanged();//  EVERY TIME you update the datasource, need to trigger the adapter - "Hey, I made a new update, check it out Adapter!"
       //add data to the listview
    }
















//    -----------------------------------------
        public void sms(){
        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        /* Create and instantiate the local broadcast receiver
           This class listens to messages come from class SMSReceiver
         */
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

    }
    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            StringTokenizer sT = new StringTokenizer(msg, "|");
            String bookIDToken = sT.nextToken();
            String titleToken = sT.nextToken();
            String authorToken = sT.nextToken();
            String ISBNToken = sT.nextToken();
            String descriptionToken = sT.nextToken();
            String priceToken = sT.nextToken();

            getBookID.setText(bookIDToken);
            getTitle.setText(titleToken);
            getAuthor.setText(authorToken);
            getISBN.setText(ISBNToken);
            getDescription.setText(descriptionToken);
            getPrice.setText(priceToken);

        }


//        getLoadBook_btn.setOnClickListener(view -> {
//            SharedPreferences.Editor editor = getSharedPreferences("MY_PREF_FILENAME", MODE_PRIVATE).edit();
//            editor.putString("TheBookID", printBookID);
//            editor.apply();
//            getBookID.setText(TheBookID);
//
//            SharedPreferences.Editor editor = getSharedPreferences("MY_PREF_FILENAME", MODE_PRIVATE).edit();
//            editor.putString("TheBookID", printBookID);
//            editor.apply();
////
//        });
    }
    // For getBook ID Comment out because button is removed
//    public void onDisplay(View v) {
//        String printBookID = getBookID.getText().toString();
//        String printTitle = getTitle.getText().toString();
//        String printAuthor = getAuthor.getText().toString();
//        String printISBN = getISBN.getText().toString();
//        String printDescription = getDescription.getText().toString();
//        float printPrice = Float.parseFloat(getPrice.getText().toString());
//
//        getAddbook_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg = "Title: (" + printTitle + ") Price: ($" + String.format("%.2f", printPrice) + ")";
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                getText();
//            }
//        });
    //        getClear_fields_btn.setOnClickListener(view -> {
//            clearFields();
//        });

//
//    }

}



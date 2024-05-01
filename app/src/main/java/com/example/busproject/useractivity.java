package com.example.busproject;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class useractivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText busno;
    Button search;

    private GoogleMap mymap;
    private final int Fine_PERMISSION_CODE = 1;
    Location curr;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_useractivity);



        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        busno=findViewById(R.id.busno);
        search=findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bus=String.valueOf(busno.getText());

                mymap=googleMap;
                DatabaseReference driveravaliable= FirebaseDatabase.getInstance().getReference("Drivers");
                String ID = driveravaliable.push().getKey();

                String driverID= FirebaseAuth.getInstance().getCurrentUser().getUid();
                driveravaliable.child(bus).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            if(task.getResult().exists())
                            {
                                DataSnapshot data=task.getResult();
                                Double latitude = data.child("latitude").getValue(Double.class);
                                Double longitude = data.child("longitude").getValue(Double.class);
                                LatLng india=new LatLng(latitude,longitude);
                                mymap.addMarker(new MarkerOptions().position(india).title("bus"));
                                mymap.moveCamera(CameraUpdateFactory.newLatLng(india));
                            }
                            else {
                                Toast.makeText(useractivity.this, "driver not there", Toast.LENGTH_SHORT).show();
                            }

                        }

                        else {
                            Toast.makeText(useractivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            }
        });





    }
}
package com.example.mobile_programming_map;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

public class MyMapFragment extends Fragment{
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private MainActivity activity;
    public MapView mapView;
    public MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private static final String ICON_ID = "ICON_ID";
    private Marker current_marker;
    private LocationComponent locationComponent;
    private LatLng start_point = null;
    private ArrayList<Marker> markers;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map, container, false);
    }
    public Icon drawableToIcon(@NonNull Context context, Drawable vectorDrawable, int colorRes) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, colorRes);
        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }
    public void addAllMarkers(){
        Pair pair = activity.mydb.getAllPositions();
        ArrayList longlats = (ArrayList)pair.first;
        ArrayList names = (ArrayList)pair.second;
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        markers = new ArrayList<>();
        for (int i = 0; i < longlats.size(); i++){

            LatLng p = new LatLng(Double.parseDouble((String)((Pair)longlats.get(i)).first), Double.parseDouble((String)((Pair)longlats.get(i)).second));
            Drawable iconDrawables = ContextCompat.getDrawable(activity, R.drawable.ic_baseline_location_on_24);
            Icon icons = drawableToIcon(activity, iconDrawables, Color.BLUE);

            Marker m = mapboxMap.addMarker(new MarkerOptions()
                    .position(p)
                    .title((String)names.get(i))
                    .icon(icons));
            markers.add(m);

        }
    }
    public  void  DeleteAllMarkersBut(LatLng point){
            for(Marker m : markers){
                if(m.getPosition().equals(point)){
                    mapboxMap.deselectMarker(m);
                }else{

                    Drawable iconDrawables = ContextCompat.getDrawable(activity, R.drawable.ic_baseline_location_on_24);
                    Icon icons = drawableToIcon(activity, iconDrawables, Color.RED);

                        mapboxMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title((m.getTitle()))
                                .icon(icons));


                    mapboxMap.deselectMarker(m);
                }

            }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mapView = (MapView) view.findViewById(R.id.mapView);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                MyMapFragment.this.mapboxMap = mapboxMap;

                addAllMarkers();
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Create an empty GeoJSON source using the empty feature collection
                        // Set up a new symbol layer for displaying the searched location's feature coordinates
                        if(start_point == null){
                            enableLocationComponent(style, CameraMode.TRACKING);
                        }else{
                            enableLocationComponent(style, CameraMode.NONE);
                        }
                        initSearchFab();
                        setUpSource(style);
                        setupLayer(style);
                    }
                });
                if(start_point != null){
                    Log.i("HEYYY", "onViewCreated HEYYYYYY: "+ start_point.getLatitude());
                    moveToPoint(start_point);
                    DeleteAllMarkersBut(start_point);
                }
                MyMapFragment.this.mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public boolean onMapLongClick(@NonNull LatLng point) {
                        if(current_marker != null){
                            current_marker.remove();}
                        Drawable iconDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_baseline_location_on_24);
                        Icon icon = drawableToIcon(activity, iconDrawable, Color.RED);
                        current_marker = mapboxMap.addMarker(new MarkerOptions()
                                .position(point)
                                .icon(icon));

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = requireActivity().getLayoutInflater();
                        View content =  inflater.inflate(R.layout.modal, null);
                        EditText name_text = (EditText) content.findViewById(R.id.name);
                        builder.setView(inflater.inflate(R.layout.modal, null))
                                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
//                                        markerViewManager.scheduleViewMarkerInvalidation();
                                        String name = name_text.getText().toString();
                                        DecimalFormat df = new DecimalFormat("#.####");
                                        df.setRoundingMode(RoundingMode.CEILING);
                                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED){
                                            Log.i("per", "onClick: "+ "permission accepted");
                                            if(activity.mydb.insertLocation(name,
                                                    df.format(point.getLatitude()),
                                                    df.format(point.getLongitude()))){
                                                Toast.makeText(getApplicationContext(), "done",
                                                        Toast.LENGTH_SHORT).show();
                                            } else{
                                                Toast.makeText(getApplicationContext(), "not done",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Storage permission is not granted.\n Please grant storage permission for this app.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.setView(content);
                        TextView lat = (TextView) content.findViewById(R.id.Lat);
                        lat.setText(String.valueOf("Latitude = " + point.getLatitude()));
                        TextView longt = (TextView) content.findViewById(R.id.Long);
                        longt.setText(String.valueOf("Longitude = " + point.getLongitude()));
                        builder.create();
                        builder.show();
                        return true;
                    }
                });

                activity.findViewById(R.id.back_to_camera_tracking_mode).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Location location = locationComponent.getLastKnownLocation();
                        if (location != null) {

                            setStart_point(new LatLng(location.getLatitude(), location.getLongitude()));
                            moveToPoint(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    }
                });

            }
        });


    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle, int mode) {
        //if (PermissionsManager.areLocationPermissionsGranted(this.activity)) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this.activity, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(mode);
            locationComponent.setRenderMode(RenderMode.NORMAL);
        } else {
            //permissionsManager = new PermissionsManager(this);
            //permissionsManager.requestLocationPermissions(this.activity);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            enableLocationComponent(loadedMapStyle, mode);
        }
    }

    private void initSearchFab() {
        activity.findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(activity);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        ));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

            // Move map camera to the selected location
                    LatLng point = new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude());
                    moveToPoint(point);
                    Drawable iconDrawables = ContextCompat.getDrawable(activity, R.drawable.ic_baseline_location_on_24);
                    Icon icons = drawableToIcon(activity, iconDrawables, Color.RED);
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title(selectedCarmenFeature.placeName())
                            .icon(icons)
                            );


                }
            }
        }
    }
    public void moveToPoint(LatLng point){
        CameraPosition pos = new CameraPosition.Builder()
                .target(point)
                .zoom(15)
                .tilt(18)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos), 2000);
    }
 //   @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    public void setStart_point(LatLng start_point) {
        this.start_point = start_point;
    }

//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        //when the user denys the first time, we want to show an explanation why they need to accept.
//        CharSequence message = "We need your location to show your place.";
//        Toast toast = Toast.makeText(this.activity, message, Toast.LENGTH_LONG);
//        toast.show();
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            mapboxMap.getStyle(new Style.OnStyleLoaded() {
//                @Override
//                public void onStyleLoaded(@NonNull Style style) {
//                    enableLocationComponent(style, CameraMode.TRACKING);
//                }
//            });
//        } else {
//            Toast.makeText(this.activity, "Permission not granted.", Toast.LENGTH_LONG).show();
//            this.activity.finish();
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}


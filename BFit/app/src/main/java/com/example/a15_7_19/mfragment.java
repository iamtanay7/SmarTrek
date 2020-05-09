package com.example.a15_7_19;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class mfragment extends Fragment {
    ListView mylistviewforsongs;
    String items[];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_music,null);
        //return inflater.inflate(R.layout.fragment_music,null);
        mylistviewforsongs=(ListView)view.findViewById(R.id.mysonglistview);
        runtimePermission();
        return view;
    }
    public void runtimePermission()
    {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    public ArrayList<File> findsong(File file)
    {
        ArrayList<File> arrayList= new ArrayList<>();
        File[] files = file.listFiles();
        for(File singlefile: files){
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arrayList.addAll(findsong(singlefile));
            }
            else{
                if(singlefile.getName().endsWith(".mp3") ||
                        singlefile.getName().endsWith(".wav")){
                    arrayList.add(singlefile);
                }
            }

        }
        return arrayList;
    }
    void display(){
        final ArrayList<File>mysongs = findsong(Environment.getExternalStorageDirectory());

        items = new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++)
        {
            items[i] = mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }
        ArrayAdapter<String> myadapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,items);
        mylistviewforsongs.setAdapter(myadapter);

        mylistviewforsongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songname = mylistviewforsongs.getItemAtPosition(i).toString();

                startActivity(new Intent(getActivity(),Player.class)
                        .putExtra("songs",mysongs).putExtra("songname",songname)
                        .putExtra("pos",i));

            }
        });
    }
}

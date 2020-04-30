package com.books.pharmacystore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    public CustomAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, R.layout.custom_list_view, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view =layoutInflater.inflate(R.layout.custom_list_view,parent,false);
        final String name = String.valueOf(getItem(position));
        TextView t1 = view.findViewById(R.id.name);
        ImageView imageButton = view.findViewById(R.id.drive);

        t1.setText(name);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Drive",Toast.LENGTH_SHORT).show();
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + name);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                parent.getContext().startActivity(mapIntent);
            }
        });


        return view;
    }
}

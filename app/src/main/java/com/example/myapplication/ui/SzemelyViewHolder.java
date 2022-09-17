package com.example.myapplication.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;


public class SzemelyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView id;
    TextView keresztNev;
    TextView vezetekNev;

    SzemelyKattintas listener;


    public SzemelyViewHolder(LinearLayout v, SzemelyKattintas listener) {
        super(v);
        v.setOnClickListener(this);
        id = v.findViewById(R.id.idMezo);
        keresztNev = v.findViewById(R.id.keresztNevMezo);
        vezetekNev = v. findViewById(R.id.vezetekNevMezo);
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onSzemelyClick(getAdapterPosition(), view);
    }
}

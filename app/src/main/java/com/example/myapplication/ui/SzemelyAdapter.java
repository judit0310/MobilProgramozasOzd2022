package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.service.PersonDTO;

import java.util.List;

public class SzemelyAdapter extends RecyclerView.Adapter<SzemelyViewHolder> {

    private List<PersonDTO> szemelyek;
    private static SzemelyKattintas listener;

    public SzemelyAdapter(List<PersonDTO> szemelyek) {
        this.szemelyek = szemelyek;
    }

    public void setSzemelyek(List<PersonDTO> szemelyek) {
        this.szemelyek = szemelyek;
    }

    @NonNull
    @Override
    public SzemelyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.szemely_lista_elem, parent, false);
        SzemelyViewHolder vh = new SzemelyViewHolder(layout, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SzemelyViewHolder holder, int position) {
        PersonDTO szemely = szemelyek.get(position);
        holder.vezetekNev.setText(szemely.getVezetekNev());
        holder.keresztNev.setText(szemely.getKeresztNev());
        holder.id.setText(String.valueOf(szemely.getId()));
    }

    @Override
    public int getItemCount() {
        return szemelyek.size();
    }

    public void setListener(SzemelyKattintas listener) {
        SzemelyAdapter.listener = listener;
    }
}

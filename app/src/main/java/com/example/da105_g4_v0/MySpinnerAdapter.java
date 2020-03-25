package com.example.da105_g4_v0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.util.List;


//implements PowerSpinnerInterface

public class MySpinnerAdapter extends RecyclerView.Adapter<MySpinnerAdapter.ViewHolder>  {

    private  PowerSpinnerView spinnerView;
    private LayoutInflater inflater;
    private OnSpinnerItemSelectedListener onSpinnerItemSelectedListener;
    private List<String> stringList;
    private Context context;
    public MySpinnerAdapter(Context context,List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
        inflater = LayoutInflater.from(context);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_main_store, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewholder, int position) {

    }



    @Override
    public int getItemCount() {

        return stringList.size();

    }

    public void notifyItemSelected(int index) {

    }

    public void setItems(List itemList) {


    }


    public PowerSpinnerView getSpinnerView() {
        return this.spinnerView;
    }


    public OnSpinnerItemSelectedListener getOnSpinnerItemSelectedListener() {
        return this.onSpinnerItemSelectedListener;
    }

    public void setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener var1) {
        this.onSpinnerItemSelectedListener = var1;
    }


}

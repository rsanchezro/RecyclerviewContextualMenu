package com.omer.contextualmenu;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Activity mActivity;
    private static ArrayList<Model> mDataset;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView mImageView;
        TextView mTextView;
        View mView;

        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tvRowItem);
            mImageView = (ImageView) v.findViewById(R.id.ivRowItem);
            mView = v;
            v.setOnLongClickListener(this);
            mImageView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            Log.i("Informacion","(ClickLargo)La posicion del elemento pulsado es: "+getAdapterPosition());
            ((MainActivity) mActivity).prepareToolbar(getAdapterPosition());
            //Se retorna true para indicar que has consumido el evento y por lo tanto que no se gestionen otros eventos para esta vista
            return false;
        }

        @Override
        public void onClick(View view) {
            Log.i("Informacion","(ClickCorto)Se pulsa el elemento:"+getAdapterPosition());
            if (MainActivity.isInActionMode) {
                ((MainActivity) mActivity).prepareSelection(getAdapterPosition());
                notifyItemChanged(getAdapterPosition());
            }
        }

    }

    MyAdapter(Activity activity, ArrayList<Model> myDataset) {
        this.mActivity = activity;
        this.mDataset = myDataset;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("Informacion","Se pinta el elemento: "+position+"getAdapterPosition"+holder.getAdapterPosition());
        Model model = mDataset.get(position);
        holder.mView.setBackgroundResource(R.color.white);
        holder.mTextView.setText(model.getText());
        holder.mImageView.setImageResource(model.getImage());

        if (MainActivity.isInActionMode) {
            if (MainActivity.selectionList.contains(mDataset.get(position))) {
                holder.mView.setBackgroundResource(R.color.grey_200);
                holder.mImageView.setImageResource(R.drawable.ic_check_circle_24dp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void removeData(ArrayList<Model> list) {
        for (Model model : list) {
            //Log.i("Informacion","Se elimina el elemnto de la posicion",list)
            mDataset.remove(model);
        }
        notifyDataSetChanged();
    }

    public void changeDataItem(int position, Model model) {
        mDataset.set(position, model);
        notifyDataSetChanged();
    }

    // for edit
    public static ArrayList<Model> getDataSet() {
        return mDataset;
    }
}
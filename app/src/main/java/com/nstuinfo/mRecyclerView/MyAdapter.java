package com.nstuinfo.mRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nstuinfo.R;
import com.nstuinfo.SecondActivity;
import com.nstuinfo.mOtherUtils.AnimationUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by whoami on 10/16/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ColorGenerator generator = ColorGenerator.MATERIAL;
    ColorGenerator list_Color = ColorGenerator.create(Arrays.asList(
            0xffffffff,
            0xff8ccbf1
    ));

    private List<String> itemsList = null;
    private Context context;
    private int previousPosition = -1;

    public MyAdapter(Context context, List<String> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_model, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.titleTV.setText(itemsList.get(position));

        // G-MAIL STYLE IMAGE BACKGROUND
        String letter = String.valueOf(itemsList.get(position).charAt(0));
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());
        holder.imageView.setImageDrawable(drawable);

        if (position > previousPosition) { // scrolling down
            AnimationUtils.animate(holder.itemView, true);
        } else { // scrolling up
            AnimationUtils.animate(holder.itemView, false);
        }
        previousPosition = position;
        AnimationUtils.setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titleTV;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recyclerImageView);
            titleTV = itemView.findViewById(R.id.recyclerTextViewTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, SecondActivity.class);
            intent.putExtra("TITLE", titleTV.getText().toString().trim());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}

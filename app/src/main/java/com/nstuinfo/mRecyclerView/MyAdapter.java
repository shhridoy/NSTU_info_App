package com.nstuinfo.mRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nstuinfo.R;
import com.nstuinfo.DetailsActivity;
import com.nstuinfo.mJsonUtils.ExtractJson;
import com.nstuinfo.mJsonUtils.ReadWriteJson;
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
    private String tag = null;
    private String title = null;
    private int previousPosition = -1;

    public MyAdapter(Context context, List<String> itemsList, String tag) {
        this.context = context;
        this.itemsList = itemsList;
        this.tag = tag;
    }

    public MyAdapter(Context context, List<String> itemsList, String title, String tag) {
        this.context = context;
        this.itemsList = itemsList;
        this.title = title;
        this.tag = tag;
    }

    public MyAdapter(Context context, List<String> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        if (tag != null) {
            if (tag.equalsIgnoreCase("second")) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_model_second, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_model_home, parent, false);
            }
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_model_home, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.titleTV.setText(itemsList.get(position));

        if (!tag.equalsIgnoreCase("second")) {
            // G-MAIL STYLE IMAGE BACKGROUND
            String letter = String.valueOf(itemsList.get(position).charAt(0));
            TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());
            holder.imageView.setImageDrawable(drawable);
        }

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

        CardView cardView;
        TextView titleTV, titleHintTV;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recyclerImageView);
            titleTV = itemView.findViewById(R.id.recyclerTextViewTitle);
            titleHintTV = itemView.findViewById(R.id.recyclerTVHint);
            cardView = itemView.findViewById(R.id.list_model_second_cardView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (tag.equalsIgnoreCase("second")) {
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.list_item_selection_color));
                detailsPopUpWindow(titleTV.getText().toString().trim());
            } else {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("TITLE", titleTV.getText().toString().trim());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    @SuppressLint("InflateParams")
    private void detailsPopUpWindow(String tvTitle) {

        final PopupWindow mPopUpWindow;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.details_popup_window,null);
        }

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        mPopUpWindow = new PopupWindow(layout, width, height, true);

        RelativeLayout backDimRL = null;
        RelativeLayout mainRL = null;
        LinearLayout linearLayout = null;
        TextView textView = null;

        if (layout != null) {
            backDimRL = layout.findViewById(R.id.dimRL);
            mainRL = layout.findViewById(R.id.main_popup);
            linearLayout = layout.findViewById(R.id.mainLL);
            textView = layout.findViewById(R.id.popUpTitleTV);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.88) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);

        textView.setText(tvTitle);

        assert backDimRL != null;
        backDimRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });

        assert linearLayout != null;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do
            }
        });


        ExtractJson extractJson = new ExtractJson(context, ReadWriteJson.readFile(context), linearLayout);
        extractJson.getPopUpView(title, tvTitle);


        //Set up touch closing outside of pop-up
        mPopUpWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_up_bg));
        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.setOutsideTouchable(true);

        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setContentView(layout);
        mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

}

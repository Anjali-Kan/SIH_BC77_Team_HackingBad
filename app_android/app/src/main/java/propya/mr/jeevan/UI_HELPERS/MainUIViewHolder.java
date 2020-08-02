package propya.mr.jeevan.UI_HELPERS;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import propya.mr.jeevan.R;

public class MainUIViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;

    public MainUIViewHolder(@NonNull View itemView, View.OnClickListener listener) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        imageView.setOnClickListener(listener);
    }
}


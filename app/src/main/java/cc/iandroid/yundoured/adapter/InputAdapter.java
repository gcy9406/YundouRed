package cc.iandroid.yundoured.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cc.iandroid.yundoured.R;

/**
 * Created by gcy on 2017/6/2.
 */

public class InputAdapter extends RecyclerView.Adapter<InputAdapter.InputViewHolder>{
    private Context context;
    private List<String> data = new ArrayList<>();

    public InputAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public InputViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_input_item,parent,false);
        return new InputViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InputViewHolder holder, int position) {
        if (data.get(position).equals("1")){
            Glide.with(context).load(R.mipmap.icon_state_on).into(holder.itemState);
        }else {
            Glide.with(context).load(R.mipmap.icon_state_off).into(holder.itemState);
        }
        holder.itemName.setText(context.getString(R.string.input)+(position+1));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class InputViewHolder extends RecyclerView.ViewHolder{
        ImageView itemState;
        TextView itemName;
        public InputViewHolder(View itemView) {
            super(itemView);
            itemState = (ImageView) itemView.findViewById(R.id.item_state);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}

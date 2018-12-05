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
import cc.iandroid.yundoured.callback.OnRelayControlClickLister;

/**
 * Created by gcy on 2017/5/29.
 */

public class OutputAdapter extends RecyclerView.Adapter<OutputAdapter.DeviceViewHolder>{
    List<String> data = new ArrayList<>();
    private Context context;
    private OnRelayControlClickLister onRelayControlClickLister;
    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setOnRelayControlClickLister(OnRelayControlClickLister onRelayControlClickLister) {
        this.onRelayControlClickLister = onRelayControlClickLister;
    }

    public OutputAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_relay_item,parent,false);
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, int position) {
        if (data.get(position).equals("1")){
            Glide.with(context).load(R.mipmap.icon_state_on).into(holder.itemState);
        }else {
            Glide.with(context).load(R.mipmap.icon_state_off).into(holder.itemState);
        }
        holder.itemName.setText(context.getString(R.string.relay)+(position+1));

        holder.itemOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                onRelayControlClickLister.doOnClick(pos,data.size());
            }
        });
        holder.itemOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                onRelayControlClickLister.doOffClick(pos,data.size());
            }
        });
        holder.itemPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                onRelayControlClickLister.doPluseClick(pos,data.size());
            }
        });
        holder.itemTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                onRelayControlClickLister.doTurnClick(pos,data.size());
            }
        });
        holder.itemLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                onRelayControlClickLister.doLockClick(pos,data.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        ImageView itemState;
        TextView itemName;
        TextView itemLock;
        TextView itemTurn;
        TextView itemPluse;
        TextView itemOn;
        TextView itemOff;
        public DeviceViewHolder(View itemView) {
            super(itemView);
            itemState = (ImageView) itemView.findViewById(R.id.item_state);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemLock = (TextView) itemView.findViewById(R.id.item_lock);
            itemTurn = (TextView) itemView.findViewById(R.id.item_turn);
            itemPluse = (TextView) itemView.findViewById(R.id.item_pluse);
            itemOn = (TextView) itemView.findViewById(R.id.item_on);
            itemOff = (TextView) itemView.findViewById(R.id.item_off);
        }
    }
}

package cc.iandroid.yundoured.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cc.iandroid.yundoured.R;
import cc.iandroid.yundoured.bean.DeviceInfo;
import cc.iandroid.yundoured.callback.OnDeviceListClickListener;

/**
 * Created by gcy on 2017/5/29.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {
    private List<DeviceInfo> data = new ArrayList<>();
    private Context context;
    private OnDeviceListClickListener onDeviceListClickListener;

    public void setOnDeviceListClickListener(OnDeviceListClickListener onDeviceListClickListener) {
        this.onDeviceListClickListener = onDeviceListClickListener;
    }

    public DeviceListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DeviceInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_list,parent,false);
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, final int position) {
        if (data.get(position).getState()){
            Glide.with(context).load(R.mipmap.icon_state_on).into(holder.deviceState);
        }else {
            Glide.with(context).load(R.mipmap.icon_state_off).into(holder.deviceState);
        }
        holder.deviceName.setText(data.get(position).getName());
        holder.deviceSn.setText(data.get(position).getSn());
        holder.deviceVersion.setText(data.get(position).getVer());
        holder.deviceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                onDeviceListClickListener.doClick(pos,data.get(pos));
            }
        });
        holder.deviceItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getLayoutPosition();
                onDeviceListClickListener.doLongClick(pos,data.get(pos),holder.deviceItem);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder{
        LinearLayout deviceItem;
        ImageView deviceState;
        TextView deviceName;
        TextView deviceSn;
        TextView deviceVersion;
        public DeviceViewHolder(View itemView) {
            super(itemView);
            deviceItem = (LinearLayout) itemView.findViewById(R.id.device_item);
            deviceState = (ImageView) itemView.findViewById(R.id.device_state);
            deviceName = (TextView) itemView.findViewById(R.id.device_name);
            deviceSn = (TextView) itemView.findViewById(R.id.device_sn);
            deviceVersion = (TextView) itemView.findViewById(R.id.device_ver);
        }
    }
}
package cc.iandroid.yundoured.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.iandroid.yundoured.R;
import cc.iandroid.yundoured.activity.DeviceLocalActivity;
import cc.iandroid.yundoured.bean.LocalDeviceBean;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalViewHolder>{
    private List<LocalDeviceBean> data = new ArrayList<>();

    public void setData(List<LocalDeviceBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public LocalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_local_item,parent,false);
        return new LocalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LocalViewHolder holder, int position) {
        final LocalDeviceBean deviceBean = data.get(position);
        holder.mIp.setText(deviceBean.getIp());
        holder.mSn.setText(deviceBean.getSn());
        holder.mVer.setText(deviceBean.getVer());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDevice != null) {
                    mDevice.doClick(holder.getLayoutPosition(),deviceBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LocalViewHolder extends RecyclerView.ViewHolder{
        TextView mIp;
        TextView mSn;
        TextView mVer;
        public LocalViewHolder(View itemView) {
            super(itemView);
            mIp = (TextView) itemView.findViewById(R.id.local_ip);
            mSn = (TextView) itemView.findViewById(R.id.local_sn);
            mVer = (TextView) itemView.findViewById(R.id.local_ver);
        }
    }
    private IDevice mDevice;

    public void setDevice(IDevice device) {
        mDevice = device;
    }

    public interface IDevice{
        void doClick(int pos,LocalDeviceBean bean);
    }
}

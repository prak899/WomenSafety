package in.pm.wosafe.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.pm.wosafe.Activity.Dashboard;
import in.pm.wosafe.Model.MasterModel;
import in.pm.wosafe.R;


public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.ViewHolder>{
    private List<MasterModel> shops;
    private Context mContext;

    public MasterAdapter(List<MasterModel> lists, Context mContext) {
        this.shops = lists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.promoters_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MasterModel sp=shops.get(position);
        holder.promoter_bid.setText(sp.getFamilyType());
        holder.promoter_name.setText(sp.getName());
        holder.promoter_address.setText(sp.getNumber());


        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, Dashboard.class);
                *//*intent.putExtra("shopId", sp.getShopId());
                intent.putExtra("shopName", sp.getShopName());
                intent.putExtra("shopType", sp.getShopType());
                intent.putExtra("shopNumber", sp.getShopNumber());
                intent.putExtra("shopLocation", sp.getShopLocation());
                intent.putExtra("shopImage", sp.getShopPhoto());

                intent.putExtra("shopLatitude", sp.getLatitude());
                intent.putExtra("shopLangitude", sp.getLongitude());
                intent.putExtra("shopAbout", sp.getShopAbout());*//*
                mContext.startActivity(intent);
            }
        });*/
        holder.Promoter_more.setOnClickListener(v-> {

        });
        holder.CallEmergency.setOnClickListener(v-> {

            Toast.makeText(mContext, sp.getNumber(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView promoter_bid,promoter_name, promoter_address;
        private ImageView CallEmergency, Promoter_more;

        public ViewHolder(View itemView) {
            super(itemView);

            promoter_name=(TextView)itemView.findViewById(R.id.promoter_name);
            promoter_address=(TextView)itemView.findViewById(R.id.promoter_address);
            promoter_bid=(TextView)itemView.findViewById(R.id.promoter_bid);


            CallEmergency=(ImageView)itemView.findViewById(R.id.calls);
            Promoter_more=(ImageView)itemView.findViewById(R.id.more);
        }
    }
}

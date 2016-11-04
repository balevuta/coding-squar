package sample.than.codingmyquar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ItemHolder> {

    private List<ItemModel> mLstItems;

    public ListItemAdapter() {
        mLstItems = Collections.emptyList();
    }

    public void setList(List<ItemModel> pItemModels) {
        this.mLstItems = pItemModels;
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_download_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        final ItemModel countryItem = mLstItems.get(position);
        holder.txtName.setText(countryItem.getName());
        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                EventClickObject lEventClickObject = new EventClickObject(countryItem.getUrl(), position);
                EventBus.getDefault().post(lEventClickObject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLstItems.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        Button btnDownload;

        ItemHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.item_txt_name);
            btnDownload = (Button) itemView.findViewById(R.id.item_btn_download);
        }

    }
}

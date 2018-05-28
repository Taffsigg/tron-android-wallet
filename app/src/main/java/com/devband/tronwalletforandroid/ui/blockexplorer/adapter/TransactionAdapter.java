package com.devband.tronwalletforandroid.ui.blockexplorer.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronlib.dto.Transfer;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> implements AdapterDataModel<Transfer>, AdapterView {

    private List<Transfer> mList;

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public TransactionAdapter(Context context, View.OnClickListener onItemClickListener) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_block_transaction, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transfer item = mList.get(position);

        holder.hashText.setText("#" + item.getHash());
        holder.blockNumberText.setText(Constants.numberFormat.format(item.getBlock()));

        if (!TextUtils.isEmpty(item.getTransferFromAddress())) {
            SpannableString fromAddressContent = new SpannableString(item.getTransferFromAddress());
            fromAddressContent.setSpan(new UnderlineSpan(), 0, fromAddressContent.length(), 0);
            holder.fromAddressText.setText(fromAddressContent);
        }

        if (!TextUtils.isEmpty(item.getTransferToAddress())) {
            SpannableString toAddressContent = new SpannableString(item.getTransferToAddress());
            toAddressContent.setSpan(new UnderlineSpan(), 0, toAddressContent.length(), 0);
            holder.toAddressText.setText(toAddressContent);
        }

        holder.fromAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AccountDetailActivity.class);
                intent.putExtra(AccountDetailActivity.EXTRA_ADDRESS, item.getTransferFromAddress());
                mContext.startActivity(intent);
            }
        });

        holder.toAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AccountDetailActivity.class);
                intent.putExtra(AccountDetailActivity.EXTRA_ADDRESS, item.getTransferToAddress());
                mContext.startActivity(intent);
            }
        });

        holder.copyToAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(item.getTransferToAddress());
                Toast.makeText(mContext, mContext.getString(R.string.copy_to_address_msg),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        holder.copyFromAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(item.getTransferFromAddress());
                Toast.makeText(mContext, mContext.getString(R.string.copy_from_address_msg),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        double amount = item.getAmount();

        if (Constants.TRON_SYMBOL.equalsIgnoreCase(item.getTokenName())) {
            amount /= Constants.ONE_TRX;
        }

        holder.valueText.setText(Constants.tronBalanceFormat.format(amount) + " " + item.getTokenName());
        holder.createdText.setText(Constants.sdf.format(new Date(item.getTimestamp())));
    }

    private void copyToClipboard(String content) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", content);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(Transfer model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<Transfer> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public Transfer getModel(int position) {
        return mList.get(position);
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hash_text)
        public TextView hashText;

        @BindView(R.id.block_number_text)
        public TextView blockNumberText;

        @BindView(R.id.from_address_text)
        public TextView fromAddressText;

        @BindView(R.id.to_address_text)
        public TextView toAddressText;

        @BindView(R.id.copy_from_address)
        public ImageView copyFromAddressView;

        @BindView(R.id.copy_to_address)
        public ImageView copyToAddressView;

        @BindView(R.id.value_text)
        public TextView valueText;

        @BindView(R.id.created_text)
        public TextView createdText;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
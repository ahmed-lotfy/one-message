package onemessagecompany.onemessage.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import onemessagecompany.onemessage.R;
import onemessagecompany.onemessage.data.MyApplication;
import onemessagecompany.onemessage.model.Message;
import onemessagecompany.onemessage.rest.ApiClient;
import onemessagecompany.onemessage.rest.SendMessageApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 52Solution on 7/06/2017.
 */

public class AdminMessagsAdapter extends RecyclerView.Adapter<AdminMessagsAdapter.AdminMessagsAdapterViewHolder> {


    private final AdminMessagsAdapterOnClickHandler mClickHandler;

    private Context context;
    private List<Message> messages;
    private int rowLayout;

    public interface AdminMessagsAdapterOnClickHandler {
        void onClick(View v, int position);

        void onRemoveMessageClick(View v, int position);

    }

    public AdminMessagsAdapter(AdminMessagsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public AdminMessagsAdapter(List<Message> messages, int rowLayout, Context context, AdminMessagsAdapterOnClickHandler clickHandler) {
        this.messages = messages;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mClickHandler = clickHandler;
    }


    public class AdminMessagsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView messageBody;
        TextView messageDate;
        ImageView removemsg;
        ImageView imgNewMsg;

        public AdminMessagsAdapterViewHolder(View view) {
            super(view);
            messageBody = (TextView) view.findViewById(R.id.item_list_message_body);
            messageDate = (TextView) view.findViewById(R.id.item_list_message_date);
            removemsg = (ImageView) view.findViewById(R.id.remove_msg);
            imgNewMsg = (ImageView) view.findViewById(R.id.img_new_message);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onClick(v, getAdapterPosition());
                }
            });

            removemsg.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onRemoveMessageClick(v, getAdapterPosition());
                }
            });
        }

//    @Override
//    public void onClick(View v) {
//      int adapterPosition = getAdapterPosition();
//      Message message = messages.get(adapterPosition);
//      mClickHandler.onClick(message);
//    }
    }


    @Override
    public AdminMessagsAdapter.AdminMessagsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new AdminMessagsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminMessagsAdapterViewHolder holder, final int position) {

        Message message = messages.get(position);
        if (message.getBody().length() > 20)
            holder.messageBody.setText(message.getBody().substring(0,20)+"...");
        else
            holder.messageBody.setText(message.getBody());

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(message.getRV());
            SimpleDateFormat dateFormatTime = new SimpleDateFormat("MMM dd,yyyy  hh:mm a");
            String dateTime = dateFormatTime.format(date);
            holder.messageDate.setText(dateTime);

        } catch (ParseException ex) {
        }

        if(message.getHasReply())
            holder.imgNewMsg.setVisibility(View.VISIBLE);
        else
            holder.imgNewMsg.setVisibility(View.INVISIBLE);

    }

    public void deleteMessage(final int position) {
        SendMessageApi apiService = ApiClient.getAuthorizedClient().create(SendMessageApi.class);


        Call<Void> call = apiService.RemoveMessage(messages.get(position).getID());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    messages.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(MyApplication.getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public Message getSelectedMessage(int Position) {
        return messages.get(Position);
    }
}

package edu.upc.eetac.dsa.lastsurvivorfrontend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Enemy;
import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Player;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    //Repo List
    private List<Player> playerList;
    private List<Enemy> enemyList;
    // Adding Listener to call it from Main Activity
    private OnItemClickListener mListener;
    //Through this we get the click and the position to our main activity
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    //for When we call the OnClick Method from main
    public void SetOnItemClickListener(OnItemClickListener listener){
        mListener = listener ;
    }
    // Provide a reference to the views for each data item, Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in our viewHolder
        public TextView playerExperience;
        public TextView playerUsername;
        public ImageView imageIcon;
        public View layout;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            layout = itemView;
            playerExperience = itemView.findViewById(R.id.secondLine);
            playerUsername = itemView.findViewById(R.id.firstLine);
            imageIcon = itemView.findViewById(R.id.avatarImg);
            //Click Handler for the whole Item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                        public void onClick(View v){
                            if( listener !=null){
                                int position = getAdapterPosition();
                                if(position != RecyclerView.NO_POSITION){
                                    listener.onItemClick(position);
                                }
                            }
                        }
                });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Player> myDataset)
    {
        playerList = myDataset;
    }


    // Create new views (invoked by the layout manager)

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from( parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, padding and layout parameters
        ViewHolder vh = new ViewHolder(v,mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       // final String name = values.get(position);
        holder.playerUsername.setText(playerList.get(position).getUsername());
        holder.playerExperience.setText(String.valueOf(playerList.get(position).getExperience()));
       // Bitmap bitmapImg  = StringToBitmap(playerList.get(position).getAvatar());
        //holder.imageIcon.setImageBitmap(bitmapImg);
        if(!playerList.get(position).getAvatar().equals("basicAvatar")){
            Bitmap bitmapImg  = StringToBitmap(playerList.get(position).getAvatar());
            holder.imageIcon.setImageBitmap(bitmapImg);
        }else{
            //Drawable myDrawable = getResources().getDrawable(R.drawable.sword_png_icon_20);
            Bitmap icon = BitmapFactory.decodeResource(holder.imageIcon.getResources(),R.drawable.userlogo);
            icon = getResizedBitmap(icon,200,200);
            holder.imageIcon.setImageBitmap(icon);
        }
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    private Bitmap StringToBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }
    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
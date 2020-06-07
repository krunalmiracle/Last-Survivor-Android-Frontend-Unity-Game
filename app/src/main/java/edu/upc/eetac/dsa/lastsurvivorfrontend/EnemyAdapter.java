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

import java.util.List;

import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Enemy;
import edu.upc.eetac.dsa.lastsurvivorfrontend.models.Player;


public class EnemyAdapter extends RecyclerView.Adapter<EnemyAdapter.ViewHolder> {
    //Repo List

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
        public TextView description;
        public TextView enemyName;
        public ImageView imageIcon;
        public View layout;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            layout = itemView;
            description = itemView.findViewById(R.id.secondLine);
            enemyName = itemView.findViewById(R.id.firstLine);
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
    public EnemyAdapter(List<Enemy> myDataset)
    {
        enemyList = myDataset;
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
    public void onBindViewHolder(@NonNull EnemyAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       // final String name = values.get(position);
        holder.enemyName.setText(enemyList.get(position).getName());
        holder.description.setText(String.valueOf(enemyList.get(position).getDescription()));
        Bitmap bitmapImg  = StringToBitmap(enemyList.get(position).getAvatar());
        Bitmap resized= getResizedBitmap(bitmapImg,64,96);
        holder.imageIcon.setImageBitmap(resized);
    }
    private Bitmap StringToBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }
    @Override
    public int getItemCount() {
        return enemyList.size();
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
}
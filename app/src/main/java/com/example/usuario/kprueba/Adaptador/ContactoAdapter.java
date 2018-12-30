package com.example.usuario.kprueba.Adaptador;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usuario.kprueba.R;
import com.example.usuario.kprueba.clases.MainActivity;
import com.example.usuario.kprueba.modelo.Contacto;

import java.util.List;


public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Contacto> mDataset;

    private View.OnClickListener onClickListener;


    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        onClickListener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView lblNombre, lblNumero;
        public ImageView imgContacto;

        public MyViewHolder(View v) {
            super(v);
            lblNombre = v.findViewById(R.id.lbl_item_contacto_combre);
            lblNumero = v.findViewById(R.id.lbl_item_contacto_numero);
            imgContacto = v.findViewById(R.id.img_item_contacto);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactoAdapter(List<Contacto> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        v.setOnClickListener(this);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.lblNombre.setText(mDataset.get(position).getNombre() + " " + mDataset.get(position).getApellido());
        holder.lblNumero.setText(mDataset.get(position).getNumero());

        //imagen
        String img = mDataset.get(position).getImagen();
        //Crea ruta de la imagen.
        img = img.replace("@drawable/", "android.resource://" + MainActivity.PACKAGE_NAME + "/drawable/");
        //Obtiene la uri de la imagen.
        Uri uriImagen = Uri.parse(img);
        //Agrega imagen al ImageView.
        holder.imgContacto.setImageURI(uriImagen);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

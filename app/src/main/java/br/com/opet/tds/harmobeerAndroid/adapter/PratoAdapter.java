package br.com.opet.tds.harmobeerAndroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.opet.tds.harmobeerAndroid.R;
import br.com.opet.tds.harmobeerAndroid.model.Prato;



public class PratoAdapter extends ArrayAdapter<Prato> {
    private int rId;

    public PratoAdapter(@NonNull Context context, int resource, @NonNull List<Prato> objects) {
        super(context, resource, objects);
        rId = resource;
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent){
        View mView = currentView;

        if(mView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = inflater.inflate(rId,null);
        }

        Prato prato = getItem(position);

        TextView textNome = mView.findViewById(R.id.textNomePrato);

        textNome.setText(prato.getNome().toUpperCase());


        return mView;
    }
}

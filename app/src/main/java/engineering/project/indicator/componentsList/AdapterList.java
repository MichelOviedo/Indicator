package engineering.project.indicator.componentsList;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import engineering.project.indicator.R;
import engineering.project.indicator.activities.IndicatorTabs;
import engineering.project.indicator.preferences.Preferences;
import io.realm.Realm;

/**
 * Created by EduardoMichel on 08/11/2015.
 */
public class AdapterList extends BaseExpandableListAdapter {

    Context context;
    ArrayList<ParentList> mParent;
    private LayoutInflater inflater;
    Preferences p;
    Realm realm;

    public AdapterList(Context context, ArrayList<ParentList> parent) {
        this.context = context;
        mParent = parent;
        inflater = LayoutInflater.from(context);
        p = new Preferences(context);
    }

    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return mParent.get(i).getArrayChildren().size();
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).getTitle();
    }

    public Object getPorcent(int i) {
        return mParent.get(i).getPorcentaje();
    }

    public Object getAllocation(int i){
        return  null;
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).getArrayChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.groupPosition = groupPosition;

        if (view == null)
            view = inflater.inflate(R.layout.list_parent, viewGroup, false);

        TextView title = (TextView) view.findViewById(R.id.list_item_text_view);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pBar);

        title.setText(getGroup(groupPosition).toString());
        int porcentaje = 0;
        try{
            porcentaje = Integer.parseInt(getPorcent(groupPosition).toString());
        }catch(Exception e){
            Log.e("AdapterList","Error al querer convertir la variable de porcentaje");
        }

        progressBar.setProgress(porcentaje);
        view.setTag(holder);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        final int pg = groupPosition;
        final int cpp = childPosition;

        if (view == null) {
            view = inflater.inflate(R.layout.list_child, viewGroup, false);
        }

        ArrayChildren arrayChildren = mParent.get(groupPosition).getArrayChildren().get(childPosition);

        TextView textView = (TextView) view.findViewById(R.id.item_child);
        textView.setText(arrayChildren.getTitle());
        LinearLayout hijo = (LinearLayout) view.findViewById(R.id.lnlChild);
        ImageView status = (ImageView) view.findViewById(R.id.imvStatus);

        final int allocation = arrayChildren.getIdAllocation();

        if (arrayChildren.getPhoto() == 1)
            status.setBackgroundResource(R.mipmap.good);
        else
            status.setBackgroundResource(R.mipmap.cross);

        view.setTag(holder);

        hijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setAllocation(allocation);
                Intent i = new Intent(context, IndicatorTabs.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        /* used to make the notifyDataSetChanged() method work */
        super.registerDataSetObserver(observer);
    }

    protected class ViewHolder {
        protected int childPosition;
        protected int groupPosition;
        protected Button button;
    }
}
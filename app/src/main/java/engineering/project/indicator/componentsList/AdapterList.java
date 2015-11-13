package engineering.project.indicator.componentsList;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
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
import java.util.StringTokenizer;

import engineering.project.indicator.R;
import engineering.project.indicator.activities.IndicatorTabs;
import engineering.project.indicator.preferences.Preferences;

/**
 * Created by EduardoMichel on 08/11/2015.
 */
public class AdapterList extends BaseExpandableListAdapter {

    Context context;
    ArrayList<ParentList> mParent;
    private LayoutInflater inflater;
    Preferences p;

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
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.groupPosition = groupPosition;

        if (view == null)
            view = inflater.inflate(R.layout.list_parent, viewGroup, false);

        TextView title = (TextView) view.findViewById(R.id.list_item_text_view);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pBar);

        title.setText(getGroup(groupPosition).toString());
        progressBar.setProgress(70);
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

        TextView textView = (TextView) view.findViewById(R.id.item_child);
        textView.setText(mParent.get(groupPosition).getArrayChildren().get(childPosition));
        LinearLayout hijo = (LinearLayout) view.findViewById(R.id.lnlChild);
        ImageView status = (ImageView) view.findViewById(R.id.imvStatus);

        status.setBackgroundResource(R.drawable.noterror);

        view.setTag(holder);

        final String toast = textView.getText().toString();
        StringTokenizer st = new StringTokenizer(toast, view.getResources().getString(R.string.grade));
        final String key = st.nextToken() + ""+ st.nextToken();


        hijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               p.setIdGroup(key);
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

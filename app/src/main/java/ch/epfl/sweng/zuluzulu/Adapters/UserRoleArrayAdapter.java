package ch.epfl.sweng.zuluzulu.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.User.UserRole;
import ch.epfl.sweng.zuluzulu.Utility.ImageLoader;

public class UserRoleArrayAdapter extends ArrayAdapter<Map<String, Object>> {
    private static final int layout_resource_id = R.layout.card_user_role;

    private final Context context;
    private final List<Map<String,Object>> data;
    private final OnFragmentInteractionListener mListener;

    /**
     * Basic constructor of an AssociationArrayAdapter
     *
     * @param context Context of the Fragment
     * @param data    List of Associations to view
     */
    public UserRoleArrayAdapter(Context context, List<Map<String,Object>> data, OnFragmentInteractionListener mListener) {
        super(context, layout_resource_id, data);
        this.mListener = mListener;
        this.context = context;
        this.data = data;
    }

    /**
     * Return a View of an Association in a GroupView in a form of a card with the
     * Association's name, short description and icon image
     *
     * @param position    position in the GroupView
     * @param convertView View where to put the Association's view
     * @param parent      the ListView to put the view
     * @return the Association card view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View user_view = convertView;
        UserRoleHolder holder = user_view == null ? new UserRoleHolder() : (UserRoleHolder) user_view.getTag();

        if (user_view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            user_view = inflater.inflate(layout_resource_id, parent, false);
            holder.sciper = user_view.findViewById(R.id.card_user_role_sciper);
            holder.adminCheckbox = user_view.findViewById(R.id.card_user_role_is_admin);
            user_view.setTag(holder);
        }

        final Map<String, Object> map = data.get(position);
        holder.sciper.setText((String)map.get("sciper"));

        Set<String> roles = Sets.newHashSet((List < String >)map.get("roles"));
        if(roles.contains(UserRole.ADMIN.toString()))
            holder.adminCheckbox.setChecked(true);

        holder.adminCheckbox.setOnClickListener(v -> {
            if(holder.adminCheckbox.isChecked()){
                roles.add("ADMIN");
                DatabaseFactory.getDependency().updateUserRole((String)map.get("sciper"), new ArrayList<>(roles));
            }
            else{
                roles.remove("ADMIN");
                DatabaseFactory.getDependency().updateUserRole((String)map.get("sciper"), new ArrayList<>(roles));
            }
            holder.adminCheckbox.clearFocus();
        });

        return user_view;
    }


    /**
     * Static class to easily create an Association's specific View
     */
    private class UserRoleHolder {
        TextView sciper;
        CheckBox adminCheckbox;
    }
}

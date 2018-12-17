package ch.epfl.sweng.zuluzulu.fragments.AdminFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.adapters.UserRoleArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.R;

/**
 * A simple {@link SuperFragment} subclass.
 * Use the {@link ChangeUserRoleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUserRoleFragment extends SuperFragment {

    private ListView userRoleListview;
    private UserRoleArrayAdapter adapter;
    private List<Map<String, Object>> allUsers;
    private List<Map<String, Object>> filteredUsers;

    private TextView searchBar;

    public ChangeUserRoleFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangeUserRoleFragment.
     */
    public static ChangeUserRoleFragment newInstance() {
        ChangeUserRoleFragment fragment = new ChangeUserRoleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Modify User Roles");
        allUsers = new ArrayList<>();
        filteredUsers = new ArrayList<>();
        adapter = new UserRoleArrayAdapter(getContext(), filteredUsers, mListener);
        fillAllUsers();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_user_role, container, false);
        searchBar = view.findViewById(R.id.user_role_searchbar);
        userRoleListview = view.findViewById(R.id.user_role_list);

        userRoleListview.setAdapter(adapter);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredUsers.clear();
                for (Map<String, Object> map : allUsers) {
                    String sciper = (String) map.get("sciper");
                    if (sciper != null && sciper.contains(s))
                        filteredUsers.add(map);
                }
                adapter = new UserRoleArrayAdapter(getContext(), filteredUsers, mListener);
                userRoleListview.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void fillAllUsers() {
        DatabaseFactory.getDependency().getAllUsers(result -> {
            allUsers.clear();
            filteredUsers.clear();
            allUsers.addAll(result);
            Collections.sort(allUsers, ((o1, o2) -> {
                assert (o1.containsKey("sciper") && o2.containsKey("sciper"));
                return Integer.parseInt((String) o1.get("sciper")) - Integer.parseInt((String) o2.get("sciper"));
            }));
            filteredUsers.addAll(allUsers);
            adapter.notifyDataSetChanged();
        });
    }
}

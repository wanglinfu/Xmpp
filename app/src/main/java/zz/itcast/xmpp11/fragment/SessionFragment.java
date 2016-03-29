package zz.itcast.xmpp11.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zz.itcast.xmpp11.R;

/**
 * 会话  ctrl + alt + o
 */
public class SessionFragment extends BaseFragment {


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_session, container, false);
    }

}

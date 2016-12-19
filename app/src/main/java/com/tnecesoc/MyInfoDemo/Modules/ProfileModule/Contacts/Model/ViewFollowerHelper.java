package com.tnecesoc.MyInfoDemo.Modules.ProfileModule.Contacts.Model;

import com.google.gson.reflect.TypeToken;
import com.tnecesoc.MyInfoDemo.Bean.ProfileBean;
import com.tnecesoc.MyInfoDemo.GlobalModel.Remote.Host;
import com.tnecesoc.MyInfoDemo.GlobalModel.Remote.RemoteModelImpl;
import com.tnecesoc.MyInfoDemo.Utils.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tnecesoc.MyInfoDemo.Modules.ProfileModule.Contacts.Model.ViewFriendListHelper.CLASS_TOKEN;

/**
 * Created by Tnecesoc on 2016/12/11.
 */
public class ViewFollowerHelper extends RemoteModelImpl {

    public static final String URL = Host.URL + "/view-follower-list";

    public ViewFollowerHelper(final String username) {
        super(URL, new HashMap<String, String>(){{
            put("username", username);
        }});
    }

    @Override @Deprecated
    public void doRequest(HttpUtil.HttpResponseListener listener) {
        super.doRequest(listener);
    }

    @SuppressWarnings("unchecked")
    public List<ProfileBean> doQuery(HttpUtil.HttpErrorListener listener) {
        List<ProfileBean> res = super.doQueryForObject(new TypeToken<List<ProfileBean>>() {}, listener);
        return res == null ? new ArrayList<ProfileBean>() : res;
    }
}
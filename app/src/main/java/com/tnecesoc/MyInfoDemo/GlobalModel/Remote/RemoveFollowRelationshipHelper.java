package com.tnecesoc.MyInfoDemo.GlobalModel.Remote;

import com.tnecesoc.MyInfoDemo.Utils.HttpUtil;

import java.util.HashMap;

/**
 * Created by Tnecesoc on 2016/12/11.
 */
public class RemoveFollowRelationshipHelper extends RemoteModelImpl {

    public static final String URL = Host.SERVER_HOST + "/remove-follow-relationship";

    public RemoveFollowRelationshipHelper(final String me, final String other) {
        super(URL, new HashMap<String, String>(){{
            put("username1", me);
            put("username2", other);
        }});
    }

    @Override
    public void doRequest(HttpUtil.HttpResponseListener listener) {
        super.doRequest(listener);
    }
}

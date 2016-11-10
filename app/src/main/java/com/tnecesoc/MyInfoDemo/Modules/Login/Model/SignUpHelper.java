package com.tnecesoc.MyInfoDemo.Modules.Login.Model;

import com.tnecesoc.MyInfoDemo.GlobalModel.Host;
import com.tnecesoc.MyInfoDemo.GlobalModel.RemoteModel;

import java.util.HashMap;

/**
 * Created by Tnecesoc on 2016/11/5.
 */
public class SignUpHelper extends RemoteModel {

    private static final String URL = Host.URL + "/sign-up";

    public SignUpHelper(final String community, final String phone, final String username, final String password) {
        super(URL, new HashMap<String, String>(){{
            put("community", community);
            put("phone", phone);
            put("username", username);
            put("password", password);
        }});
    }
}

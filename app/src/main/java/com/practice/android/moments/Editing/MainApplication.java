package com.practice.android.moments.Editing;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;

import io.realm.Realm;

/**
 * Created by Ashutosh on 6/14/2017.
 */

public class MainApplication extends MultiDexApplication implements IAdobeAuthClientCredentials {

    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID = "5d03e796d6c84d5fabe3015d3bc85053";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "e03c7a54-a8b1-4d94-9d2f-98121380e390";
    private static final String CREATIVE_SDK_REDIRECT_URI = "ams+02a638ea6407c54257dcd43ad41b8657ccadae38://adobeid/5d03e796d6c84d5fabe3015d3bc85053";
    private static final String[] CREATIVE_SDK_SCOPES = {"email", "profile", "address"};

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());

        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String[] getAdditionalScopesList() {
        return CREATIVE_SDK_SCOPES;
    }

    @Override
    public String getRedirectURI() {
        return CREATIVE_SDK_REDIRECT_URI;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

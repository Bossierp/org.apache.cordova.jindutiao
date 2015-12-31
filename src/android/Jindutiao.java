/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/

package org.apache.cordova.Jindutiao;

import __PACKAGE_NAME_R_;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;

import org.apache.cordova.*;
import org.apache.cordova.engine.*;

import android.app.Dialog;
import android.view.*;
import android.widget.*;
import java.util.Date;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class Jindutiao extends CordovaPlugin {

    private static final String LOG_TAG = "JindutiaoPlugin";

    private Dialog loadDialog;
    private ProgressBar progressBar;
    /**
     * Constructor.
     */
    // public Jindutiao() {
    // }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        loadDialog = new Dialog((CordovaActivity)cordova.getActivity(), R.style.dialog);  
        loadDialog.setCancelable(false);
        loadDialog.setContentView(R.layout.main);
        
        Window win = loadDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        win.setAttributes(lp);
    
        progressBar = (ProgressBar) loadDialog.findViewById(R.id.progressBar5);

        SystemWebViewEngine engine = (SystemWebViewEngine)webView.getEngine();
        SystemWebView swv = (SystemWebView)engine.getView();
        swv.setWebChromeClient(new MyWebChromeClient(engine));
    }
    // *
    //  * Called when a message is sent to plugin.
    //  *
    //  * @param id            The message id
    //  * @param data          The message data
    //  * @return              Object to stop propagation or null
    @Override
    public Object onMessage(String id, Object data) {
        if(id == "onPageFinished"){
          progressBar.setProgress(100);
          endLoad();
        }
        else if(id == "onPageStarted"){
          if (((String)data).equals("about:blank")) {
          } else {
            startLoad();
          }
        }
        else if(id == "onProgressChanged"){
          progressBar.setProgress(((Integer)data).intValue());
        }
        return null;
    }

    private void startLoad() {  
        if (loadDialog.isShowing()) {  
        } else {  
            loadDialog.show();  
        }  
    }  
  
    private void endLoad() {  
        if (loadDialog.isShowing()) {  
            loadDialog.cancel();  
            loadDialog.dismiss();  
        }  
    }

    //覆盖了底层方法
    private class MyWebChromeClient extends SystemWebChromeClient{
        public MyWebChromeClient(SystemWebViewEngine parentEngine){
            super(parentEngine);
        }
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            parentEngine.getCordovaWebView().getPluginManager().postMessage("onProgressChanged", newProgress);
        }
    }
}

    
package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;


public class WebViewFragment extends SuperFragment {
    public static final String URL = "url";
    private WebView webview;
    private String url;

    public static WebViewFragment newInstance() {
        WebViewFragment fragment = new WebViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = this.getArguments().getString(URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        webview = view.findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wView, String url) {
                if (url.contains("code=")) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("redirectUri", url);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        webview.loadUrl(url);

        return view;
    }


}

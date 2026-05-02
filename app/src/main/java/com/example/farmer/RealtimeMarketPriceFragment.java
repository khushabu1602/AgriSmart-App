package com.example.farmer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aniketjain.weatherapp.R;


public class RealtimeMarketPriceFragment extends Fragment {


    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_realtime_market_price, container, false);

        webView = view.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JS if needed
        webSettings.setDomStorageEnabled(true); // For better compatibility

        webView.setWebViewClient(new WebViewClient()); // Keeps navigation inside the WebView

        webView.loadUrl("https://agriplus.in/price/vegetables");

        return view;
    }
}
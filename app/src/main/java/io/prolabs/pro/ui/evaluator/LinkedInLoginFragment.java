package io.prolabs.pro.ui.evaluator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import io.prolabs.pro.R;

public class LinkedInLoginFragment extends DialogFragment {

    LinkedInDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (LinkedInDialogListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linkedin_login, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        WebView webView = ButterKnife.findById(view, R.id.webview);
        EditText verifierInput = ButterKnife.findById(view, R.id.verifierInput);
        Button doneButton = ButterKnife.findById(view, R.id.doneButton);

        Bundle args = getArguments();

        webView.loadUrl(args.getString(LinkedInSearchActivity.AUTH_URL));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        doneButton.setOnClickListener(v -> {
            String verificationKey = verifierInput.getText().toString();
            if (!verificationKey.equals("")) {
                listener.onDoneButtonClicked(verifierInput.getText().toString());
                dismiss();
            } else
                Toast.makeText(getActivity(),
                        "Please enter the verification key.", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    public interface LinkedInDialogListener {
        public void onDoneButtonClicked(String verificationKey);
    }
}

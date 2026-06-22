package com.example.assignment.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class FragmentUtils {

    public static void toast(Fragment fragment, String text) {
        if (fragment.getContext() != null) {
            Toast.makeText(fragment.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void snackbar(Fragment fragment, String text) {
        if (fragment.getView() != null) {
            Snackbar.make(fragment.getView(), text, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void errorDialog(Fragment fragment, String text) {
        if (fragment.getContext() != null) {
            new AlertDialog.Builder(fragment.getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Error")
                    .setMessage(text)
                    .setPositiveButton("Dismiss", null)
                    .show();
        }
    }

    public static void infoDialog(Fragment fragment, String text) {
        if (fragment.getContext() != null) {
            new AlertDialog.Builder(fragment.getContext())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Information")
                    .setMessage(text)
                    .setPositiveButton("Dismiss", null)
                    .show();
        }
    }

    public static void hideKeyboard(Fragment fragment) {
        if (fragment.getContext() != null && fragment.getView() != null) {
            InputMethodManager imm = (InputMethodManager) fragment.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(fragment.getView().getWindowToken(), 0);
        }
    }
}

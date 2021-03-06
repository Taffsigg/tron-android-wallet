package com.devband.tronwalletforandroid.ui.accountdetail.transaction;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface TransactionView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}

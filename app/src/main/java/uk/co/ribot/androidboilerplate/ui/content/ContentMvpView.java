package uk.co.ribot.androidboilerplate.ui.content;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

public interface ContentMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError(int messageId);

    void showSnackBar(String message);

}

package uk.co.ribot.androidboilerplate;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.test.common.TestDataFactory;
import uk.co.ribot.androidboilerplate.ui.content.ContentMvpView;
import uk.co.ribot.androidboilerplate.ui.content.ContentPresenter;
import uk.co.ribot.androidboilerplate.util.RxSchedulersOverrideRule;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();
    @Mock
    ContentMvpView mMockContentMvpView;
    @Mock
    DataManager mMockDataManager;
    private ContentPresenter mContentPresenter;

    @Before
    public void setUp() {
        mContentPresenter = new ContentPresenter(mMockDataManager);
        mContentPresenter.attachView(mMockContentMvpView);
    }

    @After
    public void tearDown() {
        mContentPresenter.detachView();
    }

    @Test
    public void loadRibotsReturnsRibots() {
        List<Ribot> ribots = TestDataFactory.makeListRibots(10);
        when(mMockDataManager.getRibots())
                .thenReturn(Observable.just(ribots));

        mContentPresenter.loadRibots();
        verify(mMockContentMvpView).showRibots(ribots);
        verify(mMockContentMvpView, never()).showRibotsEmpty();
        verify(mMockContentMvpView, never()).showError();
    }

    @Test
    public void loadRibotsReturnsEmptyList() {
        when(mMockDataManager.getRibots())
                .thenReturn(Observable.just(Collections.<Ribot>emptyList()));

        mContentPresenter.loadRibots();
        verify(mMockContentMvpView).showRibotsEmpty();
        verify(mMockContentMvpView, never()).showRibots(ArgumentMatchers.<Ribot>anyList());
        verify(mMockContentMvpView, never()).showError();
    }

    @Test
    public void loadRibotsFails() {
        when(mMockDataManager.getRibots())
                .thenReturn(Observable.<List<Ribot>>error(new RuntimeException()));

        mContentPresenter.loadRibots();
        verify(mMockContentMvpView).showError();
        verify(mMockContentMvpView, never()).showRibotsEmpty();
        verify(mMockContentMvpView, never()).showRibots(ArgumentMatchers.<Ribot>anyList());
    }

}

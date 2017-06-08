package com.example.lihui20.testhttp.customview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;

/**
 * 瀑布流下拉刷新
 * Created by sam on 15-8-30.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    private RecyclerView recyclerView;
    private boolean isScrollOnHeader = true;
    private boolean isScrollOnFooter = false;

    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    //  @Override
//    protected void onRefreshing(final boolean doScroll) {
//        /**
//         * If we're not showing the Refreshing view, or the list is empty, the
//         * the header/footer views won't show so we use the normal method.
//         */
////        ListAdapter adapter = mRefreshableView.getAdapter();
////        if (!mListViewExtrasEnabled || !getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
////            super.onRefreshing(doScroll);
////            return;
////        }
//
//        super.onRefreshing(true);
//
//        final LoadingLayout origLoadingView, listViewLoadingView, oppositeListViewLoadingView;
//        final int selection, scrollToY;
//
//        switch (getCurrentMode()) {
//            case MANUAL_REFRESH_ONLY:
//            case PULL_FROM_END:
////                origLoadingView = getFooterLayout();
////                listViewLoadingView = mFooterLoadingView;
////                oppositeListViewLoadingView = mHeaderLoadingView;
////                selection = mRefreshableView.getCount() - 1;
//                scrollToY = getScrollY() - getFooterSize();
//                break;
//            case PULL_FROM_START:
//            default:
////                origLoadingView = getHeaderLayout();
////                listViewLoadingView = mHeaderLoadingView;
////                oppositeListViewLoadingView = mFooterLoadingView;
////                selection = 0;
//                scrollToY = getScrollY() + getHeaderSize();
//                break;
//        }
//        setHeaderScroll(scrollToY);
//
//        // Hide our original Loading View
////        origLoadingView.reset();
////        origLoadingView.hideAllViews();
////
////        // Make sure the opposite end is hidden too
////        oppositeListViewLoadingView.setVisibility(View.GONE);
////
////        // Show the ListView Loading View and set it to refresh.
////        listViewLoadingView.setVisibility(View.VISIBLE);
////        listViewLoadingView.refreshing();
////
//        //   if (true) {
//        // We need to disable the automatic visibility changes for now
//        //     disableLoadingLayoutVisibilityChanges();
//
//        // We scroll slightly so that the ListView's header/footer is at the
//        // same Y position as our normal header/footer
//
//        // Make sure the ListView is scrolled to show the loading
//        // header/footer
//        //    mRefreshableView.setSelection(selection);
//
//        // Smooth scroll as normal
//        //  smoothScrollTo(0);
//        //   }
//    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        recyclerView = new RecyclerView(context, attrs);
        recyclerView.setId(com.example.lihui20.testhttp.R.id.recyclerview);
        final StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int[] lastVisibleItem;
            int[] fistVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {

                if (null != fistVisibleItem) {
                    isScrollOnHeader = 0 == fistVisibleItem[0];
                } else {
                    isScrollOnHeader = true;
                }

                if (null != lastVisibleItem) {
                    boolean isLast = mLayoutManager.getItemCount() - 1 == lastVisibleItem[0] || mLayoutManager.getItemCount() == lastVisibleItem[1]
                            || mLayoutManager.getItemCount() - 1 == lastVisibleItem[1] || mLayoutManager.getItemCount() == lastVisibleItem[0];
                    isScrollOnFooter = newState == RecyclerView.SCROLL_STATE_IDLE && isLast;
                } else {
                    isScrollOnFooter = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                fistVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPositions(new int[2]);
                lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPositions(new int[2]);
            }

        });
        return recyclerView;
    }


    @Override
    protected boolean isReadyForPullStart() {
        return isScrollOnHeader;
    }

    @Override
    protected boolean isReadyForPullEnd() {

        return isScrollOnFooter;
    }

}
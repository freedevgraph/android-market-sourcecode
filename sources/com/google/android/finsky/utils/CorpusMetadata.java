package com.google.android.finsky.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.finsky.R;
import com.google.android.finsky.config.G;

/* JADX INFO: loaded from: classes.dex */
public final class CorpusMetadata {
    public static int getBackendHintColor(Context context, int channelId) {
        int id;
        switch (channelId) {
            case 1:
                id = R.color.books_background;
                break;
            case 2:
            default:
                id = R.color.apps_background;
                break;
            case 3:
                id = R.color.apps_background;
                break;
            case 4:
                id = R.color.movies_background;
                break;
        }
        return context.getResources().getColor(id);
    }

    public static int getPromoShadowResource(Context context, int channelId) {
        switch (channelId) {
            case 1:
                return R.drawable.promo_shadow_books;
            case 2:
            case 3:
            default:
                return R.drawable.promo_shadow_apps;
            case 4:
                return R.drawable.promo_shadow_movies;
        }
    }

    public static Drawable getBucketEntryBackground(Context context, int channelId) {
        int id;
        switch (channelId) {
            case 1:
                id = R.drawable.product_tile_books;
                break;
            case 2:
            case 3:
            default:
                id = R.drawable.product_tile_apps;
                break;
            case 4:
                id = R.drawable.product_tile_movies;
                break;
        }
        return context.getResources().getDrawable(id);
    }

    public static Uri getHelpUrl(int channelId) {
        switch (channelId) {
            case 1:
                return Uri.parse(G.booksHelpUrl.get());
            case 2:
            default:
                return Uri.parse(G.defaultHelpUrl.get());
            case 3:
                return Uri.parse(G.appsHelpUrl.get());
            case 4:
                return Uri.parse(G.moviesHelpUrl.get());
        }
    }

    public static Uri getContactUsUrl(int channelId) {
        switch (channelId) {
            case 1:
                return Uri.parse(G.tabletBooksContactUsUrl.get());
            case 2:
            case 3:
            default:
                return Uri.parse(G.tabletDefaultContactUsUrl.get());
            case 4:
                return Uri.parse(G.tabletMoviesContactUsUrl.get());
        }
    }

    public static int getOwnedIconResource(int channelId) {
        switch (channelId) {
            case 1:
                return R.drawable.ic_owned_books;
            case 2:
            case 3:
            default:
                return R.drawable.ic_owned_apps;
            case 4:
                return R.drawable.ic_owned_movies;
        }
    }

    public static int getOwnedNotLocalIconResource(int channelId) {
        switch (channelId) {
            case 1:
                return R.drawable.ic_owned_not_local_books;
            default:
                return R.drawable.ic_owned_not_local_apps;
        }
    }

    public static int getIconResource(int channelId) {
        switch (channelId) {
            case 1:
                return R.drawable.ic_books_header;
            case 2:
            default:
                return R.drawable.icon;
            case 3:
                return R.drawable.ic_apps_header;
            case 4:
                return R.drawable.ic_movies_header;
        }
    }

    public static int getCategoryBackground(int channelId) {
        switch (channelId) {
            case 1:
                return R.drawable.background_category_books;
            case 2:
            case 3:
            default:
                return R.drawable.background_category_apps;
            case 4:
                return R.drawable.background_category_movies;
        }
    }

    public static int getFlyoverBackgroundResource(int backend) {
        switch (backend) {
            case 1:
                return R.drawable.sidecar_bkgd_books;
            case 2:
            case 3:
            default:
                return R.drawable.sidecar_bkgd_apps;
            case 4:
                return R.drawable.sidecar_bkgd_movies;
        }
    }

    public static int getMoreArrowResource(int backend) {
        switch (backend) {
            case 1:
                return R.drawable.more_button_large_circle_books;
            case 2:
            case 3:
            default:
                return R.drawable.more_button_large_circle_apps;
            case 4:
                return R.drawable.more_button_large_circle_movies;
        }
    }

    public static int getDetailsHeaderBackgroundResource(int backend) {
        switch (backend) {
            case 1:
                return R.drawable.ab_shadow_blue;
            case 2:
            case 3:
            default:
                return R.drawable.ab_shadow_green;
            case 4:
                return R.drawable.ab_shadow_red;
        }
    }

    public static int getDescriptionHeaderStringId(int backend) {
        switch (backend) {
            case 4:
                return R.string.details_synopsis;
            default:
                return R.string.details_description;
        }
    }

    public static int getReviewsHeaderStringId(int backend) {
        switch (backend) {
            case 4:
                return R.string.details_viewer_reviews;
            default:
                return R.string.details_reviews;
        }
    }

    public static int getIconWidth(Context context, int backend) {
        Resources res = context.getResources();
        switch (backend) {
            case 1:
                return res.getDimensionPixelSize(R.dimen.bucket_entry_book_icon_width);
            case 2:
            case 3:
            default:
                return res.getDimensionPixelSize(R.dimen.bucket_entry_icon_width);
            case 4:
                return res.getDimensionPixelSize(R.dimen.bucket_entry_movie_icon_width);
        }
    }

    public static int getRelatedIconWidth(Context context, int backend) {
        Resources res = context.getResources();
        switch (backend) {
            case 1:
                return res.getDimensionPixelSize(R.dimen.list_panel_basic_item_book_icon_width);
            case 2:
            case 3:
            default:
                return res.getDimensionPixelSize(R.dimen.list_panel_basic_item_icon_width);
            case 4:
                return res.getDimensionPixelSize(R.dimen.list_panel_basic_item_movie_icon_width);
        }
    }

    public static int getPromoHolographicStrip(int backend) {
        switch (backend) {
            case 1:
                return R.drawable.sideband_books;
            case 2:
            case 3:
            default:
                return R.drawable.sideband_apps;
            case 4:
                return R.drawable.sideband_movies;
        }
    }
}

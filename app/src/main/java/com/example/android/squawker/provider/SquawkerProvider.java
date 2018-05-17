package com.example.android.squawker.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(
    authority = SquawkerProvider.AUTHORITY,
    database = SquawkerDatabase.class
)
public final class SquawkerProvider {

    static final String AUTHORITY = "com.example.android.squawker.provider.provider";

    @TableEndpoint(table = SquawkerDatabase.SQUAKER)
    public static class Squawker {

        @ContentUri(
            path = "messages",
            type = "vnd.android.cursor.dir/messages",
            defaultSort = SquawkerContract.COLUMN_DATE + " DESC"
        )
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/messages");
    }
}

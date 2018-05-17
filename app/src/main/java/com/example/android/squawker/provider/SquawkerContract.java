package com.example.android.squawker.provider;

import android.content.SharedPreferences;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public class SquawkerContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_MESSAGE = "message";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR_KEY = "authorKey";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_DATE = "date";

    public static final String ASSER_KEY = "key_asser";
    public static final String CEZANNE_KEY = "key_cezanne";
    public static final String JLIN_KEY = "key_jlin";
    public static final String LYLA_KEY = "key_lyla";
    public static final String NIKITA_KEY = "key_nikita";
    public static final String TEST_ACCOUNT_KEY = "key_test";
    public static final String[] INSTRUCTOR_KEYS = {
        ASSER_KEY, CEZANNE_KEY, JLIN_KEY, LYLA_KEY, NIKITA_KEY
    };

    public static String createSelectionForCurrentFollowing(SharedPreferences preferences) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(COLUMN_AUTHOR_KEY).append(" IN ('").append(TEST_ACCOUNT_KEY).append("'");

        for (String key : INSTRUCTOR_KEYS) {
            if (preferences.getBoolean(key, false)) {
                stringBuilder.append(",");
                stringBuilder.append("'").append(key).append("'");
            }
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}

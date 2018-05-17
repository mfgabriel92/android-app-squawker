package com.example.android.squawker.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = 1)
public class SquawkerDatabase {

    @Table(SquawkerContract.class)
    public static final String SQUAKER = "squaker";
}
/** WTFContentProvider.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.eegeo.apisamples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 *
 * Created: 09/12/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class MyCustomSearchProvider extends ContentProvider {

    public static final String TAG = "m039";

    public static final String AUTHORITY = "com.eegeo.apisamples.MyCustomSearchProvider";

    private static class Account {
        public String name;
        public int dollars;
        public long id;

        public Account() {
            id = 0;
            name = "";
            dollars = 0;
        }

        public Account(long id, String name, int dollars) {
            this.id = id;
            this.name = name;
            this.dollars = dollars;
        }
    }

    static ArrayList<Account> sAccounts = new ArrayList<Account>();

    {
        long i = 0;
        sAccounts.add(new Account(i++, "Dima", 2));
        sAccounts.add(new Account(i++, "Anton", 15));
        sAccounts.add(new Account(i++, "Alexandr", 4));
        sAccounts.add(new Account(i++, "Andrey", 4));
        sAccounts.add(new Account(i++, "Roma", 1));
        sAccounts.add(new Account(i++, "Artem", 2));
    }

    public static final class Words {
        public static class Account {
            public static final String CONTENT_TYPE =
                    "vnd.android.cursor.dir" + "/" + AUTHORITY + "." + AccountColumns.ACCOUNT;

            public static final String ENTRY_CONTENT_TYPE =
                    "vnd.android.cursor.item" + "/" + AUTHORITY + "." + AccountColumns.ACCOUNT;

            public static final String DEFAULT_SORT_ORDER = AccountColumns.NAME;

            public static final Uri CONTENT_URI;

            static {
                CONTENT_URI = Uri
                        .parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/" + AccountColumns.ACCOUNT);
            }

        }

        public static interface AccountColumns extends BaseColumns {
            public static final String ACCOUNT = "account";
            public static final String NAME = "name";
            public static final String DOLLARS = "dollars";
        }
    }

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int MATCH_ACCOUNT = 1;
    private static final int MATCH_ACCOUNT_ID = 2;
    private static final int MATCH_ACCOUNT_NAME = 3;
    private static final int MATCH_ACCOUNT_DOLLARS = 4;

    static {
        sUriMatcher.addURI(AUTHORITY, Words.AccountColumns.ACCOUNT, MATCH_ACCOUNT);
        sUriMatcher.addURI(AUTHORITY, Words.AccountColumns.ACCOUNT + "/#", MATCH_ACCOUNT_ID);
        sUriMatcher.addURI(AUTHORITY, Words.AccountColumns.ACCOUNT + "/search_suggest_query/*", MATCH_ACCOUNT);
        sUriMatcher.addURI(AUTHORITY, Words.AccountColumns.ACCOUNT + "/#/" + Words.AccountColumns.DOLLARS, MATCH_ACCOUNT_DOLLARS);
    }

    @Override
    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        MatrixCursor c = new MatrixCursor(new String[] {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA
        });


        for(Account acc : sAccounts){
            List<String> segments = uri.getPathSegments();
            if(segments.size() == 3){
                String match = segments.get(2).toLowerCase();
                if(acc.name.toLowerCase().contains(match)){
                    ArrayList<Object> values = new ArrayList<Object>();
                    values.add(acc.id);
                    values.add(acc.name);
                    values.add(acc.name);
                    values.add(acc.name);
                    c.addRow(values.toArray(new Object[values.size()]));
                }
            }
        }

        return c;

        /*switch (sUriMatcher.match(uri)) {
            case MATCH_ACCOUNT:
            {
                int count = sAccounts.size();
                Cursor cursors[] = new Cursor[count];

                for(int i = 0; i < count; i++) {
                    cursors[i] = query(ContentUris.withAppendedId(uri, i),
                            projection,
                            selection,
                            selectionArgs,
                            sortOrder);
                }

                return new MergeCursor(cursors);
            }
            case MATCH_ACCOUNT_ID:
            {
                int id = (int) ContentUris.parseId(uri);

                String columns[];

                if (projection != null) {

                    HashSet<String> set = new HashSet<String>(Arrays.asList(projection));
                    set.add(Words.AccountColumns._ID);
                    columns = set.toArray(new String[set.size()]);

                } else {

                    columns = new String[] { Words.AccountColumns._ID };

                }

                MatrixCursor c = new MatrixCursor(columns);
                ArrayList<Object> values = new ArrayList<Object>();

                synchronized(sAccounts) {
                    if (id >= sAccounts.size()) {
                        throw new NullPointerException();
                    }

                    Account account = sAccounts.get(id);

                    for(String column : columns) {
                        if (column.equals(Words.AccountColumns._ID)) {
                            values.add(account.id);
                        } else if(column.equals(Words.AccountColumns.NAME)) {
                            values.add(account.name);
                        } else if(column.equals(Words.AccountColumns.DOLLARS)) {
                            values.add(account.dollars);
                        }
                    }
                }

                c.addRow(values.toArray(new Object[values.size()]));

                return c;
            }
            case MATCH_ACCOUNT_NAME:
            {
                List<String> segments = uri.getPathSegments();
                int id = Integer.parseInt(segments.get(segments.size() - 2));

                MatrixCursor c = new MatrixCursor(new String[] {
                        Words.AccountColumns._ID,
                        Words.AccountColumns.NAME
                });

                Account account;

                synchronized (sAccounts) {
                    if (id < 0 || id >= sAccounts.size()) {
                        throw new NullPointerException();
                    }

                    account = sAccounts.get(id);
                    c.addRow(new Object[] { account.id, account.name });
                }

                return c;
            }
            case MATCH_ACCOUNT_DOLLARS:
            {
                List<String> segments = uri.getPathSegments();
                int id = Integer.parseInt(segments.get(segments.size() - 2));

                MatrixCursor c = new MatrixCursor(new String[] {
                        Words.AccountColumns._ID,
                        Words.AccountColumns.DOLLARS
                });

                Account account;

                synchronized (sAccounts) {
                    if (id < 0 || id >= sAccounts.size()) {
                        throw new NullPointerException();
                    }

                    account = sAccounts.get(id);
                    c.addRow(new Object[] { account.id, account.dollars });
                }

                return c;
            }
            default:
                throw new IllegalArgumentException("Invalide URI");
        }*/
    }

    @Override
    public Uri insert (Uri uri, ContentValues values) {
        Uri result = null;

        switch (sUriMatcher.match(uri)) {
            case MATCH_ACCOUNT:
            {
                Account a = new Account();

                if (values.containsKey(Words.AccountColumns.DOLLARS)) {
                    a.dollars = values.getAsInteger(Words.AccountColumns.DOLLARS);
                }

                if (values.containsKey(Words.AccountColumns.NAME)) {
                    a.name = values.getAsString(Words.AccountColumns.NAME);
                }

                synchronized (sAccounts) {
                    sAccounts.add(a);

                    result = ContentUris.withAppendedId(uri, sAccounts.size() - 1);
                }
            }

            break;
            default:
                throw new IllegalArgumentException("Invalide URI");
        }

        // ?!

        getContext()
                .getContentResolver()
                .notifyChange(uri, null);

        return result;
    }

    @Override
    public int delete (Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (sUriMatcher.match(uri)) {
            case MATCH_ACCOUNT_ID:
            {
                int id = Integer.parseInt(uri.getLastPathSegment());

                synchronized (sAccounts) {
                    if (!(id < 0 || id >= sAccounts.size())) {
                        sAccounts.remove(id);
                        count = 1;
                    }
                }
            }
            break;
            default:
                throw new IllegalArgumentException("Invalide URI");
        }

        return count;
    }

    @Override
    public int update (Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (sUriMatcher.match(uri)) {
            case MATCH_ACCOUNT_ID:
            {
                int id = Integer.parseInt(uri.getLastPathSegment());

                synchronized (sAccounts) {

                    if (!(id < 0 || id >= sAccounts.size())) {

                        Account a = sAccounts.get(id);

                        if (values.containsKey(Words.AccountColumns.DOLLARS)) {
                            a.dollars = values.getAsInteger(Words.AccountColumns.DOLLARS);
                        }

                        if (values.containsKey(Words.AccountColumns.NAME)) {
                            a.name = values.getAsString(Words.AccountColumns.NAME);
                        }

                        count = 1;
                    }

                }

            }
            break;
            default:
                throw new IllegalArgumentException("Invalide URI");
        }

        return count;
    }

    @Override
    public boolean onCreate () {
        return true;
    }

    @Override
    public String getType (Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_ACCOUNT:
                return Words.Account.CONTENT_TYPE;
            case MATCH_ACCOUNT_ID:
                return Words.Account.ENTRY_CONTENT_TYPE;
            default:
                return null;
        }

    }

} // WTFContentProvider

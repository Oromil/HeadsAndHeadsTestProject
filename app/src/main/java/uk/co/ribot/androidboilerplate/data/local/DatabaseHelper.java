package uk.co.ribot.androidboilerplate.data.local;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.data.model.UserAccount;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        this(dbOpenHelper, Schedulers.io());
    }

    @VisibleForTesting
    public DatabaseHelper(DbOpenHelper dbOpenHelper, Scheduler scheduler) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, scheduler);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Ribot> setRibots(final Collection<Ribot> newRibots) {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) return;
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.delete(Db.RibotProfileTable.TABLE_NAME, null);
                for (Ribot ribot : newRibots) {
                    long result = mDb.insert(Db.RibotProfileTable.TABLE_NAME,
                            Db.RibotProfileTable.toContentValues(ribot.profile()),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) emitter.onNext(ribot);
                }
                transaction.markSuccessful();
                emitter.onComplete();
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<List<Ribot>> getRibots() {
        return mDb.createQuery(Db.RibotProfileTable.TABLE_NAME,
                "SELECT * FROM " + Db.RibotProfileTable.TABLE_NAME)
                .mapToList(cursor -> Ribot.create(Db.RibotProfileTable.parseCursor(cursor)));
    }

    public Observable<UserAccount> getUser(String email) {
        Observable<UserAccount> user = mDb.createQuery(Db.AccountsTable.TABLE_NAME,
                "SELECT * FROM " + Db.AccountsTable.TABLE_NAME + " WHERE " +
                        Db.AccountsTable.COLUMN_EMAIL + " = '" + email + "'")
                .mapToOneOrDefault(Db.AccountsTable::parseCursor,
                        UserAccount.getEmptyUser());
        return user;
    }

    public Observable<UserAccount> addUser(final UserAccount user) {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                long result = mDb.insert(Db.AccountsTable.TABLE_NAME,
                        Db.AccountsTable.toContentValues(user));
                if (result >= 0)
                    emitter.onNext(user);
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }
        });
    }

}

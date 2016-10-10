package com.example.arthome.newexchangeworld;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by arthome on 2016/10/10.
 */


public enum RealmManager {
    INSTANCE;

    private Realm realm = Realm.getDefaultInstance();

    public void createUser(final User user) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

    public List<User> retrieveAllKiiUsers() {
        RealmResults<User> results = realm.where(User.class)
                .findAll();
        return realm.copyFromRealm(results);
    }

    public User retrieveUser(final String facebookID) {
        User result = realm.where(User.class).equalTo("facebookID", facebookID)
                .findFirst();
        return (result == null) ? null : realm.copyFromRealm(result);
    }

    public void deleteUser(final String facebookID) {
        final RealmResults<User> results = realm.where(User.class).equalTo("facebookID", facebookID)
                .findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }
}

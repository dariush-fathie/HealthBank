package pro.ahoora.zhin.healthbank;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("database.realm")
                //.encryptionKey(getKey())
                .schemaVersion(1)
                //.modules(new MySchemaModule())
                //.migration(new MyMigration())
                .build();


        Realm.setDefaultConfiguration(config);

        FirebaseAnalytics.getInstance(this);
    }
}

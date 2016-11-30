package com.yermakov.xplatform.lesson5;

import android.app.Application;

public class LessonFiveApplication extends Application {

    public LessonFiveApplication() {
        super();
        try {
            ListSQLiteOpenHelper.createInstance(this);
        } catch (Exception ignored) { }
    }

}

package com.atakmap.app.rest.aidlexample;
import com.atakmap.app.rest.aidlexample.MainObject;

interface IMainService {
    MainObject[] listFiles(String path);

    void exit();
}

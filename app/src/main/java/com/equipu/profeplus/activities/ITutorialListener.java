package com.equipu.profeplus.activities;

import com.equipu.profeplus.models.Tutorial;

/**
 * Created by Herbert Caller on 24/09/2016.
 */

public interface ITutorialListener {

    void nextPage(Tutorial tutorial);

    void endRead(Tutorial tutorial);

}

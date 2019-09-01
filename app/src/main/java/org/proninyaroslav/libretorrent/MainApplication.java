/*
 * Copyright (C) 2016-2019 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of LibreTorrent.
 *
 * LibreTorrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreTorrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreTorrent.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.proninyaroslav.libretorrent;

import androidx.multidex.MultiDexApplication;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;
import org.libtorrent4j.swig.libtorrent;
import org.proninyaroslav.libretorrent.core.TorrentNotifier;
import org.proninyaroslav.libretorrent.core.model.TorrentEngine;
import org.proninyaroslav.libretorrent.core.system.filesystem.LibTorrentSafAdapter;
import org.proninyaroslav.libretorrent.core.utils.Utils;
import org.proninyaroslav.libretorrent.ui.errorreport.ErrorReportActivity;

@AcraCore(buildConfigClass = BuildConfig.class)
@AcraMailSender(mailTo = "proninyaroslav@mail.ru")
@AcraDialog(reportDialogClass = ErrorReportActivity.class)

public class MainApplication extends MultiDexApplication
{
    @SuppressWarnings("unused")
    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate()
    {
        super.onCreate();

        Utils.migrateTray2SharedPreferences(this);
        ACRA.init(this);

        LibTorrentSafAdapter adapter = new LibTorrentSafAdapter(this);
        adapter.swigReleaseOwnership();
        libtorrent.set_posix_wrapper(adapter);

        TorrentNotifier.getInstance(this).makeNotifyChans();
        TorrentEngine.getInstance(this).start();
    }
}
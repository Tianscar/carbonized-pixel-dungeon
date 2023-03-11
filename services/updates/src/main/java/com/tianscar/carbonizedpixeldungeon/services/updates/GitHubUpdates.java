/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.tianscar.carbonizedpixeldungeon.services.updates;


import com.tianscar.pixeldungeonclasses.noosa.Game;
import com.tianscar.pixeldungeonclasses.utils.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.tianscar.pixeldungeonclasses.utils.GameSettings;

import javax.net.ssl.SSLProtocolException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubUpdates extends UpdateService {

	private static Pattern descPattern = Pattern.compile("<!-- DESC_BEGIN -->(.*?)<!-- DESC_END -->", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static Pattern descPatternZH = Pattern.compile("<!-- DESC_BEGIN_ZH -->(.*?)<!-- DESC_END_ZH -->", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static Pattern descPatternTC = Pattern.compile("<!-- DESC_BEGIN_TC -->(.*?)<!-- DESC_END_TC -->", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static Pattern versionCodePattern = Pattern.compile("VERSION_CODE: ([0-9]*)", Pattern.CASE_INSENSITIVE);

	@Override
	public boolean isUpdateable() {
		return true;
	}

	@Override
	public boolean supportsBetaChannel() {
		return true;
	}

	@Override
	public void checkForUpdate(boolean useMetered, boolean includeBetas, UpdateResultCallback callback) {

		if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()){
			callback.onConnectionFailed();
			return;
		}

		Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
		httpGet.setUrl("https://api.github.com/repos/AnsdoShip/carbonized-pixel-dungeon/releases");
		httpGet.setHeader("Accept", "application/vnd.github.v3+json");

		Gdx.net.sendHttpRequest(httpGet, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				try {
					Bundle latestRelease = null;
					int latestVersionCode = Game.versionCode;

					for (Bundle b : Bundle.read( httpResponse.getResultAsStream() ).getBundleArray()){
						Matcher m = versionCodePattern.matcher(b.getString("body"));

						if (m.find()){
							int releaseVersion = Integer.parseInt(m.group(1));
							if (releaseVersion > latestVersionCode
									&& (includeBetas || !b.getBoolean("prerelease"))){
								latestRelease = b;
								latestVersionCode = releaseVersion;
							}
						}

					}

					if (latestRelease == null){
						callback.onNoUpdateFound();
					} else {

						AvailableUpdateData update = new AvailableUpdateData();

						update.versionName = latestRelease.getString("name");
						update.versionCode = latestVersionCode;
						Matcher m;
						Locale defaultLocale = Locale.getDefault();
						if (defaultLocale.getLanguage().equals("zh")) {
							if (defaultLocale.getCountry().equals("HK") || defaultLocale.getCountry().equals("MO") || defaultLocale.getCountry().equals("TW")) {
								defaultLocale = new Locale("tc");
							}
							else defaultLocale = new Locale("zh");
						}
						String lang = GameSettings.getString("language", defaultLocale.getLanguage());
						if ("zh".equals(lang)) {
							m = descPatternZH.matcher(latestRelease.getString("body"));
						}
						else if ("tc".equals(lang)) {
							m = descPatternTC.matcher(latestRelease.getString("body"));
						}
						else {
							m = descPattern.matcher(latestRelease.getString("body"));
						}
						m.find();
						update.desc = m.group(1).trim().replaceAll("- ", "_-_ ");
						update.URL = latestRelease.getString("html_url");

						callback.onUpdateAvailable(update);
					}
				} catch (Exception e) {
					Game.reportException( e );
					callback.onConnectionFailed();
				}
			}

			@Override
			public void failed(Throwable t) {
				//Failure in SSL handshake, possibly because GitHub requires TLS 1.2+.
				// Often happens for old OS versions with outdated security protocols.
				// Future update attempts won't work anyway, so just pretend nothing was found.
				if (t instanceof SSLProtocolException){
					callback.onNoUpdateFound();
				} else {
					Game.reportException(t);
					callback.onConnectionFailed();
				}
			}

			@Override
			public void cancelled() {
				callback.onConnectionFailed();
			}
		});

	}

	@Override
	public void initializeUpdate(AvailableUpdateData update) {
		Game.platform.openURI( update.URL );
	}

	@Override
	public boolean isInstallable() {
		return false;
	}

	@Override
	public void initializeInstall() {
		//does nothing, always installed
	}

	@Override
	public boolean supportsReviews() {
		return false;
	}

	@Override
	public void initializeReview(ReviewResultCallback callback) {
		//does nothing, no review functionality here
		callback.onComplete();
	}

	@Override
	public void openReviewURI() {
		//does nothing
	}
}

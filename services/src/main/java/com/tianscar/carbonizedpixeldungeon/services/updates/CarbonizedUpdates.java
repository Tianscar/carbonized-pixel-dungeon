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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.utils.GameSettings;

import javax.net.ssl.SSLProtocolException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarbonizedUpdates extends UpdatesService {

	private static final Pattern descPattern = Pattern.compile("<!-- DESC_BEGIN -->(.*?)<!-- DESC_END -->", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static final Pattern descPatternZH = Pattern.compile("<!-- DESC_BEGIN_ZH -->(.*?)<!-- DESC_END_ZH -->", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static final Pattern descPatternTC = Pattern.compile("<!-- DESC_BEGIN_TC -->(.*?)<!-- DESC_END_TC -->", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static final Pattern versionNamePattern = Pattern.compile("VERSION_NAME: (v[0-9A-Z.\\-]*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern versionCodePattern = Pattern.compile("VERSION_CODE: ([0-9]*)", Pattern.CASE_INSENSITIVE);

	@Override
	public boolean isUpdateable() {
		return true;
	}

	@Override
	public boolean supportsBetaChannel() {
		return true;
	}

	@Override
	public void checkForUpdate(boolean useMetered, boolean forceHTTPS, boolean includeBetas, UpdateResultCallback callback) {

		if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()){
			callback.onConnectionFailed();
			return;
		}

		String updateURL = forceHTTPS ? "https://carbonizedpd.tianscar.com/release" : "http://carbonizedpd.tianscar.com/release";

		Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
		httpGet.setUrl(updateURL);

		Gdx.net.sendHttpRequest(httpGet, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				String html = httpResponse.getResultAsString();
				try {
					String latestVersionName = null;
					int latestVersionCode = Game.versionCode;
					Matcher m = versionNamePattern.matcher(html);
					if (m.find()) {
						latestVersionName = m.group(1);
						if (!includeBetas && (latestVersionName.contains("ALPHA") || latestVersionName.contains("BETA") || latestVersionName.contains("RC") || latestVersionName.contains("INDEV"))) {
							callback.onNoUpdateFound();
							return;
						}
					}
					m = versionCodePattern.matcher(html);
					if (m.find()) {
						int releaseVersionCode = Integer.parseInt(m.group(1));
						if (releaseVersionCode > latestVersionCode) latestVersionCode = releaseVersionCode;
						else {
							callback.onNoUpdateFound();
							return;
						}
					}

					AvailableUpdateData update = new AvailableUpdateData();

					update.versionName = latestVersionName;
					update.versionCode = latestVersionCode;
					Locale defaultLocale = Locale.getDefault();
					if (defaultLocale.getLanguage().equals("zh")) {
						if (defaultLocale.getCountry().equals("HK") || defaultLocale.getCountry().equals("MO") || defaultLocale.getCountry().equals("TW")) {
							defaultLocale = new Locale("tc");
						}
						else defaultLocale = new Locale("zh");
					}
					String lang = GameSettings.getString("language", defaultLocale.getLanguage());
					if ("zh".equals(lang)) {
						m = descPatternZH.matcher(html);
					}
					else if ("tc".equals(lang)) {
						m = descPatternTC.matcher(html);
					}
					else {
						m = descPattern.matcher(html);
					}
					m.find();
					update.desc = m.group(1).trim()
							.replace("\r\n", "\n")
							.replace("\n<ul>\n", "")
							.replace("\n<ul>", "")
							.replace("<ul>\n", "")
							.replace("\n</ul>\n", "")
							.replace("\n</ul>", "")
							.replace("</ul>\n", "")
							.replace("<ul>", "")
							.replace("</ul>", "")
							.replace("<li>", "- ")
							.replace("</li>", "")
							.replace("-", "_-_");
					update.URL = updateURL;

					callback.onUpdateAvailable(update);
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
				if (t instanceof SSLProtocolException) {
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

/*
 ** Copyright (C) 2019 KunoiSayami
 **
 ** This file is part of Fluffy and is released under
 ** the AGPL v3 License: https://www.gnu.org/licenses/agpl-3.0.txt
 **
 ** This program is free software: you can redistribute it and/or modify
 ** it under the terms of the GNU Affero General Public License as published by
 ** the Free Software Foundation, either version 3 of the License, or
 ** any later version.
 **
 ** This program is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 ** GNU Affero General Public License for more details.
 **
 ** You should have received a copy of the GNU Affero General Public License
 ** along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package moe.fluffy.app.types;

import androidx.annotation.DrawableRes;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleType {
	public String title, author, date, body;
	public String category;
	@DrawableRes int coverId, headerId;
	public ArticleType(JSONObject j, @DrawableRes int cover, @DrawableRes int header) throws JSONException {
		title = j.getString("title");
		author = j.getString("author").split("/")[0];
		date = j.getString("date");
		body = j.getString("body");
		category = j.getString("category");
		coverId = cover;
		headerId = header;
	}


	public int getCoverId() {
		return coverId;
	}

	public int getHeaderId() {
		return headerId;
	}
}

package moe.fluffy.app.types;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ArticlesMap {
	private static ArticlesMap instance = null;

	private ArrayList<Article> l;

	ArticlesMap() {
		l = new ArrayList<>();
	}

	@NonNull
	public static ArticlesMap getInstance(){
		if (instance == null) {
			instance = new ArticlesMap();
		}
		return instance;
	}

	public void push(Article a) {
		l.add(a);
	}

	public ArrayList<Article> getList() {
		return l;
	}
}

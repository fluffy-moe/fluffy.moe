package moe.fluffy.app.types;

import java.util.ArrayList;

public class ArticlesMap {
	private static ArticlesMap instance = null;

	private ArrayList<ArticleType> l;

	ArticlesMap() {
		l = new ArrayList<>();
	}

	public static ArticlesMap getInstance(){
		if (instance == null) {
			instance = new ArticlesMap();
		}
		return instance;
	}

	public void push(ArticleType a) {
		l.add(a);
	}

	public ArrayList<ArticleType> getList() {
		return l;
	}
}

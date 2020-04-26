package moe.fluffy.app.assistant;

import java.util.Random;

public class RandomAssistant {
	private static RandomAssistant instance;

	Random r;
	RandomAssistant() {
		r = new Random();
	}

	public String choiceRandomString(final String[] szRandom) {
		return szRandom[r.nextInt(szRandom.length)];
	}

	public static RandomAssistant getInstance() {
		if (instance == null)
			instance = new RandomAssistant();
		return instance;
	}
}

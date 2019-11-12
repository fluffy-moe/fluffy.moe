package moe.fluffy.app.types;

public class Datetime extends Date {
	int hour, minute, second, millionSecond;
	Datetime(int _year, int _month, int _day, int _hour, int _minute, int _second, int _millionSecond) {
		super(_year, _month, _day);
		hour = _hour;
		minute = _minute;
		second = _second;
		millionSecond = _millionSecond;
	}
}

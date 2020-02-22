/*
 ** Copyright (C) 2019-2020 KunoiSayami
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
package moe.fluffy.app.assistant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import moe.fluffy.app.R;

public class BloodTestDrawable extends Drawable {

	public static class ScaleRect {

		private static final String TAG = "log_ScaleRect";

		private double valueScale;
		boolean isNegativeNumber, isSuperNumber;
		private final static float scaleBase = 0.33f;

		ScaleRect(double value, double referenceDown, double referenceUp) {
			if (referenceUp < referenceDown)
				throw new IllegalArgumentException("referenceUp should more than referenceDown");
			valueScale = Math.abs(value - referenceDown) / (referenceUp - referenceDown);
			//Log.v(TAG, "ScaleRect: referenceDown => "+ referenceDown + " referenceUp => " + referenceUp + " value => " + value);
			//Log.v(TAG, "ScaleRect: valueScare => " + valueScale);
			isNegativeNumber = value < referenceDown;
			isSuperNumber = value > referenceUp;
			if (isSuperNumber) {
				if (valueScale > (1 + scaleBase))
					valueScale = (1 + scaleBase);
				valueScale = valueScale - (int)valueScale;
			}
			if ((isNegativeNumber || isSuperNumber) && valueScale > scaleBase) {
				valueScale = scaleBase;
			}
			if (isNegativeNumber)
				valueScale = scaleBase - valueScale;
			else if (isSuperNumber)
				valueScale += (1 + scaleBase);
			else
				valueScale += scaleBase;
			//Log.v(TAG, "ScaleRect: valueScare => " + valueScale);
			//Log.v(TAG, "ScaleRect: is super number => " + isSuperNumber + ", is negative number => " + isNegativeNumber);
		}

		private static int getWidth(int width) {
			return (int) Math.ceil(0.0366f * (double) width);
		}

		private int getLeft(int width) {
			int left = (int) Math.ceil((double) width / (1 + scaleBase * 2) * valueScale);
			if (left < (getWidth(width))) {
				left = getWidth(width) / 2;
			}
			if (left + getWidth(width) > width) {
				left = width - getWidth(width) / 2;
			}
			return left;
		}

		Rect getRect(int width, int height) {
			//Log.v(TAG, "getRect: getWidth => " + getWidth(width) + " getLeft => " + getLeft(width));
			int rectWidth = getWidth(width);
			return new Rect(getLeft(width) - (rectWidth / 2), 0, getLeft(width) + rectWidth / 2, height);
			//return rect;
		}

		public static ScaleRect build(double value, double referenceDown, double referenceUp) {
			return new ScaleRect(value, referenceDown, referenceUp);
		}
	}

	// TODO: make static
	private final Paint targetPaint, writePaint;

	private ScaleRect scaleRect;

	private BloodTestDrawable(Context context) {
		// Set up color and text size
		targetPaint = new Paint();
		targetPaint.setColor(context.getColor(R.color.colorMedicalTarget));
		writePaint = new Paint();
		writePaint.setColor(context.getColor(R.color.colorMedicalBreak));
	}

	public BloodTestDrawable(Context context, double value, double referenceDown, double referenceUp) {
		this(context);
		try {
			scaleRect = new ScaleRect(value, referenceDown, referenceUp);
		} catch (IllegalArgumentException e) {
			PopupDialog.build(context, e);
			scaleRect = new ScaleRect(value, referenceUp, referenceDown);
		}
	}

	public BloodTestDrawable(Context context, ScaleRect sc) {
		this(context);
		scaleRect = sc;
	}

	private static int getLeft(int width) {
		return (int) Math.ceil(((float)width * 0.2f));
	}

	private static int getRectWidth(int width) {
		return (int) Math.ceil(((float)width * 0.02f));
	}

	private static int getRight(int width) {
		return getRightBottom(width) - getRectWidth(width);
	}

	private static int getRightBottom(int width) {
		return width - getLeft(width);
	}


	@Override
	public void draw(Canvas canvas) {
		// TODO: performance problem
		// Get the drawable's bounds
		int width = getBounds().width();
		int height = getBounds().height();
		//float radius = Math.min(width, height) / 2;
		Rect rLeft = new Rect(getLeft(width), 0, getLeft(width) + getRectWidth(width), height),
				rRight = new Rect(getRight(width), 0, getRightBottom(width), height);
		canvas.drawRect(rLeft, writePaint);
		canvas.drawRect(rRight, writePaint);
		canvas.drawRect(scaleRect.getRect(width, height), targetPaint);
	}

	@Override
	public void setAlpha(int alpha) {
		// This method is required
	}


	@Override
	public void setColorFilter(@Nullable ColorFilter colorFilter) {

	}

	@Override
	public int getOpacity() {
		// Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
		return PixelFormat.OPAQUE;
	}
}

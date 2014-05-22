package ui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;



public class TextViewRobotoBlack extends TextView {

	/**
	 * Font file in assets folder
	 */
	public static final String FONT_LOCATION = "Roboto-Black.ttf";
	
	private static Typeface sTypeface;
	
	public TextViewRobotoBlack(Context context) {
		super(context);
		init(context);
	}
	
	public TextViewRobotoBlack(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public TextViewRobotoBlack(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * Initializes text view
	 * @param context
	 */
	private void init(Context context) {
		if(isInEditMode()) {
			if(TextUtils.isEmpty(getText())) {
				setText("Roboto thin");
			}
			return;
		}
		setTypeface(getTypeface(context));
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Typeface getTypeface(Context context) {
		if(sTypeface == null) {
			sTypeface =  Typeface.createFromAsset(context.getAssets(), FONT_LOCATION);
		}
		return sTypeface;
	}
	
	
}
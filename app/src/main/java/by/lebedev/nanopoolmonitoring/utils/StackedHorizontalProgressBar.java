
package by.lebedev.nanopoolmonitoring.utils;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class StackedHorizontalProgressBar extends ProgressBar {
    private Paint paint;
    int primary_progress;
    int max_value;

    public StackedHorizontalProgressBar(Context context) {
        super(context);
        this.init();
    }

    public StackedHorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public synchronized void setMax(int max) {
        this.max_value = max;
        super.setMax(max);
    }

    public synchronized void setProgress(int progress) {
        if (progress > this.max_value) {
            progress = this.max_value;
        }

        this.primary_progress = progress;
        super.setProgress(progress);
    }

    public synchronized void setSecondaryProgress(int secondaryProgress) {
        if (this.primary_progress + secondaryProgress > this.max_value) {
            secondaryProgress = this.max_value - this.primary_progress;
        }

        super.setSecondaryProgress(this.primary_progress + secondaryProgress);
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setColor(-16777216);
        this.primary_progress = 0;
        this.max_value = 100;
    }
}

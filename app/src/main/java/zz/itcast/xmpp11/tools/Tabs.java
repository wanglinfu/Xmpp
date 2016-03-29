package zz.itcast.xmpp11.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tabs {
	// Alt+Shift+S 抽取方法
	private int curr = 0;
	private List<TextView> tabs = new ArrayList<TextView>();

	public static interface OnTabClickListener {
		public void onItemClick(int position);
	}

	private OnTabClickListener listener;

	public void setOnTabClickListener(OnTabClickListener listener) {
		this.listener = listener;
	}

	public void createTabs(LinearLayout layout, int tabView, String[] lables, int[] icons) throws NotFoundException {

		WindowManager wm = (WindowManager) layout.getContext().getSystemService(Context.WINDOW_SERVICE);
		Display dp = wm.getDefaultDisplay();
		final int width = dp.getWidth() / lables.length;
		for (int i = 0; i < icons.length; i++) {
			TextView view = (TextView) View.inflate(layout.getContext(), tabView, null);
			// 文字
			view.setText(lables[i]);
			Drawable top = layout.getContext().getResources().getDrawable(icons[i]);
			// CompoundDrawablesWithIntrinsicBounds text四个方位
			view.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
			view.setId(i);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = v.getId();
					// 选中-- 》默认
					tabs.get(curr).setEnabled(true);
					tabs.get(id).setEnabled(false);
					// 默认 选中-- 》
					curr = id;
					if (listener != null) {
						listener.onItemClick(curr);
					}
				}
			});
			tabs.add(view);
			layout.addView(view, p);
		}
		tabs.get(curr).setEnabled(false);
	}

	public void changeTab(int position) {
		// 选中-- 》默认
		tabs.get(curr).setEnabled(true);
		tabs.get(position).setEnabled(false);
		// 默认 选中-- 》
		curr = position;
	}
}

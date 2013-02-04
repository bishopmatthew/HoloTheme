package com.airlocksoftware.holo.picker.share;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/** Model for a Share Intent dialog - holds app name, app icon, and the app's package name. **/
public class ShareItem {

	public Drawable icon;
	public String label;
	public String packageName;

	public ShareItem() {
	}

	public static List<ShareItem> getShareItems(Context context, Intent intent) {

		List<ShareItem> items = new ArrayList<ShareItem>();
		List<ResolveInfo> resInfo = context.getPackageManager()
																				.queryIntentActivities(intent, 0);

		if (!resInfo.isEmpty()) {
			for (ResolveInfo resolveInfo : resInfo) {
				ShareItem item = new ShareItem();

				String packageName = resolveInfo.activityInfo.packageName;
				PackageManager manager = context.getPackageManager();

				item.packageName = packageName;
				item.label = resolveInfo.loadLabel(manager)
																.toString();
				item.icon = resolveInfo.loadIcon(manager);
				items.add(item);
			}
		}

		return items;
	}

}

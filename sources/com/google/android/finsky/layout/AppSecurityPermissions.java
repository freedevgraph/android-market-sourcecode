package com.google.android.finsky.layout;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.PurchaseDetailsAdapter;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.finsky.utils.Lists;
import com.google.android.finsky.utils.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes.dex */
public class AppSecurityPermissions extends FrameLayout {
    private Context mContext;
    private View mFooterView;
    private TextView mHeaderView;
    private boolean mOnlyShowDangerous;
    private PackageManager mPackageManager;

    public AppSecurityPermissions(Context context) {
        super(context);
    }

    public AppSecurityPermissions(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppSecurityPermissions(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bindInfo(List<PermissionInfo> permissions) {
        this.mContext = getContext();
        this.mPackageManager = this.mContext.getPackageManager();
        final ListView listView = (ListView) findViewById(R.id.permissions_list);
        listView.setOverScrollMode(2);
        this.mOnlyShowDangerous = true;
        final AppPermissionAdapter permissionAdapter = new AppPermissionAdapter(this.mContext, permissions);
        this.mHeaderView = (TextView) LayoutInflater.from(this.mContext).inflate(R.layout.purchase_details_title, (ViewGroup) null);
        if (permissionAdapter.getCount() == 0) {
            this.mHeaderView.setText(this.mContext.getString(R.string.no_dangerous_permissions));
        } else {
            this.mHeaderView.setText(this.mContext.getString(R.string.permissions_title));
        }
        listView.addHeaderView(this.mHeaderView);
        if (permissionAdapter.hasMorePermissions()) {
            this.mFooterView = LayoutInflater.from(this.mContext).inflate(R.layout.permissions_footer, (ViewGroup) null);
            this.mFooterView.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.layout.AppSecurityPermissions.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    AppSecurityPermissions.this.mOnlyShowDangerous = false;
                    AppSecurityPermissions.this.mHeaderView.setText(AppSecurityPermissions.this.mContext.getString(R.string.permissions_title));
                    listView.removeFooterView(AppSecurityPermissions.this.mFooterView);
                    permissionAdapter.notifyDataSetChanged();
                }
            });
            listView.addFooterView(this.mFooterView);
        }
        listView.setAdapter((ListAdapter) permissionAdapter);
    }

    private class AppPermissionAdapter extends PurchaseDetailsAdapter {
        private List<PurchaseDetailsAdapter.DetailsEntry> mDangerousList;
        private Map<String, Set<String>> mDangerousMap;
        private Map<String, Set<String>> mNormalMap;
        private List<PurchaseDetailsAdapter.DetailsEntry> mTotalList;

        AppPermissionAdapter(Context context, List<PermissionInfo> permissions) {
            super(context);
            this.mDangerousMap = Maps.newHashMap();
            this.mNormalMap = Maps.newHashMap();
            for (PermissionInfo permission : permissions) {
                PermissionGroupInfo group = null;
                try {
                    group = AppSecurityPermissions.this.mPackageManager.getPermissionGroupInfo(permission.group, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    FinskyLog.e("Invalid group name:" + permission.group, new Object[0]);
                }
                String groupLabel = group == null ? AppSecurityPermissions.this.mContext.getString(R.string.default_permission_group) : group.loadLabel(AppSecurityPermissions.this.mPackageManager).toString();
                String label = permission.loadLabel(AppSecurityPermissions.this.mPackageManager).toString();
                if (permission.protectionLevel == 1) {
                    if (!this.mDangerousMap.containsKey(groupLabel)) {
                        this.mDangerousMap.put(groupLabel, new TreeSet());
                    }
                    this.mDangerousMap.get(groupLabel).add(label);
                } else {
                    if (!this.mNormalMap.containsKey(groupLabel)) {
                        this.mNormalMap.put(groupLabel, new TreeSet());
                    }
                    this.mNormalMap.get(groupLabel).add(label);
                }
            }
            this.mDangerousList = Lists.newArrayList();
            aggregatePermissions(this.mDangerousMap, this.mDangerousList);
            this.mTotalList = Lists.newArrayList();
            aggregatePermissions(this.mDangerousMap, this.mTotalList);
            aggregatePermissions(this.mNormalMap, this.mTotalList);
        }

        private void aggregatePermissions(Map<String, Set<String>> permissionsMap, List<PurchaseDetailsAdapter.DetailsEntry> permissionsList) {
            for (String group : permissionsMap.keySet()) {
                String joinedPermissions = TextUtils.join(", ", permissionsMap.get(group));
                if (!TextUtils.isEmpty(joinedPermissions)) {
                    joinedPermissions = joinedPermissions.substring(0, 1).toUpperCase() + joinedPermissions.substring(1);
                }
                PurchaseDetailsAdapter.DetailsEntry newEntry = new PurchaseDetailsAdapter.DetailsEntry();
                newEntry.headerText = group;
                newEntry.contentText = joinedPermissions;
                permissionsList.add(newEntry);
            }
        }

        public boolean hasMorePermissions() {
            return !this.mNormalMap.isEmpty();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return AppSecurityPermissions.this.mOnlyShowDangerous ? this.mDangerousList.size() : this.mTotalList.size();
        }

        @Override // com.google.android.finsky.adapters.PurchaseDetailsAdapter, android.widget.Adapter
        public PurchaseDetailsAdapter.DetailsEntry getItem(int position) {
            return AppSecurityPermissions.this.mOnlyShowDangerous ? this.mDangerousList.get(position) : this.mTotalList.get(position);
        }
    }
}

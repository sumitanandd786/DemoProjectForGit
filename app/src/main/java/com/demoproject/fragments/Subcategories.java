package com.demoproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TableLayout;
import com.demoproject.R;
import com.demoproject.adapters.SubcategoryGridAdapter;
import com.demoproject.interfaces.ShowBackButton;
import com.demoproject.interfaces.ToolbarTitle;
import com.demoproject.pojo.Category;
import com.demoproject.utils.Constants;
import java.util.List;

public class Subcategories extends Fragment {
    static final boolean $assertionsDisabled = (!Subcategories.class.desiredAssertionStatus());
    ChildCategories childCategoriesCallback;
    ShowBackButton showBackButtonCallback;
    ToolbarTitle toolbarTitleCallback;

    public interface ChildCategories {
        void saveChildCategories(List<Category> list);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_list, container, false);
        ((TableLayout) view.findViewById(R.id.sortFilter)).setVisibility(8);
        Bundle args = getArguments();
        if ($assertionsDisabled || args != null) {
            List<Category> childCategories = (List) args.getSerializable(Constants.CAT_KEY);
            this.childCategoriesCallback.saveChildCategories(childCategories);
            this.showBackButtonCallback.showBackButton();
            this.toolbarTitleCallback.setToolbarTitle(args.getString(Constants.TITLE));
            this.toolbarTitleCallback.saveSubcategoryTitle(args.getString(Constants.TITLE));
            GridView gv = (GridView) view.findViewById(R.id.productsGrid);
            if ($assertionsDisabled || childCategories != null) {
                if (childCategories.size() >= 3) {
                    gv.setNumColumns(3);
                } else if (childCategories.size() >= 2) {
                    gv.setNumColumns(2);
                } else {
                    gv.setNumColumns(1);
                }
                gv.setAdapter(new SubcategoryGridAdapter(getActivity(), childCategories));
                return view;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.childCategoriesCallback = (ChildCategories) context;
        this.toolbarTitleCallback = (ToolbarTitle) context;
        this.showBackButtonCallback = (ShowBackButton) context;
    }
}

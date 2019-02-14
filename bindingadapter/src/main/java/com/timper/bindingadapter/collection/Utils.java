package com.timper.bindingadapter.collection;

import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.view.View;
import androidx.annotation.LayoutRes;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import com.timper.bindingadapter.collection.BindingCollectionAdapter;
import com.timper.bindingadapter.collection.ItemBinding;
import java.lang.reflect.Field;

/**
 * Helper databinding utilities. May be made public some time in the future if they prove to be
 * useful.
 */
public class Utils {
    private static final String TAG = "BCAdapters";

    @Nullable
    private static Field lifecycleOwnerField;
    private static boolean fieldFaild;

    /**
     * Helper to throw an exception when {@link androidx.databinding.ViewDataBinding#setVariable(int,
     * Object)} returns false.
     */
    public static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes) {
        Context context = binding.getRoot().getContext();
        Resources resources = context.getResources();
        String layoutName = resources.getResourceName(layoutRes);
        String bindingVariableName = DataBindingUtil.convertBrIdToString(bindingVariable);
        throw new IllegalStateException("Could not bind variable '" + bindingVariableName + "' in layout '" + layoutName + "'");
    }

    /**
     * Returns the lifecycle owner associated with the given view. This currently requires the view
     * to use databinding and uses reflection. This will hopefully be replaced with a better
     * implementation once https://issuetracker.google.com/issues/112929938 gets implemented.
     */
    @Nullable
    @MainThread
    public static LifecycleOwner findLifecycleOwner(View view) {
        ViewDataBinding binding = DataBindingUtil.findBinding(view);
        if (binding == null) {
            return null;
        }
        return getLifecycleOwner(binding);
    }

    /**
     * Returns the lifecycle owner from a {@code ViewDataBinding} using reflection.
     */
    @Nullable
    @MainThread
    private static LifecycleOwner getLifecycleOwner(ViewDataBinding binding) {
        if (!fieldFaild && lifecycleOwnerField == null) {
            try {
                lifecycleOwnerField = ViewDataBinding.class.getDeclaredField("mLifecycleOwner");
                lifecycleOwnerField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                fieldFaild = true;
                return null;
            }
        }
        if (lifecycleOwnerField == null) {
            return null;
        }
        try {
            return (LifecycleOwner) lifecycleOwnerField.get(binding);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Ensures the call was made on the main thread. This is enforced for all ObservableList change
     * operations.
     */
    public static void ensureChangeOnMainThread() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("You must only modify the ObservableList on the main thread.");
        }
    }

    /**
     * Constructs a binding adapter class from it's class name using reflection.
     */
    @SuppressWarnings("unchecked")
    public static <T, A extends BindingCollectionAdapter<T>> A createClass(Class<? extends BindingCollectionAdapter> adapterClass, ItemBinding<T> itemBinding) {
        try {
            return (A) adapterClass.getConstructor(ItemBinding.class).newInstance(itemBinding);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }
}

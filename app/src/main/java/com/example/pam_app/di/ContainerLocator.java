package com.example.pam_app.di;

import android.content.Context;

public class ContainerLocator {

    private static Container container;

    private ContainerLocator() {
        // empty
    }

    public static Container locateComponent(final Context context) {
        if (container == null) {
            setComponent(new ProductionContainer(context));
        }

        return container;
    }

    public static void setComponent(final Container container) {
        ContainerLocator.container = container;
    }
}

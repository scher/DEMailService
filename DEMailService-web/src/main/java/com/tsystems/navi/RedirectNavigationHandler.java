package com.tsystems.navi;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 23.03.13
 * Time: 20:33
 * To change this template use File | Settings | File Templates.
 */
import java.util.Map;
import java.util.Set;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

public class RedirectNavigationHandler extends ConfigurableNavigationHandler {

    private NavigationHandler parent;

    public RedirectNavigationHandler(NavigationHandler parent) {
        this.parent = parent;
    }

    @Override
    public void handleNavigation(FacesContext context, String from, String outcome) {
        if (!outcome.endsWith("?faces-redirect=true")) {
            outcome += "?faces-redirect=true";
        }

        parent.handleNavigation(context, from, outcome);
    }

    @Override
    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
        if (parent instanceof ConfigurableNavigationHandler) {
            return ((ConfigurableNavigationHandler) parent).getNavigationCase(context, fromAction, outcome);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
        if (parent instanceof ConfigurableNavigationHandler) {
            return ((ConfigurableNavigationHandler) parent).getNavigationCases();
        } else {
            return null;
        }
    }

}

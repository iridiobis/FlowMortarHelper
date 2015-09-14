package es.iridiobis.flowmortarhelper.flow;

import flow.path.Path;

/**
 * TODO: Add a class header comment!
 *
 * @author jlbe
 * @since ${VERSION}
 */
public interface CanShowScreen {
    <S extends Path> void showScreen(S screen);
    <S extends Path> void cleanAndShowScreen(S screen);
    void showInitial();
    boolean goBack();
}

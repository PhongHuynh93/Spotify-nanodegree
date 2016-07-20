/*
base view for all view (in my project, frag is a view)
 */
package dhbk.android.spotifygcs;

public interface BaseView<T> {
    // every views (frag) must set it's presenter
    void setPresenter(T presenter);
}

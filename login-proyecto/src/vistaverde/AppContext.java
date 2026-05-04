package vistaverde;

import vistaverde.model.Condominio;

/**
 * Singleton holding the shared Condominio instance.
 * All screens access the same data through AppContext.getInstance().
 */
public class AppContext {

    private static AppContext instance;
    private final Condominio condominio;

    private AppContext() {
        this.condominio = new Condominio();
    }

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public Condominio getCondominio() {
        return condominio;
    }
}

package domain.model.classDiscovery;

import java.util.Set;

/**
 * Service for locating classes in the application context.
 */
public interface ClassLocator {

    Set<Class<?>> locateClasses(String directory);
}

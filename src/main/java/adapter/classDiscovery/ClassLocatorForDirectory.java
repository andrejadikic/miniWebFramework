package adapter.classDiscovery;



import domain.model.Constants;
import domain.model.classDiscovery.ClassLocator;
import domain.model.exceptions.ClassLocationException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassLocator implementation for directories.
 * <p>
 * User recursion to scan all files in the source root directory and filters
 * those that are classes (end with ".class")
 */
public class ClassLocatorForDirectory implements ClassLocator {
    private static final String INVALID_DIRECTORY_MSG = "Invalid directory '%s'.";

    private final Set<Class<?>> locatedClasses;

    public ClassLocatorForDirectory() {
        this.locatedClasses = new HashSet<>();
    }

    /**
     * @param directory the given directory.
     * @return a set of located classes.
     */
    @Override
    public Set<Class<?>> locateClasses(String directory) {
        this.locatedClasses.clear();
        File file = new File(directory);

        if (!file.isDirectory()) {
            throw new ClassLocationException(String.format(INVALID_DIRECTORY_MSG, directory));
        }

        System.out.println(file.listFiles().length);
        try {
            for (File innerFile : file.listFiles()) {
                System.out.println(innerFile.getName());
                this.scanDir(innerFile, "");
            }
        } catch (ClassNotFoundException e) {
            throw new ClassLocationException(e.getMessage(), e);
        }

        return this.locatedClasses;
    }

    /**
     * Recursive method for listing all files in a directory.
     * <p>
     * Starts with empty package name - ""
     * If the file is directory, for each sub file calls this method again
     * with the package name having the current file's name and a dot "." appended
     * in order to build a proper package name.
     * <p>
     * If the file is file and its name ends with ".class" it is loaded using the
     * built package name and it is added to a set of located classes.
     *
     * @param file        the current file.
     * @param packageName the current package name.
     */
    private void scanDir(File file, String packageName) throws ClassNotFoundException {
        if (file.isDirectory()) {
            packageName += file.getName() + ".";

            for (File innerFile : file.listFiles()) {
                this.scanDir(innerFile, packageName);
            }
        } else {
            System.out.println(file.getName());
            if (!file.getName().endsWith(Constants.JAVA_BINARY_EXTENSION)) {
                return;
            }

            final String className = packageName + file.getName().replace(Constants.JAVA_BINARY_EXTENSION, "");

            System.out.println(className);
            this.locatedClasses.add(Class.forName(className, true, Thread.currentThread().getContextClassLoader()));
        }
    }


    public static final List<Class> getAllClasses(String packageName) {
        List<Class> allClasses = new ArrayList<>();
        String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        for (String path: classPaths) {
            if (!path.endsWith("jar")) {
                try {
                    File entry = new File(path + File.separatorChar + packageName);
                    File[] files = entry.listFiles();
                    if (files != null) {
                        for (File file :  files) {
                            String name = file.getName();
                            if (name.endsWith(".class")) {
                                System.out.println("Class " + file.getName());
                                name = name.replace(".class", "");
                                packageName = packageName.replace("/", ".");
                                allClasses.add(Class.forName(packageName + "." + name));
                            } else {
                                String temporaryPackageName;
                                if (packageName.equals("")) temporaryPackageName = name;
                                else temporaryPackageName = packageName + "/" + name;
                                List<Class> directorySubClasses = getAllClasses(temporaryPackageName);
                                allClasses.addAll(directorySubClasses);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return allClasses;
    }
}

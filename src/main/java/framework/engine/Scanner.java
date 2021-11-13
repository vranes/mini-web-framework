package framework.engine;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Scanner {

    private static Scanner instance = null;

    public static Scanner getInstance(){
        if (instance == null){
            instance = new Scanner();
        }

        return instance;
    }


    public ArrayList<Class> scan() throws IOException, ClassNotFoundException {
        File javaDir = new File("src/main/java");

        ArrayList<File> files = listJavaFiles(javaDir);
        ArrayList<Class> classes = new ArrayList<>();

        // list all *.java files
        for (File f: files){
            classes.add(getClass(javaDir.getCanonicalPath(), f));
        }
        return classes;
    }


    private ArrayList<File> listJavaFiles(File libDir){

        ArrayList<File> fileList = new ArrayList<>();
        Stack<File> stack = new Stack<>();
        stack.push(libDir);

        while(!stack.isEmpty()) {
            File currFile = stack.pop();
            File[] files = currFile.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".java")){
                        return true;
                    }

                    else {
                        File f = new File(dir + "/" + name);
                        if (f.isDirectory()) {
                            stack.push(f);
                        }
                        return false;
                    }
                }
            });
            fileList.addAll(Arrays.asList(files));
        }
        return fileList;
    }


    private Class getClass(String dirName, File javaFile) throws IOException, ClassNotFoundException {
        String className = toQualifiedName(dirName, javaFile);
        System.out.println(className);
        Class cl = Class.forName(className);

        return cl;
    }


    private String toQualifiedName(String dirName, File javaFile) throws IOException {
        String fileName = javaFile.getCanonicalPath().replace(dirName + "\\", "");
        String className = fileName.replace("\\", ".").replace(".java", "");
        return className;
    }
}

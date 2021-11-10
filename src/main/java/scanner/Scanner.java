package scanner;

import annotations.Controller;
import annotations.http.Get;
import annotations.http.Path;
import annotations.http.Post;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Scanner {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner();
        scanner.scan();
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

    private void scan() throws IOException {
        File javaDir = new File("src/main/java");

        ArrayList<File> files = listJavaFiles(javaDir);

        // list all *.java files
        for (int i = 0; i < files.size(); i++){
            try {
                processClass(javaDir.getCanonicalPath(), files.get(i));
               // System.out.println("Found java file: " + files.get(i).getCanonicalPath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processClass(String dirName, File javaFile) throws IOException, ClassNotFoundException {
        String className = toQualifiedName(dirName, javaFile);
        System.out.println(className);
        Class cl = Class.forName(className);
        if(cl.getAnnotation(Controller.class) != null){
            processMethods(cl);
            // TODO instantiate and process methods
        }
    }

    private void processMethods(Class cl){
        for (Method m: cl.getDeclaredMethods()){
            Path path = m.getAnnotation(Path.class);
            if (path != null){
                Get g = m.getAnnotation(Get.class);
                Post p = m.getAnnotation(Post.class);
                if (g == null || p == null)
                    continue;
                // TODO
            }
        }
    }

    private String toQualifiedName(String dirName, File javaFile) throws IOException {
        String fileName = javaFile.getCanonicalPath().replace(dirName + "\\", "");
        String className = fileName.replace("\\", ".").replace(".java", "");
        return className;
    }
}

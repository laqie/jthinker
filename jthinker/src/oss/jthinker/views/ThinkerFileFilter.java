package oss.jthinker.views;

import java.io.File;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author iappel
 */
public class ThinkerFileFilter extends FileFilter {

    @Override
    public String getDescription() {
        return "jThinker files";
    }

    public boolean accept(File pathname) {
        return pathname.isDirectory() ||
               pathname.getPath().endsWith(".jthinker");
    }
}

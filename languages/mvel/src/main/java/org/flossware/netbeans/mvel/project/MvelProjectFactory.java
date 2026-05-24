package org.flossware.netbeans.mvel.project;

import org.flossware.netbeans.common.project.AbstractProjectFactory;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for creating MVEL projects.
 *
 * <p>Recognizes directories as MVEL projects based on common indicators:</p>
 * <ul>
 *   <li>Presence of .mvel files</li>
 *   <li>Presence of .mv files</li>
 *   <li>Maven projects with MVEL dependencies (pom.xml)</li>
 *   <li>Gradle projects with MVEL dependencies (build.gradle)</li>
 * </ul>
 *
 * <p><b>Note:</b> MVEL is typically used as an embedded expression language
 * in Java applications, so standalone MVEL projects are uncommon. This factory
 * primarily supports projects that contain MVEL template or expression files.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@ServiceProvider(service = ProjectFactory.class)
public class MvelProjectFactory extends AbstractProjectFactory {

    @Override
    protected boolean isProjectDirectory(FileObject dir) {
        if (dir == null || !dir.isFolder()) {
            return false;
        }

        // Check for build files (Maven/Gradle projects with MVEL)
        FileObject pomXml = dir.getFileObject("pom.xml");
        if (pomXml != null) {
            return true;
        }

        FileObject buildGradle = dir.getFileObject("build.gradle");
        if (buildGradle != null) {
            return true;
        }

        // Check for any .mvel or .mv files
        return hasMvelFiles(dir);
    }

    @Override
    protected Project createProject(FileObject dir) {
        return new MvelProject(dir);
    }

    @Override
    protected String getProjectTypeName() {
        return "MVEL";
    }

    /**
     * Check if directory contains any .mvel or .mv files.
     */
    private boolean hasMvelFiles(FileObject dir) {
        if (dir == null || !dir.isFolder()) {
            return false;
        }

        for (FileObject child : dir.getChildren()) {
            if (child.isData()) {
                String ext = child.getExt();
                if ("mvel".equals(ext) || "mv".equals(ext)) {
                    return true;
                }
            }
            if (child.isFolder() && !"build".equals(child.getName()) && !"target".equals(child.getName())) {
                if (hasMvelFiles(child)) {
                    return true;
                }
            }
        }
        return false;
    }
}

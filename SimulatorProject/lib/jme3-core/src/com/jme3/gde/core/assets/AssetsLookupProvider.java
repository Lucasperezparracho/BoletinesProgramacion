/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.gde.core.assets;

import com.jme3.gde.core.j2seproject.ProjectExtensionManager;
import com.jme3.gde.core.j2seproject.actions.UpgradeProjectWizardAction;
import com.jme3.gde.core.projects.SDKProjectUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.modules.gradle.api.GradleBaseProject;
import org.netbeans.modules.java.j2seproject.J2SEProject;
import org.netbeans.modules.java.j2seproject.api.J2SEPropertyEvaluator;
import org.netbeans.spi.project.LookupProvider;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Message;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Mutex;
import org.openide.util.MutexException;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author normenhansen
 */
public class AssetsLookupProvider implements LookupProvider {
    private static final Logger logger = Logger.getLogger(AssetsLookupProvider.class.getName());
    private Project project;
    private ProjectOpenedHook openedHook;
    public static final String[] keyList = new String[]{
        "assets.jar.name",
        "assets.folder.name",
        "assets.excludes",
        "assets.compress",
        "jme.project.version"
    };
    private final String extensionName = "assets";
    private final String extensionVersion = "v1.0";
    private final String extensionTargets =
            "    <target name=\"-init-assets\">\n"
            + "        <jar jarfile=\"${build.dir}/${assets.jar.name}\" excludes=\"${assets.excludes}\" basedir=\"${assets.folder.name}\" compress=\"${assets.compress}\"/>\n"
            + "        <property location=\"${assets.folder.name}\" name=\"assets.dir.resolved\"/>\n"
            + "        <property location=\"${build.dir}/${assets.jar.name}\" name=\"assets.jar.resolved\"/>\n"
            + "        <property location=\"${build.classes.dir}\" name=\"build.classes.dir.resolved\"/>\n"
            + "        <pathconvert property=\"run.classpath.without.build.classes.dir\">\n"
            + "        <path path=\"${run.classpath}\"/>\n"
            + "        <map from=\"${build.classes.dir.resolved}\" to=\"\"/>\n"
            + "        <map from=\"${assets.dir.resolved}\" to=\"${assets.jar.resolved}\"/>\n"
            + "        </pathconvert>\n"
            + "    </target>\n";
    private final String[] extensionDependencies = new String[]{"-do-init", "-init-assets"};
    private final ProjectExtensionManager manager = new ProjectExtensionManager(extensionName, extensionVersion, extensionTargets, extensionDependencies);

    @Override
    public Lookup createAdditionalLookup(Lookup lookup) {
        Project prj = lookup.lookup(Project.class);
        project = prj;
        if (SDKProjectUtils.isGradleProject(prj)) {
            GradleBaseProject gradle = GradleBaseProject.get(project);
            
            // In a new project, Assets project name is returned by NB as "assets"
            // but when project is reloaded, it is returned as ":assets"
            Optional<String> assetsProjectName = 
                    (gradle.getSubProjects().containsKey("assets")) ? Optional.of("assets") : 
                    (gradle.getSubProjects().containsKey(":assets")) ? Optional.of(":assets") :
                    Optional.empty();
            
            if (assetsProjectName.isPresent()) {
                logger.log(Level.FINE, "Found assets subproject, extending with ProjectAssetManager");
                /* Note: The ProjectAssetManager needs the Subproject's Project,
                 * because otherwise reading assets won't work (as assets is
                 * not in the scope of the parent project). But it still needs 
                 * to be added to the Parent Project's Lookup
                */
                
                /* Complicated code to find the relative path, because the 
                 * assets subproject could be stored in a folder called "foo"
                 * or "bar" and we still need to locate it correctly
                */
                Path assetsPath = gradle.getSubProjects().get(assetsProjectName.get()).toPath();
                Path parentPath = new File(prj.getProjectDirectory().getPath()).toPath();
                Path relativePath = parentPath.relativize(assetsPath);
                try {
                    Project assetsProj = ProjectManager.getDefault().findProject(
                        prj.getProjectDirectory().getFileObject(relativePath.toString()));
                    if (assetsProj != null) {
                        return Lookups.fixed(new ProjectAssetManager(assetsProj,
                        "")); // the String is seen relative to the project root
                    }
                } catch (IOException io) {
                    Exceptions.printStackTrace(io);
                }
            }
        } else {
            FileObject assetsProperties = prj.getProjectDirectory().getFileObject("nbproject/project.properties");
            if (assetsProperties != null && assetsProperties.isValid()) {
                FileLock lock = null;
                try {
                    lock = assetsProperties.lock();
                    Properties properties;
                    try (InputStream in = assetsProperties.getInputStream()) {
                        properties = new Properties();
                        properties.load(in);
                    }
                    String assetsFolderName = properties.getProperty("assets.folder.name", "assets");
                    if (prj.getProjectDirectory().getFileObject(assetsFolderName) != null) {
                        logger.log(Level.FINE, "Valid jMP project, extending with ProjectAssetManager");
                        openedHook = genOpenedHook(project);
                        return Lookups.fixed(new ProjectAssetManager(prj, assetsFolderName), openedHook);
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                } finally {
                    if (lock != null) {
                        lock.releaseLock();
                    }
                }
            }
        }
        return Lookups.fixed();
    }
    
    private ProjectOpenedHook genOpenedHook(final Project context) {
        return new ProjectOpenedHook() {
            @Override
            protected void projectClosed() {
            }

            @Override
            protected void projectOpened() {
                if (context instanceof J2SEProject) {
                    EditableProperties properties = getProperties(context);
                    if (properties.getProperty("assets.folder.name") != null) {
                        manager.checkExtension(context);

                        String version = properties.getProperty("jme.project.version");
                        String projectName = ProjectUtils.getInformation(context).getDisplayName();
                        if (version == null) {
                            if (UpgradeProjectWizardAction.isJME31(context)) { /* Upgrade project.properties */
                                logger.log(Level.WARNING, "[{0}] Found 3.1 project, upgrading project.properties", projectName);
                                FileObject prProp = context.getProjectDirectory().getFileObject("nbproject/project.properties");
                                if (prProp != null && prProp.isValid()) {
                                    FileLock lock = null;
                                    try {
                                        lock = prProp.lock();
                                        EditableProperties edProps;
                                        try (InputStream in = prProp.getInputStream()) {
                                            edProps = new EditableProperties(true);
                                            edProps.load(in);
                                        }

                                        edProps.setProperty("jme.project.version", "3.1"); // Silently accept them.
                                        OutputStream out = prProp.getOutputStream(lock);
                                        edProps.store(out);
                                        out.close();
                                    } catch (IOException e) {
                                        logger.log(Level.WARNING, "Error when trying to write project.properties. Exception: {0}", e.getMessage());
                                    } finally {
                                        if (lock != null) {
                                            lock.releaseLock();
                                        }
                                    }
                                }
                            } else {
                                DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message("The project \"" + projectName + "\" is not compatible with the current SDK.\nIt has to be updated before you can use it.\nRight Click on the Project and select \"Upgrade Project\" (You can choose to keep 3.0 compatibility!)", Message.ERROR_MESSAGE));
                            }
                        }
                    }
                }
            }
        };
    }

    public static EditableProperties getProperties(Project project) {
        EditableProperties props = new EditableProperties(true);
        if (!(project instanceof J2SEProject)) {
            return props;
        }
        load(props, project);
        return props;
    }

    public static void load(EditableProperties ep, Project project) {
        J2SEPropertyEvaluator eval = project.getLookup().lookup(J2SEPropertyEvaluator.class);
        if (eval == null) {
            return;
        }
        for (int i = 0; i < keyList.length; i++) {
            String string = keyList[i];
            String value = eval.evaluator().getProperty(string);
            if (value != null) {
                ep.setProperty(string, value);
            }
            //TODO: create defaults!
        }
    }

    public static void store(final EditableProperties storeProps, Project project) throws IOException {
        final FileObject projPropsFO = project.getProjectDirectory().getFileObject(AntProjectHelper.PROJECT_PROPERTIES_PATH);
        final EditableProperties ep = new EditableProperties(true);
//        final EditableProperties pep = new EditableProperties(true);
//        final FileObject privPropsFO = project.getProjectDirectory().getFileObject(AntProjectHelper.PRIVATE_PROPERTIES_PATH);

        try {
            final InputStream is = projPropsFO.getInputStream();
//            final InputStream pis = privPropsFO.getInputStream();
            ProjectManager.mutex().writeAccess(new Mutex.ExceptionAction<Void>() {
                @Override
                public Void run() throws Exception {
                    try {
                        ep.load(is);
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
//                    try {
//                        pep.load(pis);
//                    } finally {
//                        if (pis != null) {
//                            pis.close();
//                        }
//                    }
                    putProperties(storeProps, ep);
                    OutputStream os = null;
                    FileLock lock = null;
                    try {
                        lock = projPropsFO.lock();
                        os = projPropsFO.getOutputStream(lock);
                        ep.store(os);
                    } finally {
                        if (lock != null) {
                            lock.releaseLock();
                        }
                        if (os != null) {
                            os.close();
                        }
                    }
//                    try {
//                        lock = privPropsFO.lock();
//                        os = privPropsFO.getOutputStream(lock);
//                        pep.store(os);
//                    } finally {
//                        if (lock != null) {
//                            lock.releaseLock();
//                        }
//                        if (os != null) {
//                            os.close();
//                        }
//                    }
                    return null;
                }
            });
        } catch (MutexException mux) {
            throw (IOException) mux.getException();
        }

    }

    public static void putProperties(EditableProperties from, EditableProperties to) {
        for (int i = 0; i < keyList.length; i++) {
            String string = keyList[i];
            String value = from.getProperty(string);
            if (value != null) {
                to.put(string, value);
            }
        }
    }
}

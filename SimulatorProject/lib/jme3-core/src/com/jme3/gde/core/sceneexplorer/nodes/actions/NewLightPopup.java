/*
 *  Copyright (c) 2009-2010 jMonkeyEngine
 *  All rights reserved.
 * 
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 *  * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.gde.core.sceneexplorer.nodes.actions;

import com.jme3.bounding.BoundingSphere;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.LightProbeFactory;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.controller.SceneToolController;
import com.jme3.gde.core.sceneexplorer.nodes.JmeLightProbeProgressHandler;
import com.jme3.gde.core.sceneexplorer.nodes.JmeSpatial;
import com.jme3.gde.core.undoredo.AbstractUndoableSceneEdit;
import com.jme3.gde.core.undoredo.SceneUndoRedoManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightProbe;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;

/**
 * Handles the SceneExplorers Popup Menu "New Light"
 *
 * @author normenhansen
 */
public class NewLightPopup extends AbstractAction implements Presenter.Popup {

    protected JmeSpatial jmeNode;
    protected Spatial node;
    protected DataObject dataObject;

    public NewLightPopup(JmeSpatial node) {
        this.jmeNode = node;
        this.node = node.getLookup().lookup(Spatial.class);
        this.dataObject = node.getLookup().lookup(DataObject.class);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu result = new JMenu("Add Light..");
        result.add(new JMenuItem(new AddAmbientAction()));
        result.add(new JMenuItem(new AddDirectionalAction()));
        result.add(new JMenuItem(new AddPointAction()));
        result.add(new JMenuItem(new AddSpotAction()));
        // FIXME: This is a work around due to issue #176: Scene graph is not properly updated for rendering
        // Something happens in LightProbeFactory
        if(node instanceof Node) {
            result.add(new JMenuItem(new AddProbeAction()));
        }

        return result;
    }

    private class AddAmbientAction extends AbstractAction {

        public AddAmbientAction() {
            putValue(NAME, "Ambient Light");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SceneApplication.getApplication().enqueue(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    AmbientLight light = new AmbientLight();
                    light.setName("AmbientLight");
                    light.setColor(ColorRGBA.White);
                    node.addLight(light);
                    addLightUndo(node, light);
                    setModified();
                    return null;
                }
            });
        }
    }

    private class AddDirectionalAction extends AbstractAction {

        public AddDirectionalAction() {
            putValue(NAME, "Directional Light");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SceneApplication.getApplication().enqueue(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    DirectionalLight light = new DirectionalLight();
                    light.setName("DirectionalLight");
                    light.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
                    light.setColor(ColorRGBA.White);
                    node.addLight(light);
                    addLightUndo(node, light);
                    setModified();
                    return null;
                }
            });
        }
    }

    private class AddPointAction extends AbstractAction {

        public AddPointAction() {
            putValue(NAME, "Point Light");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SceneApplication.getApplication().enqueue(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    PointLight light = new PointLight();
                    light.setColor(ColorRGBA.White);
                    light.setName("PointLight");
                    node.addLight(light);
                    addLightUndo(node, light);
                    setModified();
                    return null;
                }
            });
        }
    }

    private class AddSpotAction extends AbstractAction {

        public AddSpotAction() {
            putValue(NAME, "Spot Light");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SceneApplication.getApplication().enqueue(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    SpotLight light = new SpotLight();
                    light.setColor(ColorRGBA.White);
                    light.setName("SpotLight");
                    node.addLight(light);
                    addLightUndo(node, light);
                    setModified();
                    return null;
                }
            });
        }
    }

    private class AddProbeAction extends AbstractAction {

        public AddProbeAction() {
            putValue(NAME, "Light Probe");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SceneApplication.getApplication().enqueue(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    EnvironmentCamera envCam = SceneApplication.getApplication().getStateManager().getState(EnvironmentCamera.class);
                    SceneToolController toolController = SceneApplication.getApplication().getStateManager().getState(SceneToolController.class);
                    if (toolController != null) {
                        envCam.setPosition(toolController.getCursorLocation());
                    } else {
                        envCam.setPosition(new Vector3f(0, 0, 0));
                    }
                    LightProbe lightProbe = LightProbeFactory.makeProbe(envCam, node, new JmeLightProbeProgressHandler());
                    lightProbe.setName("LightProbe");
                    node.addLight(lightProbe);
                    ((BoundingSphere) lightProbe.getBounds()).setRadius(10);
                    node.updateModelBound();
                    addLightUndo(node, lightProbe);
                    setModified();

                    return null;
                }
            });
        }
    }

    private void addLightUndo(final Spatial undoParent, final Light undoLight) {
        //add undo
        if (undoParent != null && undoLight != null) {
            Lookup.getDefault().lookup(SceneUndoRedoManager.class).addEdit(this, new AbstractUndoableSceneEdit() {

                @Override
                public void sceneUndo() throws CannotUndoException {
                    undoParent.removeLight(undoLight);
                }

                @Override
                public void sceneRedo() throws CannotRedoException {
                    undoParent.addLight(undoLight);
                }

                @Override
                public void awtRedo() {
                    dataObject.setModified(true);
                    jmeNode.refresh(true);
                }

                @Override
                public void awtUndo() {
                    dataObject.setModified(true);
                    jmeNode.refresh(true);
                }
            });
        }
    }

    private void setModified() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                dataObject.setModified(true);
                jmeNode.refresh(true);
            }
        });
    }
}

/*
 *  Copyright (c) 2009-2018 jMonkeyEngine
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
package com.jme3.gde.materialdefinition.editor;

import com.jme3.gde.core.editor.nodes.Diagram;
import com.jme3.gde.materials.MaterialPreviewRenderer;
import com.jme3.material.Material;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The ShaderBackdrop Panel is the Material renderer in the background of the
 * ShaderNodes Editor. It can be placed behind all the out-busses but can also
 * be toggled to be drawn infront of everything
 * @author Nehon
 */
public class BackdropPanel extends JPanel implements MouseListener, ChangeListener,MouseMotionListener {

    private final MaterialPreviewRenderer renderer;
    private Material mat;

    /**
     * Creates new form BackdropPanel
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public BackdropPanel() {
        initComponents();
        //  setBounds(0, 0, 300, 300);
        setLayout(null);
        toolBar.setVisible(false);
        addMouseListener(this);
        addMouseMotionListener(this);
        renderer = new MaterialPreviewRenderer(previewLabel);
        recalculateTimer.setRepeats(false);
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                updateComponents(e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                updateComponents(e);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                updateComponents(e);
            }

        });

        previewLabel.setBounds(0, 0, 120, 120);
        toolBar.setBounds(0, 0, 120, 16);
        repaint();
    }

    private void updateComponents(ComponentEvent e) {
        previewLabel.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
        toolBar.setBounds(0, 0, e.getComponent().getWidth(), toolBar.getHeight());
        repaint();
    }

    public void cleanup() {
        renderer.cleanUp();
    }

    public void showMaterial(Material mat) {
        if (isVisible()) {
            this.mat = mat;
            renderer.showMaterial(mat); 
        }
    }
    
     public void showMaterial(Material mat, String technique) {
        if (isVisible()) {
            this.mat = mat;
            renderer.showMaterial(mat, technique); 
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBar = new javax.swing.JPanel();
        reloadButton = new javax.swing.JButton();
        expandButton = new javax.swing.JButton();
        sphereButton = new javax.swing.JButton();
        boxButton = new javax.swing.JButton();
        quadButton = new javax.swing.JButton();
        bringToFrontButton = new javax.swing.JButton();
        previewLabel = new javax.swing.JLabel();

        toolBar.setOpaque(false);

        reloadButton.setBackground(new java.awt.Color(153, 153, 153));
        reloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/core/editor/icons/reload.png"))); // NOI18N
        reloadButton.setToolTipText(org.openide.util.NbBundle.getMessage(BackdropPanel.class, "BackdropPanel.reloadButton.toolTipText")); // NOI18N
        reloadButton.setBorder(null);
        reloadButton.setBorderPainted(false);
        reloadButton.setContentAreaFilled(false);
        reloadButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        reloadButton.setFocusable(false);
        reloadButton.setIconTextGap(0);
        reloadButton.setMaximumSize(new java.awt.Dimension(24, 24));
        reloadButton.setMinimumSize(new java.awt.Dimension(24, 24));
        reloadButton.setPreferredSize(new java.awt.Dimension(24, 24));
        reloadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reloadButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reloadButtonMouseExited(evt);
            }
        });
        reloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadButtonActionPerformed(evt);
            }
        });

        expandButton.setBackground(new java.awt.Color(153, 153, 153));
        expandButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/core/editor/icons/collapse.png"))); // NOI18N
        expandButton.setToolTipText(org.openide.util.NbBundle.getMessage(BackdropPanel.class, "BackdropPanel.expandButton.toolTipText")); // NOI18N
        expandButton.setBorder(null);
        expandButton.setBorderPainted(false);
        expandButton.setContentAreaFilled(false);
        expandButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        expandButton.setFocusable(false);
        expandButton.setIconTextGap(0);
        expandButton.setMaximumSize(new java.awt.Dimension(24, 24));
        expandButton.setMinimumSize(new java.awt.Dimension(24, 24));
        expandButton.setPreferredSize(new java.awt.Dimension(24, 24));
        expandButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                expandButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                expandButtonMouseExited(evt);
            }
        });
        expandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expandButtonActionPerformed(evt);
            }
        });

        sphereButton.setBackground(new java.awt.Color(153, 153, 153));
        sphereButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/core/editor/icons/Sphere.png"))); // NOI18N
        sphereButton.setToolTipText(org.openide.util.NbBundle.getMessage(BackdropPanel.class, "BackdropPanel.sphereButton.toolTipText")); // NOI18N
        sphereButton.setBorder(null);
        sphereButton.setBorderPainted(false);
        sphereButton.setContentAreaFilled(false);
        sphereButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        sphereButton.setFocusable(false);
        sphereButton.setIconTextGap(0);
        sphereButton.setMaximumSize(new java.awt.Dimension(24, 24));
        sphereButton.setMinimumSize(new java.awt.Dimension(24, 24));
        sphereButton.setPreferredSize(new java.awt.Dimension(24, 24));
        sphereButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sphereButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sphereButtonMouseExited(evt);
            }
        });
        sphereButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sphereButtonActionPerformed(evt);
            }
        });

        boxButton.setBackground(new java.awt.Color(153, 153, 153));
        boxButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/core/editor/icons/cube.png"))); // NOI18N
        boxButton.setToolTipText(org.openide.util.NbBundle.getMessage(BackdropPanel.class, "BackdropPanel.boxButton.toolTipText")); // NOI18N
        boxButton.setBorder(null);
        boxButton.setBorderPainted(false);
        boxButton.setContentAreaFilled(false);
        boxButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        boxButton.setFocusable(false);
        boxButton.setIconTextGap(0);
        boxButton.setMaximumSize(new java.awt.Dimension(24, 24));
        boxButton.setMinimumSize(new java.awt.Dimension(24, 24));
        boxButton.setPreferredSize(new java.awt.Dimension(24, 24));
        boxButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boxButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boxButtonMouseExited(evt);
            }
        });
        boxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxButtonActionPerformed(evt);
            }
        });

        quadButton.setBackground(new java.awt.Color(153, 153, 153));
        quadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/core/editor/icons/Quad.png"))); // NOI18N
        quadButton.setToolTipText(org.openide.util.NbBundle.getMessage(BackdropPanel.class, "BackdropPanel.quadButton.toolTipText")); // NOI18N
        quadButton.setBorder(null);
        quadButton.setBorderPainted(false);
        quadButton.setContentAreaFilled(false);
        quadButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        quadButton.setFocusable(false);
        quadButton.setIconTextGap(0);
        quadButton.setMaximumSize(new java.awt.Dimension(24, 24));
        quadButton.setMinimumSize(new java.awt.Dimension(24, 24));
        quadButton.setPreferredSize(new java.awt.Dimension(24, 24));
        quadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quadButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quadButtonMouseExited(evt);
            }
        });
        quadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quadButtonActionPerformed(evt);
            }
        });

        bringToFrontButton.setBackground(new java.awt.Color(153, 153, 153));
        bringToFrontButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/core/editor/icons/frontBack.png"))); // NOI18N
        bringToFrontButton.setToolTipText(org.openide.util.NbBundle.getMessage(BackdropPanel.class, "BackdropPanel.bringToFrontButton.toolTipText")); // NOI18N
        bringToFrontButton.setBorder(null);
        bringToFrontButton.setBorderPainted(false);
        bringToFrontButton.setContentAreaFilled(false);
        bringToFrontButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        bringToFrontButton.setFocusable(false);
        bringToFrontButton.setIconTextGap(0);
        bringToFrontButton.setMaximumSize(new java.awt.Dimension(24, 24));
        bringToFrontButton.setMinimumSize(new java.awt.Dimension(24, 24));
        bringToFrontButton.setPreferredSize(new java.awt.Dimension(24, 24));
        bringToFrontButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bringToFrontButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bringToFrontButtonMouseExited(evt);
            }
        });
        bringToFrontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bringToFrontButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout toolBarLayout = new javax.swing.GroupLayout(toolBar);
        toolBar.setLayout(toolBarLayout);
        toolBarLayout.setHorizontalGroup(
            toolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolBarLayout.createSequentialGroup()
                .addComponent(expandButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bringToFrontButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sphereButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boxButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(reloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        toolBarLayout.setVerticalGroup(
            toolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(expandButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(reloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(sphereButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(boxButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(quadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(bringToFrontButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        add(toolBar);

        previewLabel.setBackground(new java.awt.Color(100, 100, 100));
        previewLabel.setForeground(new java.awt.Color(100, 100, 100));
        previewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        previewLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        previewLabel.setIconTextGap(0);
        previewLabel.setMaximumSize(new java.awt.Dimension(100, 100));
        previewLabel.setMinimumSize(new java.awt.Dimension(100, 100));
        previewLabel.setOpaque(true);
        previewLabel.setPreferredSize(new java.awt.Dimension(100, 100));
        add(previewLabel);
    }// </editor-fold>//GEN-END:initComponents

    private void sphereButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sphereButtonMouseEntered
        mouseEntered(SwingUtilities.convertMouseEvent(sphereButton, evt, this));
    }//GEN-LAST:event_sphereButtonMouseEntered

    private void sphereButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sphereButtonMouseExited
        mouseExited(SwingUtilities.convertMouseEvent(sphereButton, evt, this));
    }//GEN-LAST:event_sphereButtonMouseExited

    private void sphereButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sphereButtonActionPerformed
        renderer.switchDisplay(MaterialPreviewRenderer.DisplayType.Sphere);
        refresh();
    }//GEN-LAST:event_sphereButtonActionPerformed

    private void boxButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boxButtonMouseEntered
        mouseEntered(SwingUtilities.convertMouseEvent(boxButton, evt, this));
    }//GEN-LAST:event_boxButtonMouseEntered

    private void boxButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boxButtonMouseExited
        mouseExited(SwingUtilities.convertMouseEvent(boxButton, evt, this));
    }//GEN-LAST:event_boxButtonMouseExited

    private void boxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxButtonActionPerformed
        renderer.switchDisplay(MaterialPreviewRenderer.DisplayType.Box);
        refresh();
    }//GEN-LAST:event_boxButtonActionPerformed

    private void reloadButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reloadButtonMouseEntered
        mouseEntered(SwingUtilities.convertMouseEvent(reloadButton, evt, this));
    }//GEN-LAST:event_reloadButtonMouseEntered

    private void reloadButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reloadButtonMouseExited
        mouseExited(SwingUtilities.convertMouseEvent(reloadButton, evt, this));
    }//GEN-LAST:event_reloadButtonMouseExited

    private void reloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadButtonActionPerformed
        refresh();
    }//GEN-LAST:event_reloadButtonActionPerformed

    private void quadButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quadButtonMouseEntered
        mouseEntered(SwingUtilities.convertMouseEvent(quadButton, evt, this));
    }//GEN-LAST:event_quadButtonMouseEntered

    private void quadButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quadButtonMouseExited
        mouseExited(SwingUtilities.convertMouseEvent(quadButton, evt, this));
    }//GEN-LAST:event_quadButtonMouseExited

    private void quadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quadButtonActionPerformed
        renderer.switchDisplay(MaterialPreviewRenderer.DisplayType.Quad);
        refresh();
    }//GEN-LAST:event_quadButtonActionPerformed

    private void expandButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expandButtonMouseEntered
        mouseEntered(SwingUtilities.convertMouseEvent(expandButton, evt, this));
    }//GEN-LAST:event_expandButtonMouseEntered

    private void expandButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expandButtonMouseExited
        mouseExited(SwingUtilities.convertMouseEvent(expandButton, evt, this));
    }//GEN-LAST:event_expandButtonMouseExited

    private void expandButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expandButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_expandButtonActionPerformed

    private void bringToFrontButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bringToFrontButtonMouseEntered
         mouseEntered(SwingUtilities.convertMouseEvent(bringToFrontButton, evt, this));
    }//GEN-LAST:event_bringToFrontButtonMouseEntered

    private void bringToFrontButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bringToFrontButtonMouseExited
        mouseExited(SwingUtilities.convertMouseEvent(bringToFrontButton, evt, this));
    }//GEN-LAST:event_bringToFrontButtonMouseExited

    private void bringToFrontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bringToFrontButtonActionPerformed
        Diagram diagram = ((Diagram) getParent());
        if (diagram.getComponentZOrder(this) > 0) {
            diagram.setComponentZOrder(this, 0);
        } else {
            diagram.setComponentZOrder(this, ((Diagram) getParent()).getComponentCount() - 1);
        }
        diagram.repaint();
    }//GEN-LAST:event_bringToFrontButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boxButton;
    private javax.swing.JButton bringToFrontButton;
    private javax.swing.JButton expandButton;
    private javax.swing.JLabel previewLabel;
    private javax.swing.JButton quadButton;
    private javax.swing.JButton reloadButton;
    private javax.swing.JButton sphereButton;
    private javax.swing.JPanel toolBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Container c = getParent();
        if (c != null) {
            ((MouseListener) c).mousePressed(SwingUtilities.convertMouseEvent(this, e, c));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Container c = getParent();
        if (c != null) {
            ((MouseListener) c).mouseReleased(SwingUtilities.convertMouseEvent(this, e, c));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        toolBar.setVisible(true);
        t.stop();
    }
    Timer t = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            toolBar.setVisible(false);
        }
    });

    @Override
    public void mouseExited(MouseEvent e) {
        t.restart();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JViewport vp = (JViewport) e.getSource();
        update(vp);

    }

    private final Timer recalculateTimer = new Timer(20, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    });

    protected void update(JViewport viewPort) {
        if (isVisible()) {
            JScrollPane pane = (JScrollPane) viewPort.getParent();
            int width = viewPort.getWidth();
            int height = viewPort.getHeight();

            int size = Math.min(width, height) - 25;
            setSize(size, size);
            setBounds(0, 0, size, size);
            Point pos= viewPort.getViewPosition();
            setLocation(pos.x + width / 2 - size / 2, pos.y + height / 2 - size / 2);

            repaint();
            if (recalculateTimer.isRunning()) {
                recalculateTimer.restart();
            } else {
                recalculateTimer.start();
            }
        }

    }

    private void refresh() {
        if (mat != null) {
            renderer.showMaterial(mat);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Container c = getParent();
        if (c != null) {
            ((MouseMotionListener) c).mouseDragged(SwingUtilities.convertMouseEvent(this, e, c));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Container c = getParent();
        if (c != null) {
            ((MouseMotionListener) c).mouseMoved(SwingUtilities.convertMouseEvent(this, e, c));
        }
    }
    
    public MaterialPreviewRenderer getRenderer(){
        return renderer;
    }

    public void refreshOnly() {
        if (mat != null) {
            renderer.refreshOnly();
        }
    }
}
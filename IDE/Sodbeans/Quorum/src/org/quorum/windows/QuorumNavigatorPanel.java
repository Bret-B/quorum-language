/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quorum.windows;

import javax.swing.ActionMap;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.quorum.windows.nodes.QuorumClassNode;

/**
 *
 * @author stefika
 */
public class QuorumNavigatorPanel extends TopComponent implements ExplorerManager.Provider, Lookup.Provider {
    private ExplorerManager manager = new ExplorerManager();
    /**
     * Creates new form QuorumNavigatorPanel
     */
    public QuorumNavigatorPanel() {
        initComponents();
        ActionMap map = this.getActionMap();
        associateLookup (ExplorerUtils.createLookup (manager, map));
    }
    
    public void setRoot(QuorumClassNode node) {
        manager.setRootContext(node);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new BeanTreeView();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
    
    @Override
    protected void componentActivated() {
        ExplorerUtils.activateActions(manager, true);
    }
    @Override
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(manager, false);
    }
}

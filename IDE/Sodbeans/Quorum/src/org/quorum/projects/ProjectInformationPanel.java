/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.quorum.projects;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import quorum.Libraries.Language.Compile.Compiler;

/**
 *
 * @author astefik
 */
public class ProjectInformationPanel extends javax.swing.JPanel {

    private Project project;
    /**
     * Creates new form ProjectInformationPanel
     */
    public ProjectInformationPanel() {
        initComponents();
    }
    
    private void loadProperties() {
        Lookup lookup = project.getLookup();
        Properties properties = project.getLookup().lookup(Properties.class);
        String property = properties.getProperty(QuorumProject.QUORUM_PROJECT_TYPE);
        if(property == null || property.compareTo(QuorumProject.QUORUM_CONSOLE_PROJECT) == 0) {
            projectRadioButton.setSelected(true);
        } else if(property.compareTo(QuorumProject.QUORUM_COMPILED_WEB_PROJECT) == 0) {
            compiledWebProjectRadioButton.setSelected(true);
        } else if(property.compareTo(QuorumProject.QUORUM_LEGO_PROJECT) == 0) {
            legoRadioButton.setSelected(true);
        } else if (property.compareTo(QuorumProject.QUORUM_WEB_PROJECT) == 0) {
            webBrowserRadioButton.setSelected(true);
        }
        
        property = properties.getProperty(QuorumProject.QUORUM_EXECUTABLE_NAME);
        if(property != null) {
            nameTextField.setText(property);
        } else {
            Compiler compiler = lookup.lookup(quorum.Libraries.Language.Compile.Compiler.class);
            nameTextField.setText(compiler.GetName());
        }
        
        property = properties.getProperty(QuorumProject.ADDITIONAL_PLUGIN_FOLDERS);
        if(property != null) {
            addItemsToJList(pluginList, property);
        }
        property = properties.getProperty(QuorumProject.ADDITIONAL_JARS);
        if(property != null) {
            addItemsToJList(jarList, property);
        }
        
        property = properties.getProperty(QuorumProject.ADDITIONAL_SOURCES);
        if(property != null) {
            addItemsToJList(sourceList, property);
        }
    }
    
    public void addItemsToJList(JList list, String string) {
        String[] split = string.split(";");
        for(int i = 0; i < split.length; i++) {
            String val = split[i];
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.addElement(val);
        }
    } 
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
        loadProperties();
    }
    
    public String getQuorumProjectType() {
        if(projectRadioButton.isSelected()) {
            return QuorumProject.QUORUM_CONSOLE_PROJECT;
        } else if(compiledWebProjectRadioButton.isSelected()) {
            return QuorumProject.QUORUM_COMPILED_WEB_PROJECT;
        } else if(webBrowserRadioButton.isSelected()) {
            return QuorumProject.QUORUM_WEB_PROJECT;
        } else {
            return QuorumProject.QUORUM_LEGO_PROJECT;
        }
    }
    
    public String getSourceList() {
        DefaultListModel model = (DefaultListModel) sourceList.getModel();
        return getPathsFromModel(model);
    }
    
    public String getJarList() {
        DefaultListModel model = (DefaultListModel) jarList.getModel();
        return getPathsFromModel(model);
    }
    
    public String getPluginList() {
        DefaultListModel model = (DefaultListModel) pluginList.getModel();
        return getPathsFromModel(model);
    }
    
    public String getExecutableName() {
        String text = nameTextField.getText();
        if(text == null || text.isEmpty()) {
            return null;
        } else {
            return text;
        }
    }
    
    public String getPathsFromModel(DefaultListModel model) {
        String returnMe = "";
        
        for(int i = 0; i < model.size(); i++) {
            String string = (String) model.get(i);
            if(i == 0) {
                returnMe = string;
            } else {
                returnMe = returnMe + ";" + string;
            }
        }
        if(returnMe.isEmpty()) {
            return null;
        } else {
            return returnMe;
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

        projectTypeButtonGroup = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        pluginFolderChooser = new javax.swing.JFileChooser();
        jarFileChooser = new javax.swing.JFileChooser();
        sourceFileChooser = new javax.swing.JFileChooser();
        jLabel2 = new javax.swing.JLabel();
        projectRadioButton = new javax.swing.JRadioButton();
        compiledWebProjectRadioButton = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pluginList = new javax.swing.JList();
        addPlugin = new javax.swing.JButton();
        removePlugin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jarList = new javax.swing.JList();
        addJar = new javax.swing.JButton();
        removeJar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        pluginClearButton = new javax.swing.JButton();
        jarClearButton = new javax.swing.JButton();
        legoRadioButton = new javax.swing.JRadioButton();
        webBrowserRadioButton = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        sourceList = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        addSourceButton = new javax.swing.JButton();
        removeSourceButton = new javax.swing.JButton();
        clearSourceButton = new javax.swing.JButton();

        jRadioButton1.setText("jRadioButton1");

        pluginFolderChooser.setDialogTitle("");
        pluginFolderChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        jarFileChooser.setFileFilter(null);

        sourceFileChooser.setFileFilter(null);
        sourceFileChooser.setMultiSelectionEnabled(true);
        sourceFileChooser.getAccessibleContext().setAccessibleName("Additional Source File Chooser");
        sourceFileChooser.getAccessibleContext().setAccessibleDescription("This dialog allows you to select multiple quorum source files as an addition to your project.");

        jLabel2.setText("Select Project Type:");

        projectTypeButtonGroup.add(projectRadioButton);
        projectRadioButton.setText("Standard");
        projectRadioButton.setToolTipText("Select this radio button if you would like to have a normal Quorum project");

        projectTypeButtonGroup.add(compiledWebProjectRadioButton);
        compiledWebProjectRadioButton.setText("Web Server");
        compiledWebProjectRadioButton.setToolTipText("Use this option if you want to compile your Quorum code for use in a Java Web Server, like Tomcat or Glassfish");

        pluginList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(pluginList);

        addPlugin.setText("Add");
        addPlugin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPluginActionPerformed(evt);
            }
        });

        removePlugin.setText("Remove");
        removePlugin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePluginActionPerformed(evt);
            }
        });

        jLabel1.setText("Additional Quorum Plugin Folders");

        jarList.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(jarList);

        addJar.setText("Add");
        addJar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJarActionPerformed(evt);
            }
        });

        removeJar.setText("Remove");
        removeJar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeJarActionPerformed(evt);
            }
        });

        jLabel3.setText("Additional Java Jar Libraries");

        nameTextField.setToolTipText("This field indicates the name the Quorum compiler will use for the program");

        jLabel4.setText("Program Name:");

        pluginClearButton.setText("Clear");
        pluginClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pluginClearButtonActionPerformed(evt);
            }
        });

        jarClearButton.setText("Clear");
        jarClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jarClearButtonActionPerformed(evt);
            }
        });

        projectTypeButtonGroup.add(legoRadioButton);
        legoRadioButton.setText("Lego");

        projectTypeButtonGroup.add(webBrowserRadioButton);
        webBrowserRadioButton.setText("Web Browser");

        sourceList.setModel(new DefaultListModel());
        jScrollPane3.setViewportView(sourceList);

        jLabel5.setText("Additional Source Files");

        addSourceButton.setText("Add");
        addSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSourceButtonActionPerformed(evt);
            }
        });

        removeSourceButton.setText("Remove");
        removeSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSourceButtonActionPerformed(evt);
            }
        });

        clearSourceButton.setText("Clear");
        clearSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSourceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(projectRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(compiledWebProjectRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(legoRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(webBrowserRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(111, 111, 111))
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane3)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addPlugin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(removePlugin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addJar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(removeJar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(pluginClearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jarClearButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addSourceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(removeSourceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(clearSourceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(projectRadioButton)
                    .addComponent(legoRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(compiledWebProjectRadioButton)
                    .addComponent(webBrowserRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addPlugin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removePlugin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pluginClearButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addJar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeJar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jarClearButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addSourceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeSourceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearSourceButton)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        legoRadioButton.getAccessibleContext().setAccessibleDescription("This project type should be selected if you would like Quorum to automatically send programs to a lego robot after building.");
    }// </editor-fold>//GEN-END:initComponents

    private void addPluginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPluginActionPerformed
        File home = new File(System.getProperty("user.home"));
        int returnVal = pluginFolderChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = pluginFolderChooser.getSelectedFile();
            if(file != null && file.isDirectory()) {
                String path = getPathRelativeToProject(file);
                DefaultListModel model = (DefaultListModel) pluginList.getModel();
                if(!model.contains(path) && path != null && !path.isEmpty()) {
                    model.addElement(path);
                }
            }
        }
    }//GEN-LAST:event_addPluginActionPerformed

    private void removePluginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removePluginActionPerformed
        DefaultListModel model = (DefaultListModel) pluginList.getModel();
        int index = pluginList.getSelectedIndex();
        model.remove(index);
    }//GEN-LAST:event_removePluginActionPerformed

    private void addJarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJarActionPerformed
        File home = new File(System.getProperty("user.home"));
        int returnVal = jarFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jarFileChooser.getSelectedFile();
            if(file != null && file.isFile()) {
                String absolutePath = file.getAbsolutePath();
                if (absolutePath.length() > 4) {
                    String extension = absolutePath.substring(absolutePath.length() - 4);
                    if(extension.compareTo(".jar") == 0) {
                        String path = getPathRelativeToProject(file);
                        DefaultListModel model = (DefaultListModel) jarList.getModel();
                        if(!model.contains(path) && path != null && !path.isEmpty()) {
                            model.addElement(path);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_addJarActionPerformed

    private void removeJarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeJarActionPerformed
        DefaultListModel model = (DefaultListModel) jarList.getModel();
        int index = jarList.getSelectedIndex();
        model.remove(index);
    }//GEN-LAST:event_removeJarActionPerformed

    private void pluginClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pluginClearButtonActionPerformed
        DefaultListModel model = (DefaultListModel) pluginList.getModel();
        model.clear();
    }//GEN-LAST:event_pluginClearButtonActionPerformed

    private void jarClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jarClearButtonActionPerformed
        DefaultListModel model = (DefaultListModel) jarList.getModel();
        model.clear();
    }//GEN-LAST:event_jarClearButtonActionPerformed

    private void addSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSourceButtonActionPerformed
        File home = new File(System.getProperty("user.home"));
        int returnVal = sourceFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = sourceFileChooser.getSelectedFiles();
            for(int i = 0; i < files.length; i++) {
                File file = files[i];
                if(file != null && file.isFile()) {
                    String absolutePath = file.getAbsolutePath();
                    if (absolutePath.length() > 7) {
                        String extension = absolutePath.substring(absolutePath.length() - 7);
                        if(extension.compareTo(".quorum") == 0) {
                            String path = getPathRelativeToProject(file);
                            DefaultListModel model = (DefaultListModel) sourceList.getModel();
                            if(!model.contains(path) && path != null && !path.isEmpty()) {
                                model.addElement(path);
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_addSourceButtonActionPerformed

    private void removeSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSourceButtonActionPerformed
        DefaultListModel model = (DefaultListModel) sourceList.getModel();
        int[] selectedIndices = sourceList.getSelectedIndices();
        for(int i = selectedIndices.length - 1; i >= 0 ; i--) {
            int index = selectedIndices[i];
            model.remove(index);
        }
    }//GEN-LAST:event_removeSourceButtonActionPerformed

    private void clearSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSourceButtonActionPerformed
        DefaultListModel model = (DefaultListModel) sourceList.getModel();
        model.clear();
    }//GEN-LAST:event_clearSourceButtonActionPerformed

    
    public String getPathRelativeToProject(File file) {
        String absolutePath = file.getAbsolutePath();
        QuorumProject proj = (QuorumProject) project;
        FileObject directory = proj.getProjectDirectory();
        String projectPath = FileUtil.toFile(directory).getAbsolutePath();
        Path sourceFile = Paths.get(projectPath);
        Path targetFile = Paths.get(absolutePath); 
        Path relativePath = sourceFile.relativize(targetFile);


        return relativePath.toString();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJar;
    private javax.swing.JButton addPlugin;
    private javax.swing.JButton addSourceButton;
    private javax.swing.JButton clearSourceButton;
    private javax.swing.JRadioButton compiledWebProjectRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jarClearButton;
    private javax.swing.JFileChooser jarFileChooser;
    private javax.swing.JList jarList;
    private javax.swing.JRadioButton legoRadioButton;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton pluginClearButton;
    private javax.swing.JFileChooser pluginFolderChooser;
    private javax.swing.JList pluginList;
    private javax.swing.JRadioButton projectRadioButton;
    private javax.swing.ButtonGroup projectTypeButtonGroup;
    private javax.swing.JButton removeJar;
    private javax.swing.JButton removePlugin;
    private javax.swing.JButton removeSourceButton;
    private javax.swing.JFileChooser sourceFileChooser;
    private javax.swing.JList<String> sourceList;
    private javax.swing.JRadioButton webBrowserRadioButton;
    // End of variables declaration//GEN-END:variables
}

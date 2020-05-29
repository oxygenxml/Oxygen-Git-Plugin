package com.oxygenxml.git.view;

import java.io.File;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.Test;

import com.oxygenxml.git.service.PushResponse;
import com.oxygenxml.git.service.entities.FileStatus;
import com.oxygenxml.git.service.entities.GitChangeType;
import com.oxygenxml.git.utils.TreeFormatter;
import com.oxygenxml.git.view.ChangesPanel.ResourcesViewMode;
import com.oxygenxml.git.view.event.GitCommand;

/**
* Test cases related to the actions performed
* on the staged/unstaged resources seen in the flat view.
*/
public class TreeView3Test extends FlatViewTestBase {
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    stagingPanel.getUnstagedChangesPanel().setResourcesViewMode(ResourcesViewMode.TREE_VIEW);
    stagingPanel.getStagedChangesPanel().setResourcesViewMode(ResourcesViewMode.TREE_VIEW);
  }
  
  /**
   * Invokes the change button on the view.
   * 
   * @param stage <code>true</code> to mode files from UnStage to the IDNEX.
   * <code>false</code> to move files out of the INDEX.
   * @param index Index in the table of the file to move.
   */
  private void change(boolean stage, String fileToSelect) {
    ChangesPanel changesPanel = null;
    if (stage) {
      changesPanel = stagingPanel.getUnstagedChangesPanel();
    } else {
      changesPanel = stagingPanel.getStagedChangesPanel();
    }
    
    JTree filesTree = changesPanel.getTreeView();
    
    JButton ssButton = changesPanel.getChangeSelectedButton();
    
    expandAll(filesTree);
    
    TreePath treePath = TreeFormatter.getTreePath(filesTree.getModel(), fileToSelect);
    filesTree.getSelectionModel().setSelectionPath(treePath);
    flushAWT();
    
    assertTrue(ssButton.isEnabled());
    ssButton.doClick();
    
    waitForScheduler();
  }
  
  private static void expandAll(JTree tree) {
    TreeFormatter.expandAllNodes(tree, 0, tree.getRowCount());
  }
  
  /**
   * Stage and UnStage a newly created file.
   *  
   * @throws Exception If it fails.
   */
  @Test
  public void testStageUnstage_ModifiedFile() throws Exception {
    /**
     * Local repository location.
     */
    String localTestRepository = "target/test-resources/testStageUnstage_ModifiedFile_local";
    
    /**
     * Remote repository location.
     */
    String remoteTestRepository = "target/test-resources/testStageUnstage_ModifiedFile_remote";
    
    // Create repositories
    Repository remoteRepo = createRepository(remoteTestRepository);
    Repository localRepo = createRepository(localTestRepository);
    // Bind the local repository to the remote one.
    bindLocalToRemote(localRepo , remoteRepo);
    
    // Add it to the index.
    
    File file = createNewFile(localTestRepository, "test.txt", "remote");
    
    add(new FileStatus(GitChangeType.UNKNOWN, "test.txt"));
    waitForScheduler();
    
    assertTreeModels("", "ADD, test.txt");
    
    gitAccess.commit("First version.");
    
    assertTreeModels("", "");
    
    // Change the file.
    setFileContent(file, "index content");
    
    assertTreeModels("MODIFIED, test.txt", "");
    //------------
    // Add to INDEX (Stage)
    //------------
    add(new FileStatus(GitChangeType.MODIFIED, "test.txt"));
    
    assertTreeModels("", "CHANGED, test.txt");
    
    //-----------------
    // Change the file again. It will appear in the index as well.
    //------------------
    setFileContent(file, "modified content");
    
    assertTreeModels(
        "MODIFIED, test.txt", 
        "CHANGED, test.txt");
    
    //------------------
    // Unstage the file from the INDEX.
    //------------------
    change(false, "test.txt");
    
    assertTreeModels(
        "MODIFIED, test.txt", 
        "");
  }
}
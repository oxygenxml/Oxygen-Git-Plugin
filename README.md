# Oxygen Git Client add-on

This add-on contributes a Git client directly in Oxygen XML Editor/Author/Developer, as a side-view, available in the Editor and DITA perspectives.

This add-on is compatible with Oxygen XML Editor/Author/Developer version 20.1 or higher. 

## Installation

To install the add-on, follow these instructions:

1. Go to **Help > Install new add-ons...** to open an add-on selection dialog box.
2. Enter or paste https://raw.githubusercontent.com/oxygenxml/Oxygen-Git-Plugin/master/build/addon.xml in the **Show add-ons from** field.
3. Select the **Git Client** add-on and click **Next**.
4. Select the **I accept all terms of the end user license agreement** option and click **Finish**.
5. Restart the application.

**Result:** A **Git Staging** view will now be available in Oxygen. If it is not visible, go to **Window > Show View** and select **Git Staging**. This view acts as a basic Git client integrated directly in Oxygen, and it provides support for committing changes to a Git repository, comparing and merging changes, resolving conflicts, and other Git commands.

The add-on can also be installed using the following alternative installation procedure:
1. Go to the [Releases page](https://github.com/oxygenxml/oxygen-git-plugin/releases/latest) and download the `git.support-{version}-plugin.jar` file.
2. Unzip it inside `{oXygenInstallDir}/plugins`. Make sure you don't create any intermediate folders. After unzipping the archive, the file system should look like this: `{oXygenInstallDir}/plugins/git.support-x.y.z`, and inside this folder, there should be a `plugin.xml`file.

## Cloning a repository

Click the **Clone new repository** button (it has a '+' sign as the icon) and provide the following:
- **Repository URL**: The URL of the remote repository to be cloned.
- **Checkout branch**: A specific branch of the repository that is being cloned. The default branch will be cloned if another one is not specified.
- **Destination path**: The local path were the repository will be cloned.

After cloning a repository, it will automatically be set as the current working copy.

## Authentication

This Git client supports both **HTTPS** and **SSH** connections to **GitHub, GitLab, Bitbucket**, etc.

To access the remote repository, you will need to provide your credentials (if not using unprotected SSH keys). If no credentials are found, the add-on will ask for them. If you have the _two-factor authentication_ enabled for GitHub, you must go to your GitHub account, **Settings > Developer settings > Personal access tokens > Generate new token**, and back in the **Git Staging** view in Oxygen, use the generated token value as the authentication password when asked for your credentials.

If, for example, you have been using a GitHub account but you decide to switch to another GitHub account, you would need to reset your credentials so that you will be prompted for new ones. This is because we currently only store one set of credentials for each Git platform/server.

To reset your credentials, go to the toolbar at the top of the _Git Staging_ side-view, click the settings icon (a cogwheel), and select **Reset all credentials**.

## Selecting a working copy

Click the **Browse** button to the right of the **Working copy** combo box to select a working copy from your file system. The selected folder must be a Git repository.

## Switching between local branches

To switch between local branches, use the **Change branch** action from the toolbar of the **Git Staging** view and select the desired branch from the presented combo box.

New branches can be created from the History table using the **Create branch** action in the contextual menu.

## Working with submodules

To open and work with a Git submodule, use the **Submodules** action from the toolbar and select the desired submodule from the presented combo box.

## Showing the current branch history

To show the history of the current branch, invoke the **Show current branch history** action from the toolbar (look for the clock icon). This will open the **Git History** view at the bottom of Oxygen.

The **Git History** view presents all the affected resources for each commit in a list, in the bottom-right area. For each resource, the following actions are available in the contextual menu:
- **Open** (available for added and modifed resources): This action opens the selected resource.
- **Open previous version** (available for deleted resources): This action opens the version of the selected resource from before its deletion.
- **Compare with previous version** (available for modified resources): This action compares the selected version of the selected resource with the previous one using the **Oxygen Diff Files** tool.
- **Compare with working copy version** (available for modified resources): This action compares the selected version of the selected resource with the current one using the **Oxygen Diff Files** tool.
- **Compare with each other** (available when selecting 2 versions of a single file): This action compares the selected versions with each other using the **Oxygen Diff Files** tool.

Moreover, new branches can be created from the History table using the **Create branch** action in the contextual menu.

## Blame

The contextual menu of each unstaged resource contains a **Show blame** action that opens the selected resource in Oxygen's main editing area and colors the editor lines with different colors based on the revision information. Selecting a line in the opened editor will highlight the corresponding entry from the history table in the **Git History** side-view.

This action is also available in the contextual menu of the current editor and of the Git resources from the **Project** side-view.

## Unstaged resources area

In the *unstaged resources area* (the top pane), you will see all the modifications that have occurred in your working copy (files that have been modified, new files, and deleted files) and are not part of the next commit.
- Various actions are available in the contextual menu (**Open**, **Open in compare editor**, **Stage**, **Discard**, **Show history**, **Show blame**, and more).
- You can stage all the files by clicking the **Stage All** button or you can stage certain files by selecting and clicking the **Stage Selected** button.
- You can switch between the flat (table) view and the tree view by clicking on the **Switch to tree/flat view** button positioned to the right of the staging buttons.

## Staged resources area

In the *staged resources area*, you will see all the resources that are ready to be committed. The files from this area can be unstaged and sent back to the *unstaged resources area*. The *staged resources area* has actions similar to those from the *unstaged resources area*, with the exception of the **Show history** and **Show blame** actions that are only available in the *unstqaged resources area*.

## Comparing changes and conflict resolution

At any time, if you want to see the differences between the last commit and your current modifications, you can double-click a file from either the *unstaged resources area* or *staged resources area*, and the [Oxygen's Diff](https://www.oxygenxml.com/xml_editor/xml_diff_and_merge.html) window will appear and highlight the changes.

If the file has a conflict (has been modified both by you and another), [Oxygen's Three Way Diff](https://www.oxygenxml.com/xml_editor/xml_diff_and_merge.html) will show a comparison between the local change, the remote change, and the original base revision.

## Committing

After staging the files, on the bottom of the view, you can provide a commit message and commit them to your local repository. For convenience, you can also select a previously provided message.

In the toolbar above the _Commit message_ text area, there are a few toggle buttons that affect your commit if they are enabled:
- **Amend last commit:** Enabling this option is a convenient way to modify the most recent commit. It lets you combine staged changes with the previous commit instead of creating an entirely new commit. It can also be used to simply edit the previous commit message without changing its snapshot.
- **Automatically push changes to remote when committing:** If this option is enabled, when a commit is performed, the committed changes are also pushed to the remote repository.

## Push / Pull (with merge or rebase)

To push your local repository changes to the remote repository, use the **Push** button from the view's toolbar (up arrow). 

To bring the changes from the remote repository into your local repository, use one of the **Pull** actions from the same toolbar (down arrow). You can choose between **Pull (merge)** and **Pull (rebase)**. The invoked action is promoted as the current action of the toolbar button.

**Note:** When pushing a local branch that does not have a corresponding remote branch, a remote branch will automatically be created with the same name as the local branch.

## File conflicts solving flow

After editing a file, committing it to the local repository, and trying to push it to the remote repository, if a warning appears about not being up-to-date with the repository, follow these steps:

1. Pull the data from the repository using one of the **Pull** actions.
2. In the **Unstaged** area, select each conflicted file and resolve the conflicts. You can do this, for example, by opening the conflicted files in the compare editor, either by double-clicking on them or by using the contextual menu action, and then choose what changes you want to keep and discard, and save the document. You can also use the **Resolve using Mine**, **Resolve using Theirs**, or **Mark as resolved** actions from the contextual menu of a resource. 
3. If you choose to use the compare editor, after you close it, the file will be staged automatically and moved to the **Staged** area.

At this point, the next actions depend on which **Pull** action was chosen:

 - **Pull (merge)**:
    1. When all the conflicts are resolved and no more files are left in the **Unstaged** area, the changes can be committed.
    2. Enter a message and commit. You will now have new changes to push.
    3. Push the changes to the remote repository.

 - **Pull (rebase)**:
    1. When all the conflicts are resolved, press the **Continue rebase** button.
    2. You can abort the *Pull with rebase* action by pressing the **Abort rebase** button. This will revert the repository to its state before trying to pull.
    
## The Project view and the current editor

For resources from Git repositories, this add-on also contributes a variety of actions in the contextual menus of the **Project** side-view and the current editor (Text and Author pages). These actions include: **Show history**, **Show blame**, **Git Diff** (only in the Project view), and **Commit** (only in the Project view).

Copyright and License
---------------------
Copyright 2018 Syncro Soft SRL.

This project is licensed under [Apache License 2.0](https://github.com/oxygenxml/oxygen-git-plugin/blob/master/LICENSE)

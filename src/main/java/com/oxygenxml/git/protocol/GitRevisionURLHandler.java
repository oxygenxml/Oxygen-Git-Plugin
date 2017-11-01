package com.oxygenxml.git.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.log4j.Logger;
import org.eclipse.jgit.lib.ObjectId;

import com.oxygenxml.git.options.OptionsManager;
import com.oxygenxml.git.service.Commit;
import com.oxygenxml.git.service.GitAccess;
import com.oxygenxml.git.utils.FileHelper;

/**
 * Handler for the "git" protocol. Can be used to for the three way diff on the
 * remote commit, last local commit and the base. It is also used for the 2 way
 * diff with the Last Local Commit
 * 
 */
public class GitRevisionURLHandler extends URLStreamHandler {

	/**
	 * Logger for logging.
	 */
	private static Logger logger = Logger.getLogger(GitRevisionURLHandler.class);
	
	/**
	 * The git protocol
	 */
	public static final String GIT_PROTOCOL = "git";
	
	/**
	 * Connection class for XML files in archives.
	 */
	private static class GitRevisionConnection extends URLConnection {

		/**
		 * The "file object" used to get the input stream from. The jgit library
		 * uses this kind of object(ObjectId) to point to commits.
		 */
		private ObjectId fileObject;

		/**
		 * Path obtained from the URL. The path is relative to the selected
		 * repository
		 */
		private String path;

		/**
		 * The host which is used to let the user write in the diff tool
		 */
		private String currentHost;

		/**
		 * Construct the connection
		 * 
		 * @param url
		 *          The URL
		 */
		protected GitRevisionConnection(URL url) throws IOException {
			super(url);
			setDoOutput(true);
				
			decode(url);
		}

		/**
		 * Extracts the Git identifiers from the URL.
		 * 
		 * @param url
		 *          - URL to get the host
		 * @return the URL host
		 */
		private void decode(URL url) throws IOException {
			path = url.getPath();
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			currentHost = url.getHost();
			if(currentHost.equals("CurrentSubmodule") || currentHost.equals("PreviousSubmodule")) {
				path = path.replace(".txt", "");
			}
			
			GitAccess gitAccess = GitAccess.getInstance();
			if (VersionIdentifier.MINE.equals(currentHost)) {
				fileObject = gitAccess.getCommit(Commit.MINE, path);
			} else if (VersionIdentifier.LAST_COMMIT.equals(currentHost)) {
				fileObject = gitAccess.getCommit(Commit.LOCAL, path);
			} else if (VersionIdentifier.THEIRS.equals(currentHost)) {
				fileObject = gitAccess.getCommit(Commit.THEIRS, path);
			} else if (VersionIdentifier.BASE.equals(currentHost)) {
				fileObject = gitAccess.getCommit(Commit.BASE, path);
			} else if (VersionIdentifier.CURRENT_SUBMODULE.equals(currentHost)) {
				fileObject = gitAccess.submoduleCompare(path, false);
			} else if (VersionIdentifier.PREVIOUSLY_SUBMODULE.equals(currentHost)) {
				fileObject = gitAccess.submoduleCompare(path, true);
			} else {
				throw new IOException("Not able to extract GIT data from: " + getURL());
			}
			
			if (fileObject == null) {
				throw new IOException("Unable to obtain commit ID for: " + getURL());
			}
		}

		/**
		 * Returns an input stream that reads from this open connection.
		 * 
		 * @return the input stream
		 */
		public InputStream getInputStream() throws IOException {
			if (VersionIdentifier.CURRENT_SUBMODULE.equals(currentHost) 
					|| VersionIdentifier.PREVIOUSLY_SUBMODULE.equals(currentHost)) {
				String commit = "Subproject commit " + fileObject.getName();
				File temp = File.createTempFile("submodule", ".txt");
				PrintWriter printWriter = new PrintWriter(temp);
				printWriter.println(commit);
				printWriter.close();
				return new FileInputStream(temp);
				//return IOUtils.toInputStream(commit, "UTF-8");
				//return new ByteArrayInputStream(commit.getBytes(StandardCharsets.UTF_8));
			}
			
			GitAccess gitAccess = GitAccess.getInstance();
			InputStream inputStream = gitAccess.getInputStream(fileObject);
			gitAccess.setRepository(OptionsManager.getInstance().getSelectedRepository());
			return inputStream;
		}

		/**
		 * Returns an output stream that writes to this connection.
		 * 
		 * @return the output stream
		 */
		public OutputStream getOutputStream() throws IOException {
			if (VersionIdentifier.MINE.equals(currentHost)) {
				URL fileContent = FileHelper.getFileURL(path);
				return fileContent.openConnection().getOutputStream();
			}
			throw new IOException("Writing is permitted only in the local file.");
		}

		public void connect() throws IOException {
		}

		/**
		 * @see java.net.URLConnection#getContentLength()
		 */
		@Override
		public int getContentLength() {
			return -1;
		}

		/**
		 * @see java.net.URLConnection#getContentType()
		 */
		@Override
		public String getContentType() {
			// Let Oxygen decide.
			return null;
		}
	}


	/**
	 * Creates and opens the connection
	 * 
	 * @param u
	 *          The URL
	 * @return The connection
	 */
	protected URLConnection openConnection(URL u) throws IOException {
		URLConnection connection = new GitRevisionConnection(u);
		return connection;
	}

	/**
	 * Constructs an URL for the diff tool
	 * 
	 * @param locationHint A constant from {@link GitFile}
	 * @param fileLocation The file location relative to the repository
	 * @return the URL of the form git://gitFile/fileLocation
	 * 
	 * @throws MalformedURLException Unnable to build the URL.
	 */
	public static URL encodeURL(String locationHint, String fileLocation) throws MalformedURLException {
		URL url = new URL ("git://" + locationHint + "/" + fileLocation);
		if(locationHint.equals(VersionIdentifier.CURRENT_SUBMODULE) || locationHint.equals(VersionIdentifier.PREVIOUSLY_SUBMODULE)) {
			// Add an extension to mimic a file. The content of this file will be the commit ID linked with the SUBMODULE.
			url = new URL("git://" + locationHint + "/" + fileLocation +".txt");
		}

		return url;
	}
}

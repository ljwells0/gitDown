/**
 * @author Liam Wells (ljwells0)
 */
package gitDown;
// Java
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
// Eclipse JGit
import org.eclipse.jgit.api.Git;
// Eclipse JGit (Exception)
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
// Json
import org.json.JSONArray;
import org.json.JSONObject;
/**
 * Batch class
 */
public class Batch {
	public static String jsonIn;
	public static JSONArray jsonArray;
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * Get json result from GitHub API
		 */
		try {
			URL url = new URL("https://api.github.com/users/" + args[0] + "/repos");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			jsonArray = new JSONArray(br.readLine());
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		/**
		 * For each repository returned by the github api a new json object is created which is then parsed to obtain the value from the html_url key
		 */
		jsonArray.forEach(item -> {
			JSONObject jsonObject = new JSONObject(item.toString());
			String repoURL = jsonObject.getString("html_url");
			String repoName = jsonObject.getString("name");
			File downloadPath = null;
			try {
				downloadPath = new File(args[1] + "/" + repoName);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println("Cloning '" + repoName + "'" + " into " + downloadPath);
			try {
				Git.cloneRepository().setURI(repoURL).setDirectory(downloadPath).call();
			} catch (InvalidRemoteException e) {
				e.printStackTrace();
			} catch (TransportException e) {
				e.printStackTrace();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		});
		System.out.println("Cloning complete...");
	}
}
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dataproc.v1.Cluster;
import com.google.cloud.dataproc.v1.ClusterConfig;
import com.google.cloud.dataproc.v1.ClusterControllerClient;
import com.google.cloud.dataproc.v1.ClusterControllerSettings;
import com.google.cloud.dataproc.v1.ClusterOperationMetadata;
import com.google.cloud.dataproc.v1.HadoopJob;
import com.google.cloud.dataproc.v1.InstanceGroupConfig;
import com.google.cloud.dataproc.v1.Job;
import com.google.cloud.dataproc.v1.JobControllerClient;
import com.google.cloud.dataproc.v1.JobControllerSettings;
import com.google.cloud.dataproc.v1.JobMetadata;
import com.google.cloud.dataproc.v1.JobPlacement;
import com.google.cloud.dataproc.v1.SubmitJobRequest;
import com.google.cloud.storage.StorageOptions;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;


// MAKE CONSTANTS FOR ALL BUCKET/FILES/CLUSTERS/SERVERS/PROJECTIDS
public class ApplicationInterface {
	private static final String dockerServiceAccountJSON = "enter here";
	private static final String projectID = "enter here";
	private static final String clusterName = "enter here";
	private static final String region = "enter here";
	
	private static class ApplicationScreen implements ActionListener  {
		JFrame frame;
		JCheckBox hugoCheck;
		JCheckBox shakespeareCheck;
		JCheckBox tolstoyCheck;
		JButton submitFiles;
		JButton loadFiles;
		String hugoURI;
		String shakespeareURI;
		String tolstoyURI;
		
		public ApplicationScreen() {
			
			frame = new JFrame();
		    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    hugoCheck = new JCheckBox("Hugo");
		    shakespeareCheck = new JCheckBox("Shakespeare");
		    tolstoyCheck = new JCheckBox("Tolstoy");
		    submitFiles = new JButton("Submit");
		    submitFiles.addActionListener(this);
		
		    frame.getContentPane().add(hugoCheck, BorderLayout.LINE_START);
		    frame.getContentPane().add(shakespeareCheck);
		    frame.getContentPane().add(tolstoyCheck, BorderLayout.LINE_END);
		    frame.getContentPane().add(submitFiles, BorderLayout.PAGE_END);
		    frame.pack();
		    frame.setVisible(true);
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(submitFiles)) {
				if(hugoCheck.isSelected() || shakespeareCheck.isSelected() || tolstoyCheck.isSelected()) {
					// Add code for generating files/indices, but for now just send next GUI screen
					
					GoogleCredentials credentials;
					Storage storage = null;
					try {
						credentials = GoogleCredentials
								.fromStream(new FileInputStream(dockerServiceAccountJSON))
						        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
						 storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					//Blob blob = storage.get("dataproject1660", "yes/test.txt");
					//String dir = new String(blob.getContent());
					//System.out.println(dir);
					
					frame.getContentPane().removeAll();
					frame.getContentPane().revalidate();
					frame.getContentPane().repaint();
					frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
					
					hugoURI = null;
					shakespeareURI = null;
					tolstoyURI = null;
					
					if(hugoCheck.isSelected()) {
						JLabel hugoLabel = new JLabel("Hugo loaded!");
						frame.getContentPane().add(hugoLabel);
					}
					if(shakespeareCheck.isSelected()) {
						JLabel shakespeareLabel = new JLabel("Shakespeare loaded!");
						frame.getContentPane().add(shakespeareLabel);
					}
					if(tolstoyCheck.isSelected()) {
						JLabel tolstoyLabel = new JLabel("Tolstoy loaded!");
						frame.getContentPane().add(tolstoyLabel);
					}
					loadFiles = new JButton("Load Engine");
					loadFiles.addActionListener(this);
					
					frame.getContentPane().add(loadFiles);
				}
			} else if(e.getSource().equals(loadFiles)) {
				try {
					submitJob(projectID, region, clusterName);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		private static void submitJob(
			      String projectId, String region, String clusterName)
			      throws IOException, InterruptedException {
				  System.out.println("hello2");
			    String myEndpoint = String.format("%s-dataproc.googleapis.com:443", region);

			    // Configure the settings for the job controller client.
			    JobControllerSettings jobControllerSettings =
			        JobControllerSettings.newBuilder().setEndpoint(myEndpoint).build();

			    // Create a job controller client with the configured settings. Using a try-with-resources
			    // closes the client,
			    // but this can also be done manually with the .close() method.
			    try (JobControllerClient jobControllerClient =
			        JobControllerClient.create(jobControllerSettings)) {
			    	System.out.println("hello3");
			      // Configure cluster placement for the job.
			      JobPlacement jobPlacement = JobPlacement
			    		  						.newBuilder()
			    		  						.setClusterName(clusterName)
			    		  						.build();
			      
			      System.out.println("hello4");
			      // Configure Spark job settings.
			      HadoopJob hadJob =
			          HadoopJob.newBuilder()
			              .setMainJarFileUri("gs://dataproc-staging-us-central1-221523287754-gmqsy530/jar/MapReduce.jar")
			              .addArgs("gs://dataproc-staging-us-central1-221523287754-gmqsy530/Data/Hugo/Hugo")
			              .addArgs("gs://dataproc-staging-us-central1-221523287754-gmqsy530/output")
			              .build();

			      Job job = Job.newBuilder().setPlacement(jobPlacement).setHadoopJob(hadJob).build();
			      
			      System.out.println("hello5");
			      // Submit an asynchronous request to execute the job.
			      OperationFuture<Job, JobMetadata> submitJobAsOperationAsyncRequest =
			          jobControllerClient.submitJobAsOperationAsync(projectId, region, job);
			      
			      jobControllerClient.submitJob(projectId, region, job);
			      System.out.println("hello6");
			      
			      Job response = submitJobAsOperationAsyncRequest.get();
			      System.out.println("hello7");
			      Matcher matches =
			          Pattern.compile("gs://(.*?)/(.*)").matcher(response.getDriverOutputResourceUri());
			      matches.matches();

			      Storage storage = StorageOptions.getDefaultInstance().getService();
			      Blob blob = storage.get(matches.group(1), String.format("%s.000000000", matches.group(2)));

			      System.out.println(
			          String.format("Job finished successfully: %s", new String(blob.getContent())));
			    } catch (Exception e) {
			      // If the job does not complete successfully, print the error message.
			      System.err.println(String.format("submitJob: %s ", e.getMessage()));
			    }
			  }
	}
  
	public static void main(String[] args) throws InterruptedException, IOException {
	    ApplicationScreen gui = new ApplicationScreen();
	    
	}
}
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.dataproc.v1.HadoopJob;
import com.google.cloud.dataproc.v1.Job;
import com.google.cloud.dataproc.v1.JobControllerClient;
import com.google.cloud.dataproc.v1.JobControllerSettings;
import com.google.cloud.dataproc.v1.JobMetadata;
import com.google.cloud.dataproc.v1.JobPlacement;
import com.google.cloud.dataproc.v1.SparkJob;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubmitJob {

  public static void submitJob() throws IOException, InterruptedException {
    // TODO(developer): Replace these variables before running the sample.
	System.out.println("hello1");
    String projectId = "cryptic-bivouac-309222";
    String region = "us-central1";
    String clusterName = "cluster-57b5";
    submitJob(projectId, region, clusterName);
  }

  public static void submitJob(
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
      //ssh to cluster
      HadoopJob sparkJob =
          HadoopJob.newBuilder()
              .setMainJarFileUri("gs://dataproc-staging-us-central1-221523287754-gmqsy530/jar/Test.jar")
              .build();

      Job job = Job.newBuilder().setPlacement(jobPlacement).setHadoopJob(sparkJob).build();
      
      System.out.println("hello5");
      // Submit an asynchronous request to execute the job.
      OperationFuture<Job, JobMetadata> submitJobAsOperationAsyncRequest =
          jobControllerClient.submitJobAsOperationAsync(projectId, region, job);
      System.out.println("hello6");
      Job response = submitJobAsOperationAsyncRequest.get();
      System.out.println("hello7");
      // Print output from Google Cloud Storage.
      Matcher matches =
          Pattern.compile("gs://(.*?)/(.*)").matcher(response.getDriverOutputResourceUri());
      matches.matches();

      Storage storage = StorageOptions.getDefaultInstance().getService();
      Blob blob = storage.get(matches.group(1), String.format("%s.000000000", matches.group(2)));

      System.out.println(
          String.format("Job finished successfully: %s", new String(blob.getContent())));

    } catch (ExecutionException e) {
      // If the job does not complete successfully, print the error message.
      System.err.println(String.format("submitJob: %s ", e.getMessage()));
    }
  }
  public static void main(String[] args) throws IOException, InterruptedException {
	  submitJob();
  }
}
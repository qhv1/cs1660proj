# cs1660proj
**How to run GUI:**
* Open a project inside of eclipse using the ApplicationInterface.java file, and import all the .jar inside the lib folders as external jar files for use
* In ecliplse marketplace under "Help" tab, install Google Cloud tools for the project and import the Storage model for the project
* Once all dependencies are included, add the projectID, region, etc (located top of the file) to complete the setup
* Using ecliplse, build a runnable jar file of the ApplicationInterface.java (name it ApplicationInterface.jar) and place it inside the same folder as the dockerfile
* Inside docker folder, configure DISPLAY variable and GOOGLE_APPLICATION_CREDENTIALS (You will need to generate a service account for the GOOGLE_APPLICATION_CREDENTIALS variable)
* Type "docker build "directory of the dockerfile"
* GUI should appear top left

**Setup Google Cloud:**
* Upload folder named "Data" into the google bucket of a cluster you have made
* Upload MapReduce.jar into that bucket
* Submit a job using the MapReduce.jar file provided, similar to how it is used in the video

Video: https://www.youtube.com/watch?v=OYmGN5RpqvI

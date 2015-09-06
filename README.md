# COSBAS-Client
Source code for the client part of the COSBAS, well anything related to the client


*To setup and run OpenCV with Jetbrains IntelliJ IDEA:
1) Click on File -> Project Structure (CTRL + ALT + SHIFT + S)
2) In the middle window, click on the green + (New Project Library) and select Java
3) Navigate to the directory where OpenCV was extracted. (i.e. F:\opencv\build\java\opencv-300.jar) and select opencv-300.jar
4) Next, to run OpenCV without a link error, select (double click) the application you want to run. 
5) In the menu pane, select "Run" then click on "Edit Configurations"
6) In the Application VM field, enter the following command: -Djava.library.path="F:\opencv\build\java\x64", where F:\opencv\build\java\x64 is the directory where opencv's x64 libraries are located.
7) Click Apply
8) In the terminal, run gradle bootrun.

* If gradle tells you to re-import a project due to missing dependencies, etc...
1) Go to View -> Tool Windows -> Gradle
2) In the window that pops up, click the blue refresh button (Refresh All Gradle Projects)
3) In the terminal, run Gradle Bootrun

For updating project, you need to pass arguments:

projectsFile: file containing all the project
projectConfigurationsFile: file containing all the project configurations
version: from the projectConfigurationsFile, enter the projectconfiguration name

mvn intact.devtools.maven-plugins:project-version-updater-maven-plugin:LATEST:update -DprojectsFile=projects.txt -DprojectConfigurationsFile=project-configurations.xml -Dversion=5.7 %*

For updating html files, you need to pass arguments:

outputFolder: folder where the html files will be created
projectConfigurationsFile: file containing all the project configurations

mvn intact.devtools.maven-plugins:project-version-updater-maven-plugin:LATEST:generate-doc -DoutputFolder=projects.txt -DprojectConfigurationsFile=project-configurations.xml %*

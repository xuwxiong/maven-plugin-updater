Scripts are created to automate POM updgare :

1) 1_load-wsbis.bat username password
   Load your project in the current directory. Projetcs are read from projectsScm.bat

2) 2_acceptbis.bat username password
	Accept all modification to prevent parallel versions

3) 3_upgradeversionh.bat
	Upgrade all version of projects defined in projects.txt and project-configurations_bp.xml
	
4) 4_commit-ws.bat : In Progress...
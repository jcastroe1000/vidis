== CONTENT ==
	1. Introduction
	2. License
		2.1 Terms of license
		2.2 Authors
		2.3 Co-Authors
	3. Install instructions
	4. Execution instructions

== Chapter 1: Introduction ==

	Vidis is a Java based Visualisation and Simulation Framework for Distributed Systems using Java and OpenGL via jogl.
	
== Chapter 2: License ==
	
	2.1 This program is licensed under GNU General Public License v3. Hence all files are copyrighted.
		
		Copyright (C) 2009 Dominik Psenner, Christoph Caks
		
		This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
		This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
		You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
		
	2.2 Authors
		The authors of VIDIS are:
		
		Dominik Psenner, Christoph Caks
		
		Special thanks go to Dr. Max Berger, which supported us during the whole work.
		
	2.3 Co-Authors
		Furthermore we would like to thank Ralf Vandenhouten and Jesus M. Salvo Jr. They
		provided us a complete set of graph algorithm implementations which can be found
		in the package vidis.util.graphs and are derived from the former project OpenJGraph
		lead by Jesus M. Salvo Jr.
	
== Chapter 3: Install instructions ==

	3.1	Get latest Java release (www.java.com)
		You need at least Java version 1.5, Java version 1.6 is recommended.
		
== Chapter 4: Execution instructions ==

	4.1 VERY important notes
	
		4.1.1 Usage of build.xml
		
			4.1.1.1 Task 'launch-vis'
				Launches VIDIS using the correct native libraries for your OS if it is supported.
				Otherwise you may get your own libraries and modify the build file respectively.
				
			4.1.1.2 Task 'compile'
				This task compiles the current classes incrementally.
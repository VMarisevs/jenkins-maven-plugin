/*Copyright (C) 2015 Roland Hauser, <sourcepond@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package ch.sourcepond.maven.plugin.jenkins.it;

import java.util.List;

import org.junit.Before;

/**
 *
 */
public class HttpsNoCertificateCheckITCase extends HttpsITCase {

	/**
	 * @throws Exception
	 */
	@Before
	public void setupNoCertificateCheck() throws Exception {
		super.setup();
		mojo.setNoCertificateCheck(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sourcepond.maven.plugin.jenkins.it.ITCase#specifyExpectedStdout(java
	 * .util.List)
	 */
	@Override
	protected void specifyExpectedStdout(final List<String> pLines) {
		super.specifyExpectedStdout(pLines);
		pLines.add(
				2,
				"Skipping HTTPS certificate checks altogether. Note that this is not secure at all.");
	}
}

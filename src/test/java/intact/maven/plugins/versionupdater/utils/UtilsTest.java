package intact.maven.plugins.versionupdater.utils;

import static org.junit.Assert.assertEquals;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;

import java.io.FileOutputStream;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class UtilsTest 
{
	@Test
	public void testFieldsWithJsonViewAnnotation()
	{
		ProjectConfigurations configs = null;
		String[] fields = null;
		Class<? extends ContentViews.Normal> viewClass = null;
		String fieldsAsStr = null;
		configs = new ProjectConfigurations();
		
		viewClass = ContentViews.Bom.class;
		fields = Utils.getFieldsWithAnnotation(configs,viewClass);
		
		fieldsAsStr = StringUtils.join(fields,",");
		
		assertEquals("groupId,artifactId,version,type,scope", fieldsAsStr);
		
		viewClass = ContentViews.BomRelease.class;
		fields = Utils.getFieldsWithAnnotation(configs,viewClass);
		
		fieldsAsStr = StringUtils.join(fields,",");
		assertEquals("groupId,artifactId,version,release,toRelease,includedInBOMRelease", fieldsAsStr);
		
		viewClass = ContentViews.Release.class;
		fields = Utils.getFieldsWithAnnotation(configs,viewClass);
		
		fieldsAsStr = StringUtils.join(fields,",");
		assertEquals("groupId,artifactId,version,release,toRelease", fieldsAsStr);
		
		viewClass = ContentViews.Normal.class;
		fields = Utils.getFieldsWithAnnotation(configs,viewClass);
		
		fieldsAsStr = StringUtils.join(fields,",");
		assertEquals("groupId,artifactId,version,type,scope,release,toRelease,includedInBOMRelease", fieldsAsStr);
	}
	
	public static void testConnectionTo(String aURL) throws Exception {
        URL destinationURL = new URL(aURL);
        HttpsURLConnection conn = (HttpsURLConnection) destinationURL.openConnection();
        conn.connect();
        Certificate[] certs = conn.getServerCertificates();
        System.out.println("nb = " + certs.length);
        int i = 1;
        for (Certificate cert : certs) {
            System.out.println("Certificate is: " + cert);
            if(cert instanceof X509Certificate) {
                try {
                    ( (X509Certificate) cert).checkValidity();
                    System.out.println("Certificate is active for current date");
                    FileOutputStream os = new FileOutputStream("C:\\Users\\mhilali\\myCert"+i);
                    i++;
                    os.write(cert.getEncoded());
                } catch(CertificateExpiredException cee) {
                    System.out.println("Certificate is expired");
                }
            } else {
                System.err.println("Unknown certificate type: " + cert);
            }
        }
    }
	
	
	public static void main(String[] args)  throws Exception
	{
		UtilsTest.testConnectionTo("https://prod-nexus-b2eapp.iad.ca.inet:8443/nexus/content/");
		
	}

}

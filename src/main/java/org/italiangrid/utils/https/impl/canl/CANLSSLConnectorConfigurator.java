package org.italiangrid.utils.https.impl.canl;

import javax.net.ssl.SSLContext;

import org.eclipse.jetty.server.Connector;
import org.italiangrid.utils.https.JettySSLConnectorConfigurator;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.impl.SSLContextConnectorConfigurator;
import org.italiangrid.voms.util.CertificateValidatorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.NamespaceCheckingMode;
import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.impl.OpensslCertChainValidator;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;

/**
 * A {@link JettySSLConnectorConfigurator} that leverages the EMI Common Authentication Library.
 * 
 * @author andreaceccanti
 *
 */
public class CANLSSLConnectorConfigurator implements JettySSLConnectorConfigurator{
	
	public static final Logger log = LoggerFactory.getLogger(CANLSSLConnectorConfigurator.class);
		
	public static final NamespaceCheckingMode DEFAULT_NAMESPACE_CHECKING_MODE = NamespaceCheckingMode.EUGRIDPMA_AND_GLOBUS;
	
	SSLContextConnectorConfigurator configurator;
	
	X509CertChainValidatorExt certChainValidator;
	
	public CANLSSLConnectorConfigurator() {
		
	}
	
	public CANLSSLConnectorConfigurator(X509CertChainValidatorExt validator){
		this.certChainValidator = validator;
	}
	
	public Connector configureConnector(String host, int port,
			SSLOptions options) {
		
		
		try {
		
			PEMCredential serviceCredentials = new PEMCredential(options.getKeyFile(), 
					options.getCertificateFile(), 
					options.getKeyPassword());
			
			if (certChainValidator == null){
				CANLListener l = new CANLListener();
				
				certChainValidator = CertificateValidatorBuilder
						.buildCertificateValidator(options.getTrustStoreDirectory(), 
								l,
								l,
								options.getTrustStoreRefreshIntervalInMsec());
			}
			
			
			SSLContext sslContext = SocketFactoryCreator.getSSLContext(serviceCredentials, 
					certChainValidator, 
					null);
			
			configurator = new SSLContextConnectorConfigurator(sslContext);
			
			return configurator.configureConnector(host, port, options);
			
			
		} catch (Throwable t) {
			log.error("SSL initialization error!",t);
			return null;
		}
		
	}
}

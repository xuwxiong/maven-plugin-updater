<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
   <soapenv:Header/>
   <soapenv:Body>
      <bpel:BpelUpgradeProcessRequestMessage xmlns:ns1="http://soa.intactfc.com/BpelDeploymentTypes" xmlns:bpel="http://soa.intactfc.com/BpelUpgradeProcessMessage" xmlns:bpel1="http://soa.intactfc.com/BpelDeploymentTypes">
         <bpel1:UpgradeParams>
            <bpel1:current>dev-z</bpel1:current>
            <bpel1:source>dev-u</bpel1:source>
         </bpel1:UpgradeParams>
         <updateURNs>false</updateURNs>
         
         <ns1:bpelUpgradeParameters>
         
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.client.task</ns1:groupId>
               <ns1:artifactId>personallines-create-client-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>save-transaction-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>update-quote-new-business-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.client.task</ns1:groupId>
               <ns1:artifactId>personallines-update-client-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.ratingunderwriting.task</ns1:groupId>
               <ns1:artifactId>underwrite-contract-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>retrieve-contract-billing-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>generate-offer-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>search-contract-entity-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>retrieve-contract-distributor-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.client.task</ns1:groupId>
               <ns1:artifactId>personallines-search-client-contract-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>product-management-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.client.task</ns1:groupId>
               <ns1:artifactId>personallines-delete-client-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>generate-contract-task-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>copy-contract-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>create-quote-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>cancel-policy-automated-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>update-policy-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>can-add-risk-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.ratingunderwriting.task</ns1:groupId>
               <ns1:artifactId>get-premium-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>update-policy-automated-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>reinstate-policy-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>remove-party-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>update-contract-notes-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.ratingunderwriting.task</ns1:groupId>
               <ns1:artifactId>personallines-rating-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>delete-quote-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>submit-transaction-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.client.task</ns1:groupId>
               <ns1:artifactId>user-login-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>retrieve-contract-claims-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>remove-risk-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.client.task</ns1:groupId>
               <ns1:artifactId>consult-client-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>consult-quote-contract-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>complete-policy-transaction-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>convert-to-new-business-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>b2b-new-quote-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>cancel-policy-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>can-add-party-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.billing.task</ns1:groupId>
               <ns1:artifactId>calculate-payment-schedule-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>renew-policy-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>delete-transaction-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>add-party-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>add-risk-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>update-driving-experience-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
            <ns1:projectToDeploy xsi:type="ns1:ProjectToDeployNexus" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
               <ns1:groupToSearch>releases</ns1:groupToSearch>
               <ns1:groupId>intact.soa.plpolicy.task</ns1:groupId>
               <ns1:artifactId>update-pending-renewal-process</ns1:artifactId>
               <ns1:version>5.13.3.0.0.D11BUGFIX-SNAPSHOT</ns1:version>
            </ns1:projectToDeploy>
         </ns1:bpelUpgradeParameters>
      </bpel:BpelUpgradeProcessRequestMessage>
   </soapenv:Body>
</soapenv:Envelope>
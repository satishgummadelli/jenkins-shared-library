@Grab('org.apache.kafka:kafka-clients:2.1.0')
@GrabExclude(group = 'org.slf4j', module = 'slf4j-api')




import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.admin.NewTopic

def kerb() {
    System.setProperty("java.security.auth.login.config", "/var/jenkins_home/jaas1.conf")
    props = new HashMap<String, Object>([(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG): 'kafka-3-node-0.ujgcetklw1vu1ail22tmlbzzad.zx.internal.cloudapp.net:9092,kafka-3-node-1.ujgcetklw1vu1ail22tmlbzzad.zx.internal.cloudapp.net:9092,kafka-3-node-2.ujgcetklw1vu1ail22tmlbzzad.zx.internal.cloudapp.net:9092', (AdminClientConfig.CLIENT_ID_CONFIG): UUID.randomUUID().toString()])

    props.put("security.protocol", "SASL_SSL");
    props.put("ssl.truststore.location", "/usr/share/jenkins/ref/clienttruststore.jks");
    props.put("ssl.truststore.password", "confluent");
    props.put("sasl.mechanism", "GSSAPI");
    props.put("sasl.kerberos.service.name", "cp-kafka");

    AdminClient adminClient
    try {
        adminClient = KafkaAdminClient.create(props)
        println('finished client')
        //adminClient.createTopics([new NewTopic("satish-test", 3, 1)]).all().get()
        println(adminClient.listTopics().names().get().sort())

    } finally {
        if (adminClient != null)
            adminClient.close()
    }

}


def call( Map args ) {
    node {
      stage('Kerb') {
        sh 'cat /var/jenkins_home/jaas1.conf'
        kerb()
      }
    }
}

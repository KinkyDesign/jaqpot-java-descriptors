FROM jboss/wildfly

USER root

EXPOSE 8080
EXPOSE 9990

ADD ./target/jaqpot-java-descriptors-1.0.0.war /opt/jboss/wildfly/standalone/deployments/

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
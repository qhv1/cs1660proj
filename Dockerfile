FROM openjdk:8-oraclelinux7
COPY . /usr/src/myapp
ENV DISPLAY *IP HERE*:0.0
ENV GOOGLE_APPLICATION_CREDENTIALS *JSON DIRECTORY HERE*
RUN yum -y install libX11-devel.x86_64
RUN yum -y install libXext.x86_64
RUN yum -y install libXrender.x86_64
RUN yum -y install libXtst.x86_64
WORKDIR /usr/src/myapp
RUN java -jar ApplicationInterface.jar
CMD ["java", "-jar", "ApplicationInterface.jar"]

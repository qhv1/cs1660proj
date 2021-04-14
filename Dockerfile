FROM openjdk:8-oraclelinux7
COPY . /usr/src/myapp
ENV DISPLAY 10.0.0.148:0.0
ENV GOOGLE_APPLICATION_CREDENTIALS /usr/src/myappcryptic-bivouac-309222-46d3b58b228e.json
RUN yum -y install libX11-devel.x86_64
RUN yum -y install libXext.x86_64
RUN yum -y install libXrender.x86_64
RUN yum -y install libXtst.x86_64
WORKDIR /usr/src/myapp
RUN java -jar ApplicationInterface.jar
CMD ["java", "-jar", "ApplicationInterface.jar"]
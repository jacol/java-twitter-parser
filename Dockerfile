FROM java:8
EXPOSE 8080
ADD /out/artifacts/maia_jar/maia.jar maia.jar
ENTRYPOINT ["java","-jar","maia.jar"]
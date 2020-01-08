check java presence using "java --version" ; shouild be more than 11.

git clone this repo

cd SpringBoot-fileUpload

sudo apt-get install maven

mvn clean install

ll ./target/

cp ./target/VideoProcessingFFMpeg-0.0.1.jar ../

cd ..

chmod +x ./VideoProcessingFFMpeg-0.0.1.jar

cd /home/test/webservice/SpringBoot-fileUpload/src/main/resources

edit application.properties and paste in working directory

cd /home/test/webservice/SpringBoot-fileUpload/src/main/java/com/zoho/zlabs/service

edit FileStorageService.java for src and out directories

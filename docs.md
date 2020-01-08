## Requirements

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

keep needed bashfile metadata_old.sh in working directory

## Run following command

java -jar VideoProcessingFFMpeg-0.0.1.jar --spring.config.location=./application.properties

## Commandline arguments

/home/test/webservice/metadata_old.sh /tmp/video1_min.mp4 > /tmp/video1_min.mp4.txt

## Other edits if required

cd /home/test/webservice/SpringBoot-fileUpload/src/main/java/com/zoho/zlabs/service

edit FileStorageService.java for src and out directories

cd /home/test/webservice/SpringBoot-fileUpload/src/main/resources/static

edit indel.html for outpot names of web page

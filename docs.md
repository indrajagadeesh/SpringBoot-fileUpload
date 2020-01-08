git clone this repo

cd SpringBoot-fileUpload

sudo apt-get install maven

sudo su

mvn clean install

ll ./target/

sudo cp ./target/VideoProcessingFFMpeg-0.0.1.jar ../

cd ..

sudo chmod +x ./VideoProcessingFFMpeg-0.0.1.jar

sudo chown test ./VideoProcessingFFMpeg-0.0.1.jar


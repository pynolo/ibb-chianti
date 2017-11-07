# Package GWT user interface
rm -R src/main/webapp/greeter
rm -R src/main/webapp/WEB-INF/lib
rm -R src/main/webapp/WEB-INF/classes
mvn clean compile war:inplace package -Pprod
mv target/ibb-greeter.war ~/workspace/
rm -R src/main/webapp/WEB-INF/lib
rm -R src/main/webapp/WEB-INF/classes


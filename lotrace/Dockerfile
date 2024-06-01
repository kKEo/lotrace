FROM openjdk:11-jdk

RUN apt-get update && apt-get install -y wget unzip

RUN mkdir /android-sdk
RUN wget https://dl.google.com/android/repository/commandlinetools-linux-7583922_latest.zip -O /sdk-tools.zip
RUN unzip /sdk-tools.zip -d /android-sdk/cmdline-tools
RUN mv /android-sdk/cmdline-tools/cmdline-tools /android-sdk/cmdline-tools/latest
RUN rm /sdk-tools.zip

ENV ANDROID_HOME /android-sdk
ENV PATH ${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${PATH}

RUN yes | sdkmanager --licenses

RUN sdkmanager "platform-tools" "platforms;android-31" "build-tools;31.0.0"

RUN wget https://services.gradle.org/distributions/gradle-7.3.3-bin.zip -P /tmp
RUN unzip /tmp/gradle-7.3.3-bin.zip -d /opt
RUN ln -s /opt/gradle-7.3.3/bin/gradle /usr/bin/gradle

ENV GRADLE_HOME /opt/gradle-7.3.3
WORKDIR /workspace

#CMD ["gradle", "init", "--type", "basic"]
CMD ["gradle", "init", "--type", "java-application"]



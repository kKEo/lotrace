FROM openjdk:11-jdk

RUN apt-get update && apt-get install -y wget unzip

RUN mkdir /android-sdk
RUN wget https://dl.google.com/android/repository/commandlinetools-linux-7583922_latest.zip -O /sdk-tools.zip
RUN unzip /sdk-tools.zip -d /android-sdk/cmdline-tools
RUN mv /android-sdk/cmdline-tools/cmdline-tools /android-sdk/cmdline-tools/latest
RUN rm /sdk-tools.zip

ENV ANDROID_SDK_ROOT /android-sdk
ENV PATH ${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools:${PATH}

RUN yes | sdkmanager --licenses

RUN sdkmanager "platform-tools" "platforms;android-31" "build-tools;31.0.0"

COPY . /lotrace
WORKDIR /lotrace

#RUN ./gradlew clean assembleDebug


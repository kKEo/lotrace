
build:
	docker build . -t gradle-android:0.1 
init:
	docker run --rm -v "$(PWD):/workspace" -it gradle-android:0.1 /bin/bash
compile:
	docker run --rm -v "$(PWD):/workspace" -it gradle-android:0.1 ./gradlew clean assembleDebug
serve:
	cd app/build/outputs/apk && python3 -m http.server

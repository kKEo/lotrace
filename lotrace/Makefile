
pwd?=$(shell pwd)

build:
	docker build . -t gradle-android:0.1 


init:
	docker run --rm -v "$(pwd):/workspace" -it gradle-android:0.1 /bin/bash

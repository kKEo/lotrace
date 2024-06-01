build:
	docker build . -t android-build


run:
	docker run --rm -v "$(PWD)/lotrace:/workspace" -ti android-build bash


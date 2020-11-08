.DEFAULT_GOAL := all
.PHONY: all
all: clean output-all deploy

.PHONY: compile
compile: compile-core compile-characters compile-plugins
.PHONY: build
build: build-core build-characters build-plugins
.PHONY: clean
clean: clean-core clean-characters clean-plugins clean-output
.PHONY: output-all
output-all: output-core output-characters output-plugins
.PHONY: deploy
deploy: deploy-plugins

.PHONY: jpackage
jpackage: jpackage-core

.PHONY: characters
characters: kokori
.PHONY: compile-characters
compile-characters: compile-kokori
.PHONY: build-characters
build-characters: build-kokori
.PHONY: clean-characters
clean-characters: clean-kokori
.PHONY: output-characters
output-characters: output-kokori

.PHONY: plugins
plugins: dev-plugin console-plugin tianxing-chatbot-plugin wqy-font-plugin r18-plugin debug-plugin
.PHONY: compile-plugins
compile-plugins: compile-dev-plugin compile-console-plugin compile-tianxing-chatbot-plugin compile-wqy-font-plugin compile-r18-plugin compile-debug-plugin
.PHONY: build-plugins
build-plugins: build-dev-plugin build-console-plugin build-tianxing-chatbot-plugin build-wqy-font-plugin build-r18-plugin build-debug-plugin
.PHONY: clean-plugins
clean-plugins: clean-dev-plugin clean-console-plugin clean-tianxing-chatbot-plugin clean-wqy-font-plugin clean-r18-plugin clean-debug-plugin
.PHONY: output-plugins
output-plugins: output-dev-plugin output-console-plugin output-tianxing-chatbot-plugin output-wqy-font-plugin output-r18-plugin output-debug-plugin
.PHONY: deploy-plugins
deploy-plugins: deploy-dev-plugin deploy-console-plugin deploy-tianxing-chatbot-plugin deploy-wqy-font-plugin deploy-r18-plugin deploy-debug-plugin

CHRONIC := $(shell if [[ ! -z "`which chronic`" ]]; then echo "chronic"; fi)

output:
	mkdir output
.PHONY: clean-output
clean-output:
	rm -rf output

.PHONY: home-chara
home-chara:
	if [ ! -d "$(HOME)/.chara/plugin/" ]; then \
		mkdir -p "$(HOME)/.chara/plugin"; \
	fi

.PHONY: core
core: clean-core compile-core build-core output-core
.PHONY: compile-core
compile-core:
	cd core && $(CHRONIC) ./gradlew jar
.PHONY: build-core
build-core:
	cd core && $(CHRONIC) ./gradlew clean jar jlink
.PHONY: clean-core
clean-core:
	cd core && $(CHRONIC) ./gradlew clean
.PHONY: output-core
output-core: output build-core
	rm -rf ./output/image
	cp -r ./core/build/image ./output/
.PHONY: jpackage-core
jpackage-core: output
	cd core && $(CHRONIC) ./gradlew clean jpackagePost
	rm -rf ./output/jpackage
	cp -r ./core/build/jpackage ./output/

.PHONY: kokori
kokori: clean-kokori compile-kokori build-kokori output-kokori
.PHONY: compile-kokori
compile-kokori: compile-core
	cd characters/kokori/ && $(CHRONIC) ./gradlew jar
.PHONY: build-kokori
build-kokori: compile-core
	cd characters/kokori/models/ && $(CHRONIC) ./build.sh
.PHONY: clean-kokori
clean-kokori:
	cd characters/kokori/ && $(CHRONIC) ./gradlew clean
	cd characters/kokori/models/ && rm -f *.model
.PHONY: output-kokori
output-kokori: output build-kokori
	cp characters/kokori/models/*.model output/

.PHONY: dev-plugin
dev-plugin: clean-dev-plugin compile-dev-plugin build-dev-plugin output-dev-plugin deploy-dev-plugin
.PHONY: compile-dev-plugin
compile-dev-plugin: compile-core
	cd plugins/dev/ && $(CHRONIC) ./gradlew jar
.PHONY: build-dev-plugin
build-dev-plugin: compile-core
	cd plugins/dev/ && $(CHRONIC) ./build-plugin.sh
.PHONY: clean-dev-plugin
clean-dev-plugin:
	cd plugins/dev/ && $(CHRONIC) ./gradlew clean
	cd plugins/dev/plugin/ && rm -f *.plugin
.PHONY: output-dev-plugin
output-dev-plugin: output build-dev-plugin
	cp plugins/dev/plugin/*.plugin output/
.PHONY: deploy-dev-plugin
deploy-dev-plugin: output-dev-plugin home-chara
	cp output/dev.plugin ~/.chara/plugin/

.PHONY: console-plugin
console-plugin: clean-console-plugin compile-console-plugin build-console-plugin output-console-plugin deploy-console-plugin
.PHONY: compile-console-plugin
compile-console-plugin: compile-core
	cd plugins/console/ && $(CHRONIC) ./gradlew jar
.PHONY: build-console-plugin
build-console-plugin: compile-core
	cd plugins/console/ && $(CHRONIC) ./build-plugin.sh
.PHONY: clean-console-plugin
clean-console-plugin:
	cd plugins/console/ && $(CHRONIC) ./gradlew clean
	cd plugins/console/plugin/ && rm -f *.plugin
.PHONY: output-console-plugin
output-console-plugin: output build-console-plugin
	cp plugins/console/plugin/*.plugin output/
.PHONY: deploy-console-plugin
deploy-console-plugin: output-console-plugin home-chara
	cp output/console.plugin ~/.chara/plugin/

.PHONY: tianxing-chatbot-plugin
tianxing-chatbot-plugin: clean-tianxing-chatbot-plugin compile-tianxing-chatbot-plugin build-tianxing-chatbot-plugin output-tianxing-chatbot-plugin deploy-tianxing-chatbot-plugin
.PHONY: compile-tianxing-chatbot-plugin
compile-tianxing-chatbot-plugin: compile-core
	cd plugins/tianxing-chatbot/ && $(CHRONIC) ./gradlew clean jar
.PHONY: build-tianxing-chatbot-plugin
build-tianxing-chatbot-plugin: compile-core
	cd plugins/tianxing-chatbot/ && $(CHRONIC) ./build-plugin.sh
.PHONY: clean-tianxing-chatbot-plugin
clean-tianxing-chatbot-plugin:
	cd plugins/tianxing-chatbot/ && $(CHRONIC) ./gradlew clean
	cd plugins/tianxing-chatbot/plugin/ && rm -f *.plugin
.PHONY: output-tianxing-chatbot-plugin
output-tianxing-chatbot-plugin: output build-tianxing-chatbot-plugin
	cp plugins/tianxing-chatbot/plugin/*.plugin output/
.PHONY: deploy-tianxing-chatbot-plugin
deploy-tianxing-chatbot-plugin: output-tianxing-chatbot-plugin home-chara
	cp output/tianxing-chatbot.plugin ~/.chara/plugin/

.PHONY: wqy-font-plugin
wqy-font-plugin: clean-wqy-font-plugin compile-wqy-font-plugin build-wqy-font-plugin output-wqy-font-plugin deploy-wqy-font-plugin
.PHONY: compile-wqy-font-plugin
compile-wqy-font-plugin: compile-core
	cd plugins/wqy-font/ && $(CHRONIC) ./gradlew clean jar
.PHONY: build-wqy-font-plugin
build-wqy-font-plugin: compile-core
	cd plugins/wqy-font/ && $(CHRONIC) ./build-plugin.sh
.PHONY: clean-wqy-font-plugin
clean-wqy-font-plugin:
	cd plugins/wqy-font/ && $(CHRONIC) ./gradlew clean
	cd plugins/wqy-font/plugin/ && rm -f *.plugin
.PHONY: output-wqy-font-plugin
output-wqy-font-plugin: output build-wqy-font-plugin
	cp plugins/wqy-font/plugin/*.plugin output/
.PHONY: deploy-wqy-font-plugin
deploy-wqy-font-plugin: output-wqy-font-plugin home-chara
	cp output/wqy-font.plugin ~/.chara/plugin/

.PHONY: r18-plugin
r18-plugin: clean-r18-plugin compile-r18-plugin build-r18-plugin output-r18-plugin deploy-r18-plugin
.PHONY: compile-r18-plugin
compile-r18-plugin: compile-core
	cd plugins/r18/ && $(CHRONIC) ./gradlew jar
.PHONY: build-r18-plugin
build-r18-plugin: compile-core
	cd plugins/r18/ && $(CHRONIC) ./build-plugin.sh
.PHONY: clean-r18-plugin
clean-r18-plugin:
	cd plugins/r18/ && $(CHRONIC) ./gradlew clean
	cd plugins/r18/plugin/ && rm -f *.plugin
.PHONY: output-r18-plugin
output-r18-plugin: output build-r18-plugin
	cp plugins/r18/plugin/*.plugin output/
.PHONY: deploy-r18-plugin
deploy-r18-plugin: output-r18-plugin home-chara
	cp output/r18.plugin ~/.chara/plugin/

.PHONY: debug-plugin
debug-plugin: clean-debug-plugin compile-debug-plugin build-debug-plugin output-debug-plugin deploy-debug-plugin
.PHONY: compile-debug-plugin
compile-debug-plugin: compile-core
	cd plugins/debug/ && $(CHRONIC) ./gradlew jar
.PHONY: build-debug-plugin
build-debug-plugin: compile-core
	cd plugins/debug/ && $(CHRONIC) ./build-plugin.sh
.PHONY: clean-debug-plugin
clean-debug-plugin:
	cd plugins/debug/ && $(CHRONIC) ./gradlew clean
	cd plugins/debug/plugin/ && rm -f *.plugin
.PHONY: output-debug-plugin
output-debug-plugin: output build-debug-plugin
	cp plugins/debug/plugin/*.plugin output/
.PHONY: deploy-debug-plugin
deploy-debug-plugin: output-debug-plugin home-chara
	cp output/debug.plugin ~/.chara/plugin/

.PHONY: noto-font-plugin
noto-font-plugin: clean-noto-font-plugin compile-noto-font-plugin build-noto-font-plugin output-noto-font-plugin deploy-noto-font-plugin
.PHONY: compile-noto-font-plugin
compile-noto-font-plugin: compile-core
	cd plugins/noto-font/ && $(CHRONIC) ./gradlew jar
.PHONY: build-noto-font-plugin
build-noto-font-plugin: compile-core
	cd plugins/noto-font/ && $(CHRONIC) ./build-plugin.sh
.PHONY: clean-noto-font-plugin
clean-noto-font-plugin:
	cd plugins/noto-font/ && $(CHRONIC) ./gradlew clean
	cd plugins/noto-font/plugin/ && rm -f *.plugin
.PHONY: output-noto-font-plugin
output-noto-font-plugin: output build-noto-font-plugin
	cp plugins/noto-font/plugin/*.plugin output/
.PHONY: deploy-noto-font-plugin
deploy-noto-font-plugin: output-noto-font-plugin home-chara
	cp output/noto-font.plugin ~/.chara/plugin/

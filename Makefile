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
plugins: dev-plugin tianxing-chatbot-plugin
.PHONY: compile-plugins
compile-plugins: compile-dev-plugin compile-tianxing-chatbot-plugin
.PHONY: build-plugins
build-plugins: build-dev-plugin build-tianxing-chatbot-plugin
.PHONY: clean-plugins
clean-plugins: clean-dev-plugin clean-tianxing-chatbot-plugin
.PHONY: output-plugins
output-plugins: output-dev-plugin output-tianxing-chatbot-plugin
.PHONY: deploy-plugins
deploy-plugins: deploy-dev-plugin deploy-tianxing-chatbot-plugin

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
	rm -rf ./output/chara
	cp -r ./core/build/image ./output/chara

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

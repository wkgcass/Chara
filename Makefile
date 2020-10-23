.PHONY: all
all: clean compile output-all

.PHONY: compile
compile: core characters plugins
.PHONY: clean
clean: clean-core clean-characters clean-plugins clean-output
.PHONY: output-all
output-all: output-core output-characters output-plugins
.PHONY: deploy
deploy: deploy-plugins

.PHONY: characters
characters: kokori
.PHONY: clean-characters
clean-characters: clean-kokori
.PHONY: output-characters
output-characters: output-kokori

.PHONY: plugins
plugins: dev-plugin tianxing-chatbot-plugin
.PHONY: clean-plugins
clean-plugins: clean-dev-plugin clean-tianxing-chatbot-plugin
.PHONY: output-plugins
output-plugins: output-dev-plugin output-tianxing-chatbot-plugin
.PHONY: deploy-plugins
deploy-plugins: deploy-dev-plugin deploy-tianxing-chatbot-plugin

output:
	mkdir output
.PHONY: clean-output
clean-output:
	rm -rf output

.PHONY: core
core:
	cd core && ./gradlew clean jar jlink
.PHONY: clean-core
clean-core:
	cd core && ./gradlew clean 1>/dev/null 2>/dev/null
.PHONY: output-core
output-core: output core
	rm -rf ./output/chara
	cp -r ./core/build/image ./output/chara

.PHONY: kokori
kokori:
	cd characters/kokori/models/ && ./build.sh
.PHONY: clean-kokori
clean-kokori:
	cd characters/kokori/ && ./gradlew clean 1>/dev/null 2>/dev/null
	cd characters/kokori/models/ && rm -f *.model
.PHONY: output-kokori
output-kokori: output kokori
	cp characters/kokori/models/*.model output/

.PHONY: dev-plugin
dev-plugin:
	cd plugins/dev/ && ./build-plugin.sh
.PHONY: clean-dev-plugin
clean-dev-plugin:
	cd plugins/dev/ && ./gradlew clean 1>/dev/null 2>/dev/null
	cd plugins/dev/plugin/ && rm -f *.plugin
.PHONY: output-dev-plugin
output-dev-plugin: output dev-plugin
	cp plugins/dev/plugin/*.plugin output/
.PHONY: deploy-dev-plugin
deploy-dev-plugin: output-dev-plugin
	cp output/dev.plugin ~/.chara/plugin/

.PHONY: tianxing-chatbot-plugin
tianxing-chatbot-plugin:
	cd plugins/tianxing-chatbot/ && ./build-plugin.sh
.PHONY: clean-tianxing-chatbot-plugin
clean-tianxing-chatbot-plugin:
	cd plugins/tianxing-chatbot/ && ./gradlew clean 1>/dev/null 2>/dev/null
	cd plugins/tianxing-chatbot/plugin/ && rm -f *.plugin
.PHONY: output-tianxing-chatbot-plugin
output-tianxing-chatbot-plugin: output tianxing-chatbot-plugin
	cp plugins/tianxing-chatbot/plugin/*.plugin output/
.PHONY: deploy-tianxing-chatbot-plugin
deploy-tianxing-chatbot-plugin: output-tianxing-chatbot-plugin
	cp output/tianxing-chatbot.plugin ~/.chara/plugin/
